package com.example.ecolizer.activities

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ecolizer.R

abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ocultarBarras()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Demian
    fun ocultarBarras() {
        supportActionBar?.hide()

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    // manuel
    protected fun configurarBotonInfo() {
        val btnInfo = findViewById<View>(R.id.btnInfo)

        btnInfo?.setOnClickListener {
            mostrarGuiaReciclaje()
        }
    }

    // victor
    private fun mostrarGuiaReciclaje() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_guide, null)

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnClose = view.findViewById<Button>(R.id.btnCloseInfo)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}