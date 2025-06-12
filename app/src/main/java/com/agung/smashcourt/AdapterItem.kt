package com.agung.smashcourt

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agung.smashcourt.databinding.ItemCartBinding

class AdapterItem(private val item: List<CartItem>) :
    RecyclerView.Adapter<AdapterItem.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = item[position]
        with(holder.binding) {
            image.setImageResource(item.imageResId)
            cost.text = item.price

            if (item.orderName.isNullOrBlank()) {
                deskripsi.visibility = View.GONE
                date.visibility = View.GONE
                time.visibility = View.GONE

                court.text = item.itemName
                quantity.text = "Jumlah: ${item.quantity}"
                quantity.visibility = View.VISIBLE
            } else if (item.itemName.isNullOrBlank()) {
                quantity.visibility = View.GONE

                court.text = item.orderName
                deskripsi.text = item.description
                date.text = item.date
                time.text = item.time
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int = item.size
}
