package com.example.myapplication

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListaRiscosActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var campoDataFiltro: EditText
    private lateinit var textoSemRisco: TextView
    private var listaCompletaRiscos = listOf<Risco>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_riscos)

        recyclerView = findViewById(R.id.recycler_riscos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        campoDataFiltro = findViewById(R.id.et_data_filtro)
        textoSemRisco = findViewById(R.id.tv_sem_risco)

        val botaoDeslogar = findViewById<FrameLayout>(R.id.back_container)
        val botaoMapaRisco = findViewById<Button>(R.id.button_ver_mapa_risco)

        botaoMapaRisco.setOnClickListener {
            startActivity(Intent(this, MapaRiscoActivity::class.java))
        }

        botaoDeslogar.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

        // Aplica máscara no campo de data
        campoDataFiltro.addTextChangedListener(DateInputMask(campoDataFiltro))

        // Adiciona filtro quando a data é modificada
        campoDataFiltro.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filtrarPorData(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        listarRiscosRegistrados(this)
    }

    private fun listarRiscosRegistrados(context: ContextWrapper) {
        lifecycleScope.launch {
            val riscos = FirebaseDB.obterTodosRegistros(db)
            listaCompletaRiscos = riscos

            if (riscos.isEmpty()) {
                textoSemRisco.visibility = View.VISIBLE
            } else {
                textoSemRisco.visibility = View.GONE
            }

            recyclerView.adapter = MeuAdapter(context, riscos)
        }
    }

    private fun filtrarPorData(dataSelecionada: String) {
        if (dataSelecionada.isEmpty()) {
            recyclerView.adapter = MeuAdapter(this, listaCompletaRiscos)
            textoSemRisco.visibility =
                if (listaCompletaRiscos.isEmpty()) View.VISIBLE else View.GONE
            return
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val filtrados = listaCompletaRiscos.filter { risco ->
            val timestamp = risco.data
            if (timestamp is Timestamp) {
                val dataFormatada = sdf.format(timestamp.toDate())
                dataFormatada == dataSelecionada
            } else {
                false
            }
        }

        recyclerView.adapter = MeuAdapter(this, filtrados)
        textoSemRisco.visibility = if (filtrados.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        listarRiscosRegistrados(this)
    }
}
