package com.example.ecolizer.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ecolizer.databinding.ActivityValidationBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.FallosDef
import com.example.ecolizer.room.definitions.MaterialesDef
import com.example.ecolizer.room.entities.Fallo
import com.example.ecolizer.viewModels.MainViewModel
import com.example.ecolizer.viewModels.MainViewModelFactory
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class ValidationActivity: BaseActivity() {
    private lateinit var binding: ActivityValidationBinding

    // CONFIGURACIÓN BLUETOOTH
    private val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val address: String = "00:22:09:01:10:70"

    private var socket: BluetoothSocket? = null
    private var isConnected = false

    // VARIABLES DE LÓGICA
    private val requestEnableBT = 1
    private var cantidadActual = 0
    private var cantidadMeta = 5
    private var materialSeleccionado = "PET"

    private var isRunning = false
    private var procesoTerminado = false

    // VARIABLES DE ANIMACIÓN
    private val handlerAnimacion = Handler(Looper.getMainLooper())
    private var runnableAnimacion: Runnable? = null
    private var animacionActiva = false

    private var simularEventoObjetoIncompatible = false
    private var simularEventoTransaccionFallida = false

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            falloDao = database.falloDao(),
            materialDao = database.materialDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()

        materialSeleccionado = intent.getStringExtra("MATERIAL_EXTRA") ?: "Material"
        configurarMetas(materialSeleccionado)

        actualizarTextoConteo()

        binding.tvValidating.text = "Validando..."
        binding.tvValidating.setTextColor(Color.parseColor("#333333"))
        binding.tvSuccess.visibility = View.VISIBLE
        binding.ivCheckmark.visibility = View.GONE

        viewModel.fallos.observe(this) { items ->
            validateFallos(items = items)
        }

        if (verificarPermisos()) {
            conectarYEscuchar()
        }
        iniciarAnimacionEspera()
    }

    private fun configurarMetas(material: String) {
        cantidadMeta = when (material) {
            MaterialesDef.PET.nombre -> MaterialesDef.PET.cantidadRequerida
            MaterialesDef.TETRA.nombre -> MaterialesDef.TETRA.cantidadRequerida
            MaterialesDef.LATA.nombre -> MaterialesDef.LATA.cantidadRequerida
            else -> 5
        }
        cantidadActual = 0
    }

    private fun conectarYEscuchar() {
        Thread {
            try {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@Thread
                }

                if (!isConnected) {
                    bluetoothAdapter.cancelDiscovery()

                    val device = bluetoothAdapter.getRemoteDevice(address)
                    socket = device.createRfcommSocketToServiceRecord(myUUID)
                    socket?.connect()
                    isConnected = true
                }

                runOnUiThread {
                    Toast.makeText(this, "Sistema Conectado", Toast.LENGTH_SHORT).show()
                }
                enviarComandoBluetooth("A")

                val inputStream: InputStream = socket?.inputStream ?: return@Thread
                isRunning = true
                val buffer = ByteArray(1024)
                var bytes: Int

                while (isRunning) {
                    try {
                        if(inputStream.available() > 0) {
                            bytes = inputStream.read(buffer)
                            val mensaje = String(buffer, 0, bytes)

                            if(mensaje.contains("1") && !procesoTerminado) {
                                runOnUiThread {
                                    procesarDeteccion()
                                }
                            }
                        }
                    } catch (_: IOException) {
                        isRunning = false
                        break
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error de conexión BT", Toast.LENGTH_SHORT).show()
                }
                limpiarConexion()
                finish()
            }
        }.start()
    }

    private fun limpiarConexion() {
        try {
            isConnected = false
            socket?.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        socket = null
    }

    private fun procesarDeteccion() {
        cantidadActual++
        actualizarTextoConteo()
        mostrarValidacionOK()

        if(cantidadActual >= cantidadMeta) {
            mostrarExito()
        }
    }

    private fun actualizarTextoConteo() {
        binding.tvMaterialDetected.text =
            "Material detectado: $cantidadActual/$cantidadMeta $materialSeleccionado"
    }

    private fun mostrarValidacionOK() {
        binding.tvValidating.text = "Validando... OK"
        binding.tvValidating.setTextColor(Color.parseColor("#2E7D32"))
        detenerAnimacionEspera()
        binding.tvQuantity.text = "Procesando..."

        Handler(Looper.getMainLooper()).postDelayed({
            if(!procesoTerminado) {
                binding.tvValidating.text = "Validando..."
                binding.tvValidating.setTextColor(Color.parseColor("#333333"))
                iniciarAnimacionEspera()
            }
        }, 2000)
    }

    private fun mostrarExito() {
        procesoTerminado = true

        Handler(Looper.getMainLooper()).postDelayed({
            detenerAnimacionEspera()
            binding.tvQuantity.text = "¡Depósito Completo! "
            binding.tvValidating.text = "Validando... OK"
            binding.tvValidating.setTextColor(Color.parseColor("#2E7D32"))
            binding.ivCheckmark.visibility = View.VISIBLE

            // Enviar señal de cierre al Arduino
            enviarComandoBluetooth("C")

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = if(simularEventoObjetoIncompatible) {
                    Intent(this@ValidationActivity, ObjectIncompatibleActivity::class.java)
                } else if(simularEventoTransaccionFallida) {
                    Intent(this@ValidationActivity, TransactionFailedActivity::class.java)
                } else {
                    val intent = Intent(this@ValidationActivity, SuccessActivity::class.java)
                    intent.putExtra("CANTIDAD_TOTAL", cantidadActual)
                    intent.putExtra("MATERIAL_EXTRA", materialSeleccionado)
                    intent
                }

                startActivity(intent)
                finish()
            }, 2000)

        }, 1500)
    }

    private fun enviarComandoBluetooth(comando: String) {
        Thread {
            try {
                socket?.outputStream?.write(comando.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun verificarPermisos(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val connectPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            val scanPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)

            if (connectPermission != PackageManager.PERMISSION_GRANTED || scanPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN), requestEnableBT)
                return false
            }
        } else {
            val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestEnableBT)
                return false
            }
        }
        return true
    }

    private fun iniciarAnimacionEspera() {
        if(animacionActiva) return
        animacionActiva = true
        runnableAnimacion = object: Runnable {
            var numPuntos = 3
            var subiendo = false
            override fun run() {
                val puntosStr = ".".repeat(numPuntos)
                binding.tvQuantity.text = "Esperando depósito $puntosStr"
                if(subiendo) {
                    numPuntos++
                    if(numPuntos == 3) subiendo = false
                } else {
                    numPuntos--
                    if(numPuntos == 0) subiendo = true
                }
                handlerAnimacion.postDelayed(this, 500)
            }
        }
        handlerAnimacion.post(runnableAnimacion!!)
    }

    private fun detenerAnimacionEspera() {
        animacionActiva = false
        runnableAnimacion?.let { handlerAnimacion.removeCallbacks(it) }
    }

    private fun validateFallos(items: List<Fallo>) {
        items.find { it.nombre == FallosDef.ERROR_OBJETO_INCOMPATIBLE.name }?.let {
            simularEventoObjetoIncompatible = it.activo
        }

        items.find { it.nombre == FallosDef.ERROR_TRANSACCION_FALLIDA.name }?.let {
            simularEventoTransaccionFallida = it.activo
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestEnableBT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario acaba de dar los permisos, ¡ahora sí nos conectamos!
                conectarYEscuchar()
            } else {
                Toast.makeText(this, "Los permisos son necesarios para conectar con la máquina", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        detenerAnimacionEspera()
        try {
            socket?.close()
            socket = null
        } catch (e: Exception) { e.printStackTrace() }
    }
}