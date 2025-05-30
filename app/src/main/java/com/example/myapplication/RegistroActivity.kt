package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistroActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var idRisco: String

    private lateinit var dataRegistroRisco: TextView
    private lateinit var localizacaoRegistroRico: TextView
    private lateinit var descricaoRegistroRisco: TextView
    private lateinit var autorRegistroRisco: TextView
    private lateinit var fotoRegistroRisco: ImageView

    private lateinit var radioGroupStatus: RadioGroup
    private lateinit var radioAtuando: RadioButton
    private lateinit var radioResolvido: RadioButton
    private lateinit var buttonSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        idRisco = intent.getStringExtra("idRisco") ?: ""

        dataRegistroRisco = findViewById(R.id.tv_data_registro)
        localizacaoRegistroRico = findViewById(R.id.tv_localizacao_registro)
        descricaoRegistroRisco = findViewById(R.id.tv_descricao_registro)
        autorRegistroRisco = findViewById(R.id.tv_autor_registro)
        fotoRegistroRisco = findViewById(R.id.iv_foto_registro)

        radioGroupStatus = findViewById(R.id.radioGroupStatus)
        radioAtuando = findViewById(R.id.radioAtuando)
        radioResolvido = findViewById(R.id.radioResolvido)
        buttonSalvar = findViewById(R.id.button_salvar)

        if (idRisco.isNotEmpty()) {
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
                .addOnFailureListener {
                    Toast.makeText(this, "Houve um erro ao buscar o risco", Toast.LENGTH_SHORT).show()
                }
        }

        buttonSalvar.setOnClickListener {
            salvarModificacoes()
        }

        val botaoRetornar = findViewById<ImageButton>(R.id.button_back)
        botaoRetornar.setOnClickListener {
            finish()
        }
    }

    private fun mostrarRiscoRegistrado(document: DocumentSnapshot) {
        val localReferencia = document.getString("localReferencia") ?: ""
        val descricao = document.getString("descricao") ?: ""
        val autor = document.getString("emailUsuario") ?: ""
        val imagemBase64 = document.getString("imagemBase64")

        val timestamp = document.getTimestamp("data")
        val data = timestamp?.toDate()
        val dataFormatada = Data.formatarData(data)

        if (!imagemBase64.isNullOrEmpty()) {
            val imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            fotoRegistroRisco.setImageBitmap(bitmap)
        }

        dataRegistroRisco.text = "Data: $dataFormatada"
        localizacaoRegistroRico.text = "Local: $localReferencia"
        descricaoRegistroRisco.text = "Descrição: $descricao"
        autorRegistroRisco.text = "Quem registrou: $autor"

        val status = document.getString("status")
        when (status) {
            "atuando" -> radioGroupStatus.check(R.id.radioAtuando)
            "resolvido" -> radioGroupStatus.check(R.id.radioResolvido)
        }
    }

    private fun salvarModificacoes() {
        val status = when (radioGroupStatus.checkedRadioButtonId) {
            R.id.radioAtuando -> "atuando"
            R.id.radioResolvido -> "resolvido"
            else -> ""
        }

        if (status.isEmpty()) {
            Toast.makeText(this, "Selecione o status", Toast.LENGTH_SHORT).show()
            return
        }

        val atualizacoes = hashMapOf<String, Any>(
            "status" to status
        )

        db.collection("riscos")
            .document(idRisco)
            .update(atualizacoes)
            .addOnSuccessListener {
                Toast.makeText(this, "Modificações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ListaRiscosActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Falha ao salvar modificações.", Toast.LENGTH_SHORT).show()
            }
    }
}
