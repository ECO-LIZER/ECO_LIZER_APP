package com.example.ecolizer.activities

import android.os.Bundle
import com.example.ecolizer.databinding.ActivityObjectIncompatibleBinding

class ObjectIncompatibleActivity: BaseActivity() {
    private var _binding: ActivityObjectIncompatibleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityObjectIncompatibleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()
        beginButton()
    }

    private fun beginButton() {
        binding.btnRetry.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}