package com.agung.smashcourt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agung.smashcourt.databinding.ItemCourtBinding

class CourtAdapter(
    private val court: List<Court>,
    private val onItemClick: (Court, Int) -> Unit
) : RecyclerView.Adapter<CourtAdapter.CourtViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtViewHolder {
        val binding = ItemCourtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourtViewHolder, position: Int) {
        val item = court[position]
        holder.binding.apply {
            tvTitle.text = item.name
            ivCourt.setImageResource(item.image)

            // ✅ Kirim item + posisi saat diklik
            root.setOnClickListener {
                onItemClick(item, position)
            }
        }
    }

    override fun getItemCount(): Int = court.size

    class CourtViewHolder(val binding: ItemCourtBinding) : RecyclerView.ViewHolder(binding.root)
}
