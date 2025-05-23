package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MapaRiscoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_risco)

        val botaoRetornar = findViewById<ImageButton>(R.id.button_back)

        botaoRetornar.setOnClickListener {
            finish()
        }
    }
}