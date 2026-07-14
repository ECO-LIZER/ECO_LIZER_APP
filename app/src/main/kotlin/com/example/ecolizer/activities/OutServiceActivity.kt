package com.example.ecolizer.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ecolizer.databinding.ActivityOutOfServiceBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.EventosDef
import com.example.ecolizer.room.definitions.toEvento
import kotlinx.coroutines.launch

class OutServiceActivity: BaseActivity() {
    private var _binding: ActivityOutOfServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOutOfServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()
        beginButton()
        evento()
    }

    private fun beginButton() {
        binding.cardOutOfService.setOnClickListener {
            finish()
        }
    }

    private fun evento() {
        lifecycleScope.launch {
            AppDatabase.getDatabase(this@OutServiceActivity).eventoDao().apply {
                replace(EventosDef.FUERA_SERVICIO.toEvento())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}