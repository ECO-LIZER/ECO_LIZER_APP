package com.example.ecolizer.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecolizer.R
import com.example.ecolizer.databinding.ActivitySuccessBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.EstacionesDef
import com.example.ecolizer.room.definitions.EstadoTransaccion
import com.example.ecolizer.room.definitions.FallosDef
import com.example.ecolizer.room.definitions.MaterialesDef
import com.example.ecolizer.room.entities.Fallo
import com.example.ecolizer.room.entities.Transaccion
import com.example.ecolizer.viewModels.MainViewModel
import com.example.ecolizer.viewModels.MainViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class SuccessActivity: BaseActivity() {
    private var _binding: ActivitySuccessBinding? = null
    private val binding get() = _binding!!

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            falloDao = database.falloDao(),
            materialDao = database.materialDao()
        )
    }

    private var simularFalloTransaccion = false
    private var simularFalloNoDetectado = false
    private var simularFalloSistema = false
    private var simularFalloDesconocido = false

    private var cantidadTotal: Int = 0
    private var materialSeleccionado: String = ""
    private val ticketGenerado = generarTicketRandom()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()

        binding.loadingContainer.visibility = View.VISIBLE

        cantidadTotal = intent.getIntExtra("CANTIDAD_TOTAL", 0)
        materialSeleccionado = intent.getStringExtra("MATERIAL_EXTRA") ?: "Material"

        binding.tvQuantity.text = "Has depositado: $cantidadTotal $materialSeleccionado"

        binding.tvTicketResult.text = "Ticket generado: $ticketGenerado\n" +
                "Gracias por reciclar\n" +
                "¡Disfruta tu viaje!"

        viewModel.fallos.observe(this) { items ->
            validateFallos(items = items)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            verificarYMostrarResultados()
        }, 3000)
    }

    private fun verificarYMostrarResultados() {
        when {
            simularFalloTransaccion -> {
                transaccion(estadoTransaccion = EstadoTransaccion.FALLO_TRANSACCION)
                mostrarModalError("¿Sigues ahí?", "La operación está tardando más de lo esperado.")
            }

            simularFalloNoDetectado -> {
                transaccion(estadoTransaccion = EstadoTransaccion.FALLO_IMPRESORA)
                mostrarModalError(
                    "No pudimos verlo",
                    "Por favor, acomoda el material e inténtalo de nuevo."
                )
            }

            simularFalloSistema -> {
                transaccion(estadoTransaccion = EstadoTransaccion.FALLO_SISTEMA)
                mostrarModalError(
                    "Transacción incompleta",
                    "Has ingresado 2 de 3 botellas PET.\nFaltan elementos por introducir."
                )
            }

            simularFalloDesconocido -> {
                transaccion(estadoTransaccion = EstadoTransaccion.FALLO_DESCONOCIDO)
                mostrarModalError("Algo salió mal", "Ocurrió algo inesperado. Código E-04.")
            }

            else -> {
                transaccion(estadoTransaccion = EstadoTransaccion.EXITOSO)
                mostrarResultadoExitoso()

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }, 7000)
            }
        }
    }

    private fun mostrarModalError(titulo: String, mensaje: String) {
        binding.lottieLoading.cancelAnimation()

        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_generic_error, null)
        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val ivError = view.findViewById<ImageView>(R.id.ivIcon)
        val tvTitle = view.findViewById<TextView>(R.id.tvErrorTitle)
        val tvMessage = view.findViewById<TextView>(R.id.tvErrorMessage)
        val btnAccept = view.findViewById<Button>(R.id.btnAcceptError)

        when (titulo) {
            "¿Sigues ahí?" -> ivError.setImageResource(R.drawable.search)
            "No pudimos verlo" -> ivError.setImageResource(R.drawable.searchcancel)
            "Transacción incompleta" -> ivError.setImageResource(R.drawable.warning)
            "Algo salió mal" -> ivError.setImageResource(R.drawable.cancel)

            else -> ivError.setImageResource(R.drawable.cancel)
        }
        tvTitle.text = titulo
        tvMessage.text = mensaje

        btnAccept.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun mostrarResultadoExitoso() {
        binding.lottieLoading.cancelAnimation()
        binding.loadingContainer.visibility = View.GONE
    }

    private fun generarTicketRandom(): String {
        val letra1 = ('A'..'Z').random()
        val letra2 = ('A'..'Z').random()
        val numero = Random.nextInt(10000, 99999)
        return "$letra1$letra2-$numero"
    }

    private fun validateFallos(items: List<Fallo>) {
        items.find { it.nombre == FallosDef.FALLO_TRANSACCION.name }?.let {
            simularFalloTransaccion = it.activo
        }

        items.find { it.nombre == FallosDef.FALLO_NO_DETECTADO.name }?.let {
            simularFalloNoDetectado = it.activo
        }

        items.find { it.nombre == FallosDef.FALLO_SISTEMA.name }?.let {
            simularFalloSistema = it.activo
        }

        items.find { it.nombre == FallosDef.FALLO_DESCONOCIDO.name }?.let {
            simularFalloDesconocido = it.activo
        }
    }

    private fun transaccion(estadoTransaccion: EstadoTransaccion) {
        lifecycleScope.launch {
            database.transaccionDao().replace(
                transaccion = Transaccion(
                    estacionId = EstacionesDef.PARQUE_CENTENARIO.id,
                    materialId = MaterialesDef.from(value = materialSeleccionado).id,
                    fecha = System.currentTimeMillis(),
                    estado = estadoTransaccion.name,
                    hashTicket = ticketGenerado,
                    hashSeguridad = UUID.randomUUID().toString(),
                    usado = false
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}