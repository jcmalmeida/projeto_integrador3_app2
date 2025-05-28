package com.example.myapplication

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Data {
    companion object {
        fun formatarData(data: Date?): String? {
            if (data == null) return null

            val formatoDesejado = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
            return formatoDesejado.format(data)
        }
    }
}