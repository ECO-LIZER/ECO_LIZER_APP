package com.example.ecolizer.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ecolizer.databinding.ActivityMaintenanceBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.EventosDef
import com.example.ecolizer.room.definitions.toEvento
import kotlinx.coroutines.launch

class

MaintenanceActivity: BaseActivity() {
    private var _binding: ActivityMaintenanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()
        beginButton()
        evento()
    }

    private fun beginButton() {
        binding.cardMaintenance.setOnClickListener {
            finish()
        }
    }

    private fun evento() {
        lifecycleScope.launch {
            AppDatabase.getDatabase(this@MaintenanceActivity).eventoDao().apply {
                replace(EventosDef.MANTENIMIENTO.toEvento())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}