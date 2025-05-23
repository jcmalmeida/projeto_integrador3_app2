package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class RegistroActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var idRisco: String
    private lateinit var dataRegistroRisco: TextView
    private lateinit var localizacaoRegistroRico: TextView
    private lateinit var descricaoRegistroRisco: TextView
    private lateinit var autorRegistroRisco: TextView
    private lateinit var fotoRegistroRisco: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        idRisco = intent.getStringExtra("idRisco") ?: ""
        dataRegistroRisco = findViewById(R.id.tv_data_registro)
        localizacaoRegistroRico = findViewById(R.id.tv_localizacao_registro)
        descricaoRegistroRisco = findViewById(R.id.tv_descricao_registro)
        autorRegistroRisco = findViewById(R.id.tv_autor_registro)
        fotoRegistroRisco = findViewById(R.id.iv_foto_registro)

        if (idRisco != "") {
            db.collection("riscos")
                .document(idRisco)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        mostrarRiscoRegistrado(document)
                    } else {
                        Toast.makeText(this, "Houve um erro ao buscar o risco", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Houve um erro ao buscar o risco",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        val botaoRetornar = findViewById<ImageButton>(R.id.button_back)

        botaoRetornar.setOnClickListener {
            finish()
        }
    }

    private fun mostrarRiscoRegistrado(document: DocumentSnapshot) {
        val data = document.data?.get("data").toString()
        val localReferencia = document.data?.get("localReferencia").toString()
        val descricao = document.data?.get("descricao").toString()
        val autor = document.data?.get("localReferencia").toString()
        val imagemBase64 = document.data?.get("imagemBase64").toString()
        val dataFormatada = Data.formatarData(data)

        if (imagemBase64 != null) {
            val imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            fotoRegistroRisco.setImageBitmap(bitmap)
        }

        dataRegistroRisco.text = "Data: $dataFormatada"
        localizacaoRegistroRico.text = "Local: $localReferencia"
        descricaoRegistroRisco.text = "Descrição: $descricao"
        autorRegistroRisco.text = "Quem registrou: $autor"
    }
}