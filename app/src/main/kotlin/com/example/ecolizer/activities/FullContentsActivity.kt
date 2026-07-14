package com.example.ecolizer.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ecolizer.databinding.ActivityFullContentsBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.EventosDef
import com.example.ecolizer.room.definitions.toEvento
import kotlinx.coroutines.launch

class FullContentsActivity: BaseActivity() {
    private var _binding: ActivityFullContentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFullContentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()
        beginButton()
        evento()
    }

    private fun beginButton() {
        binding.cardFullAlert.setOnClickListener {
            finish()
        }
    }

    private fun evento() {
        lifecycleScope.launch {
            AppDatabase.getDatabase(this@FullContentsActivity).eventoDao().apply {
                replace(EventosDef.CONTENEDOR_LLENO.toEvento())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}