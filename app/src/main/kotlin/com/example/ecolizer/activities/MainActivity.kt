package com.example.ecolizer.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.ecolizer.R
import com.example.ecolizer.databinding.ActivityMainBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.FallosDef
import com.example.ecolizer.room.entities.Fallo
import com.example.ecolizer.viewModels.MainViewModel
import com.example.ecolizer.viewModels.MainViewModelFactory

class MainActivity: BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            falloDao = database.falloDao(),
            materialDao = database.materialDao()
        )
    }

    private var simularEventoContenedorLleno = false
    private var simularEventoMantenimiento = false
    private var simularEventoFueraServicio = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()

        viewModel.fallos.observe(this) { items ->
            validateFallos(items = items)
        }

        beginButton()
    }

    private fun beginButton() {
        binding.btnStart.setOnClickListener {
            val intent = if(simularEventoContenedorLleno) {
                Intent(this, FullContentsActivity::class.java)
            } else if(simularEventoMantenimiento) {
                Intent(this, MaintenanceActivity::class.java)
            } else if(simularEventoFueraServicio) {
                Intent(this, OutServiceActivity::class.java)
            } else {
                Intent(this, SelectionActivity::class.java)
            }

            startActivity(intent)
        }
    }

    private fun validateFallos(items: List<Fallo>) {
        items.find { it.nombre == FallosDef.EVENTO_CONTENEDOR_LLENO.name }?.let {
            simularEventoContenedorLleno = it.activo
        }

        items.find { it.nombre == FallosDef.EVENTO_MANTENIMIENTO.name }?.let {
            simularEventoMantenimiento = it.activo
        }

        items.find { it.nombre == FallosDef.EVENTO_FUERA_SERVICIO.name }?.let {
            simularEventoFueraServicio = it.activo
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}