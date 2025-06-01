package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MeuAdapter(
    private val context: Context,
    private val listaRiscos: List<Risco>
) : RecyclerView.Adapter<MeuAdapter.RiscoViewHolder>() {

    class RiscoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.titulo_risco)
        val data: TextView = itemView.findViewById(R.id.data_risco)
        val local: TextView = itemView.findViewById(R.id.local_risco)
        val image: ImageView = itemView.findViewById(R.id.ic_camera)
        val clickableArea: LinearLayout = itemView.findViewById(R.id.linear_layout_for_click)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiscoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return RiscoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiscoViewHolder, position: Int) {
        val risco = listaRiscos[position]
        holder.titulo.text = risco.descricao

        // ✅ Conversão de Timestamp? para String usando SimpleDateFormat
        val dataFormatada = risco.data?.toDate()?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: "Data não informada"
        holder.data.text = dataFormatada

        holder.local.text = risco.localReferencia

        if (risco.imagemBase64 != null) {
            val imageBytes = Base64.decode(risco.imagemBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.image.setImageBitmap(bitmap)
        }

        holder.clickableArea.setOnClickListener {
            val intent = Intent(context, RegistroActivity::class.java)
            intent.putExtra("idRisco", risco.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaRiscos.size
}
