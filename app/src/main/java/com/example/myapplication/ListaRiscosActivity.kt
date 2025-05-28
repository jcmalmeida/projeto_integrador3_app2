package com.example.myapplication

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class ListaRiscosActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_riscos)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_riscos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val botaoDeslogar = findViewById<FrameLayout>(R.id.back_container)

        val botaoMapaRisco = findViewById<Button>(R.id.button_ver_mapa_risco)

        botaoMapaRisco.setOnClickListener {
            val intent = Intent(this, MapaRiscoActivity::class.java)
            startActivity(intent)
        }

        botaoDeslogar.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

        listarRiscosRegistrados(this)
    }

    private fun listarRiscosRegistrados(context: ContextWrapper){
        lifecycleScope.launch {
            val riscos = FirebaseDB.obterTodosRegistros(db)

            if (riscos.isEmpty())
                Toast.makeText(
                    baseContext,
                    "Não foi possível obter a lista de riscos registrados.",
                    Toast.LENGTH_SHORT
                ).show()

            if (riscos.size > 0)
                findViewById<TextView>(R.id.tv_sem_risco).visibility = View.GONE

           recyclerView.adapter = MeuAdapter(context, riscos)
        }
    }

    override fun onResume() {
        super.onResume()

        listarRiscosRegistrados(this)
    }
}