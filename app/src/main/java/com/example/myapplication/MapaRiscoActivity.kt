package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class MapaRiscoActivity : AppCompatActivity(), OnMapReadyCallback {

    private val db = Firebase.firestore
    private lateinit var mMap: GoogleMap
    private var pontos: List<Quadruple> = emptyList()
    private var mapaPronto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_risco)

        val botaoRetornar = findViewById<ImageButton>(R.id.button_back)

        botaoRetornar.setOnClickListener {
            finish()
        }

        listarRiscosRegistrados()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapaPronto = true
        mostrarPontosNoMapa()
    }

    private fun mostrarPontosNoMapa() {
        if (!mapaPronto || pontos.isEmpty()) return

        for ((id, nome, lat, lon) in pontos) {
            val pos = LatLng(lat, lon)
            val marker = mMap.addMarker(MarkerOptions()
                .position(pos)
                .title(nome)
                .snippet("Clique para ver mais informações"))

            marker?.tag = id
        }

        mMap.setOnInfoWindowClickListener { marker ->
            val id = marker.tag as? String
            val intent = Intent(this, RegistroActivity::class.java)
            intent.putExtra("idRisco", id)
            startActivity(intent)
        }

        val primeiro = pontos.first()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(primeiro.third, primeiro.fourth), 13f))
    }

    private fun listarRiscosRegistrados(){
        lifecycleScope.launch {
            val riscos = FirebaseDB.obterTodosRegistros(db)

            if (riscos.isEmpty())
                Toast.makeText(
                    baseContext,
                    "Não foi possível obter a lista de riscos registrados.",
                    Toast.LENGTH_SHORT
                ).show()

            if (riscos.size > 0) {
                val pontosObtidos: MutableList<Quadruple> = mutableListOf()

                for (risco in riscos) {
                    pontosObtidos.add(
                        Quadruple(
                            risco.id,
                            risco.data,
                            risco.latitude.toDouble(),
                            risco.longitude.toDouble()
                        )
                    )
                }

                pontos = pontosObtidos.toList()

                mostrarPontosNoMapa()
            }
        }
    }

    data class Quadruple(
        val first: String,
        val second: String,
        val third: Double,
        val fourth: Double
    )
}