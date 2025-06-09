package com.agung.smashcourt

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class DateAdapter(
    private val list: List<Pair<String, String>>,
    private val onDateSelected: (String) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private var selectedPosition = -1

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvHari: TextView = itemView.findViewById(R.id.tvHari)
        val cardView: MaterialCardView = itemView as MaterialCardView

        init {
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onDateSelected(list[adapterPosition].first)
            }
        }
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

        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#A10F1C"))
            holder.cardView.strokeWidth = 4
            holder.cardView.strokeColor = Color.BLACK
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
            holder.cardView.strokeWidth = 2
            holder.cardView.strokeColor = Color.LTGRAY
        }
    }

    override fun getItemCount(): Int = list.size
}
