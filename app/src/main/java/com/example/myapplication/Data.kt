package com.example.myapplication

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Data {
    companion object {
        public fun formatarData(data: String): String?{

            val formatoOriginal = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val date: Date? = formatoOriginal.parse(data)
            val formatoDesejado = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
            val dataFormatada = date?.let { formatoDesejado.format(it) }

            return dataFormatada
        }
    }
}