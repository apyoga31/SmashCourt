package com.agung.smashcourt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agung.smashcourt.databinding.ItemCartBinding

class AdapterOrder(private val order: List<OrderItem>) :
    RecyclerView.Adapter<AdapterOrder.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = order[position]
        with(holder.binding) {
            image.setImageResource(item.imageResId)
            court.text = item.courtName
            deskripsi.text = item.description
            date.text = item.date
            time.text = item.time
            cost.text = item.price
        }
    }

    override fun getItemCount(): Int = order.size
}