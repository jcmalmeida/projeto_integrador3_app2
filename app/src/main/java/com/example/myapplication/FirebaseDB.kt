package com.example.myapplication

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirebaseDB {
    companion object {
        suspend fun obterTodosRegistros(
            db: FirebaseFirestore
        ): List<Risco> {

            return try {
                val snapshot = db.collection("riscos").get().await()
                listarRiscosRegistrados(snapshot)
            } catch (e: Exception) {
                emptyList()
            }
        }

        fun listarRiscosRegistrados(documents: QuerySnapshot): List<Risco> {
            var listaRiscos = mutableListOf<Risco>()

            for (document in documents) {
                val idRisco = document.id
                val descricao = document.data.get("descricao").toString()
                val localReferencia = document.data.get("localReferencia").toString()
                val emailUsuario = document.data.get("localReferencia").toString()
                val latitude = document.data.get("latitude").toString()
                val longitude = document.data.get("longitude").toString()
                val imagemBase64 = document.data.get("imagemBase64").toString()

                val timestamp = document.getTimestamp("data")
                val data = timestamp?.toDate()
                val dataFormatada = Data.formatarData(data)

                val risco = Risco(
                    idRisco,
                    descricao,
                    dataFormatada ?: "",
                    localReferencia,
                    emailUsuario,
                    latitude,
                    longitude,
                    imagemBase64
                )
                listaRiscos.add(risco)
            }

            return listaRiscos.toList();
        }
    }
}