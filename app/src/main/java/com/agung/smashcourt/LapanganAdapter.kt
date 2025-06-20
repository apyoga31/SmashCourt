package com.agung.smashcourt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agung.smashcourt.databinding.ItemLapanganBinding

class LapanganAdapter(private val orders: List<OrderModel>) :
    RecyclerView.Adapter<LapanganAdapter.LapanganViewHolder>() {

    inner class LapanganViewHolder(val binding: ItemLapanganBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapanganViewHolder {
        val binding = ItemLapanganBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapanganViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapanganViewHolder, position: Int) {
        val order = orders[position]
        with(holder.binding) {
            textOrderName.text = order.orderName
            textOrderPrice.text = "Rp ${order.price}"

            // Format tanggal (gunakan SimpleDateFormat)
            val sdf = java.text.SimpleDateFormat("dd MMMM yyyy, HH:mm", java.util.Locale("id", "ID"))
            textOrderDate.text = order.date?.toDate()?.let { sdf.format(it) } ?: "-"

            // Tipe
            textOrderType.text = order.type

            // Status bayar dari isPay
//            if (order.isPay) {
//                textOrderStatus.text = "Lunas"
//                textOrderStatus.setTextColor(android.graphics.Color.parseColor("#2E7D32")) // hijau
//            } else {
//                textOrderStatus.text = "Belum Lunas"
//                textOrderStatus.setTextColor(android.graphics.Color.parseColor("#B00020")) // merah
//            }
        }
    }

    override fun getItemCount(): Int = orders.size
}