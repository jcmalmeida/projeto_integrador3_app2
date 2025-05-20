package com.example.myapplication

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListaRiscosActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_riscos)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_riscos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val botaoDeslogar = findViewById<FrameLayout>(R.id.back_container)

        obterDadosBD()

        botaoDeslogar.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }
    }

    private fun obterDadosBD()
    {
        db.collection("riscos")
            .get()
            .addOnSuccessListener { documents ->
                listarRiscosRegistrados(documents)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Não foi possível obter a lista de riscos registrados.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun listarRiscosRegistrados(documents: QuerySnapshot){
        var listaRiscos = mutableListOf<Risco>()

        for (document in documents) {
            val descricao = document.data.get("descricao").toString()
            val localReferencia = document.data.get("localReferencia").toString()
            val emailUsuario = document.data.get("localReferencia").toString()
            val latitude = document.data.get("latitude").toString()
            val longitude = document.data.get("longitude").toString()
            val imagemBase64 = document.data.get("imagemBase64").toString()

            val data = document.get("data").toString()
            val dataFormatada = formatarData(data)

            val risco = Risco(
                descricao,
                dataFormatada ?: "",
                localReferencia,
                emailUsuario,
                latitude,
                longitude,
                imagemBase64)
            listaRiscos.add(risco)
        }

        if (listaRiscos.size > 0)
            findViewById<TextView>(R.id.tv_sem_risco).visibility = View.GONE

        recyclerView.adapter = MeuAdapter(listaRiscos)
    }

    private fun formatarData(data: String): String?{

        val formatoOriginal = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val date: Date? = formatoOriginal.parse(data)
        val formatoDesejado = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
        val dataFormatada = date?.let { formatoDesejado.format(it) }

        return dataFormatada
    }

    override fun onResume() {
        super.onResume()

        obterDadosBD()
    }
}