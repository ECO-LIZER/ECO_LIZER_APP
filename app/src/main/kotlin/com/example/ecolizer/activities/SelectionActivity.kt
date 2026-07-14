package com.example.ecolizer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.graphics.toColorInt
import com.example.ecolizer.databinding.ActivitySelectionBinding
import com.example.ecolizer.room.database.AppDatabase
import com.example.ecolizer.room.definitions.MaterialesDef
import com.example.ecolizer.room.entities.Material
import com.example.ecolizer.viewModels.MainViewModel
import com.example.ecolizer.viewModels.MainViewModelFactory
import com.google.android.material.card.MaterialCardView

class SelectionActivity: BaseActivity() {
    private var _binding: ActivitySelectionBinding? = null
    private val binding get() = _binding!!

    private var enablePet = true
    private var enableTetra = true
    private var enableAlum = true

    private val defaultColor = "#B1FB56".toColorInt()
    private val selectedColor = "#E4F5DA".toColorInt()
    private val selectedStrokeColor = "#2E7D32".toColorInt()
    private val disabledColor = "#E0E0E0".toColorInt()

    private var materialSeleccionado: String? = null

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            falloDao = database.falloDao(),
            materialDao = database.materialDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonInfo()

        viewModel.materiales.observe(this) { items ->
            configurarSeleccionTarjetas(items)
        }

        binding.btnContinue.setOnClickListener {
            validarYContinuar()
        }
    }

    private fun configurarSeleccionTarjetas(items: List<Material>) = binding.apply {
        items.find { it.materialId == MaterialesDef.PET.id }?.let {
            enablePet = it.activo
            textPet.text = it.equivalenciaTicket + "\n" + it.nombre
        }

        items.find { it.materialId == MaterialesDef.TETRA.id }?.let {
            enableTetra = it.activo
            textTetra.text = it.equivalenciaTicket + "\n" + it.nombre
        }

        items.find { it.materialId == MaterialesDef.LATA.id }?.let {
            enableAlum = it.activo
            textLatas.text = it.equivalenciaTicket + "\n" + it.nombre
        }

        fun selectCard(cardView: MaterialCardView) {
            if(enablePet) {
                cardPet.setCardBackgroundColor(defaultColor)
                cardPet.strokeWidth = 0
                cardPet.cardElevation = 18f
            }

            if(enableTetra) {
                cardTetra.setCardBackgroundColor(defaultColor)
                cardTetra.strokeWidth = 0
                cardTetra.cardElevation = 18f
            }

            if(enableAlum) {
                cardAlum.setCardBackgroundColor(defaultColor)
                cardAlum.strokeWidth = 0
                cardAlum.cardElevation = 18f
            }

            cardView.strokeWidth = 4
            cardView.strokeColor = selectedStrokeColor
            cardView.setCardBackgroundColor(selectedColor)
        }

        if(enablePet) {
            habilitarTarjeta(cardView = cardPet)
            cardPet.setOnClickListener {
                selectCard(cardView = cardPet)
                materialSeleccionado = MaterialesDef.PET.nombre
            }
        } else {
            deshabilitarTarjeta(cardView = cardPet)
        }

        if(enableTetra) {
            habilitarTarjeta(cardView = cardTetra)
            cardTetra.setOnClickListener {
                selectCard(cardView = cardTetra)
                materialSeleccionado = MaterialesDef.TETRA.nombre
            }
        } else {
            deshabilitarTarjeta(cardView = cardTetra)
        }

        if(enableAlum) {
            habilitarTarjeta(cardView = cardAlum)
            cardAlum.setOnClickListener {
                selectCard(cardView = cardAlum)
                materialSeleccionado = MaterialesDef.LATA.nombre
            }
        } else {
            deshabilitarTarjeta(cardView = cardAlum)
        }
    }

    private fun validarYContinuar() {
        if(materialSeleccionado != null) {
            val intent = Intent(this, ValidationActivity::class.java)
            intent.putExtra("MATERIAL_EXTRA", materialSeleccionado)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Por favor, selecciona un material disponible", Toast.LENGTH_SHORT).show()
        }
    }

    private fun habilitarTarjeta(cardView: MaterialCardView) {
        cardView.setCardBackgroundColor(defaultColor)
        cardView.strokeWidth = 1
        cardView.cardElevation = 1f
        cardView.alpha = 1f
        cardView.isClickable = true
        cardView.isFocusable = true
    }

    private fun deshabilitarTarjeta(cardView: MaterialCardView) {
        cardView.setCardBackgroundColor(disabledColor)
        cardView.strokeWidth = 0
        cardView.cardElevation = 0f
        cardView.alpha = 0.5f
        cardView.isClickable = false
        cardView.isFocusable = false
        cardView.setOnClickListener(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}