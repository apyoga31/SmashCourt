package com.agung.smashcourt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DateAdapter(
    private val list: List<Pair<String, String>>
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvHari: TextView = itemView.findViewById(R.id.tvHari)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val (tanggal, hari) = list[position]
        holder.tvTanggal.text = tanggal
        holder.tvHari.text = hari
    }

    override fun getItemCount(): Int = list.size
}
