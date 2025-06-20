package com.agung.smashcourt

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(private val orderList: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrderName: TextView = itemView.findViewById(R.id.textOrderName)
        val textType: TextView = itemView.findViewById(R.id.textType)
        val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        val textQuantity: TextView = itemView.findViewById(R.id.textQuantity)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
//        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        // Format tanggal
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val formattedDate = order.date?.let { sdf.format(it.toDate()) } ?: "-"

        // Set data umum
        holder.textType.text = "Tipe: ${order.type}"
        holder.textPrice.text = "Harga: Rp${order.price}"
        holder.textDate.text = "Tanggal: $formattedDate"
//        holder.textStatus.text = if (order.isPay) "Sudah Dibayar" else "Belum Dibayar"

        // Logika nama + quantity
        if (order.type == "sewa lapangan") {
            holder.textOrderName.text = order.orderName ?: "Booking"
            holder.textQuantity.visibility = View.GONE
        } else if (order.type == "sewa alat") {
            holder.textOrderName.text = order.itemName ?: "Sewa Alat"
            holder.textQuantity.text = "Jumlah: ${order.quantity ?: 0}"
            holder.textQuantity.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = orderList.size
}
