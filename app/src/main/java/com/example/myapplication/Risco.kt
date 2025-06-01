package com.example.myapplication

import com.google.firebase.Timestamp

data class Risco(
    val id: String,
    val descricao: String,
    val data: Timestamp? = null,
    val localReferencia: String,
    val emailUsuario: String,
    val latitude: String,
    val longitude: String,
    val imagemBase64: String?
)

