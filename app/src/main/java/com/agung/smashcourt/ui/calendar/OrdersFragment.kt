package com.agung.smashcourt.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.smashcourt.AdapterItem
import com.agung.smashcourt.CartItem
import com.agung.smashcourt.R
import com.agung.smashcourt.databinding.FragmentOrdersBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private var orderListener: ListenerRegistration? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val orderItems = mutableListOf<CartItem>()
    private lateinit var adapter: AdapterItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = AdapterItem(orderItems)
        binding.recyclerViewOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOrder.adapter = adapter

        // Ambil userId dari FirebaseAuth dan ambil data dari Firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            listenToPaidOrders(userId)
        }

        return root
    }

    private fun listenToPaidOrders(userId: String) {
        orderListener?.remove()
        orderListener = firestore.collection("users").document(userId).collection("orders")
            .whereEqualTo("isPay", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                orderItems.clear()
                for (doc in snapshot.documents) {
                    val courtName = doc.getString("courtName") ?: "Lapangan"
                    val price = doc.getLong("price")?.toInt() ?: 0
                    val type = doc.getString("type") ?: "sewa lapangan"
                    val timestamp = doc.getTimestamp("date")
                    val (dateStr, timeStr, desc) = formatDateTimeDesc(timestamp)
                    val image = doc.getString("imageName")

                    orderItems.add(
                        CartItem(
                            imageResId = R.drawable.court,
                            courtName = courtName,
                            description = desc,
                            date = dateStr,
                            time = timeStr,
                            price = "RP. " + price.toString().replace("\\B(?=(\\d{3})+(?!\\d))".toRegex(), ".")
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun formatDateTimeDesc(timestamp: Timestamp?): Triple<String, String, String> {
        if (timestamp == null) return Triple("-", "-", "-")
        val cal = java.util.Calendar.getInstance()
        cal.time = timestamp.toDate()
        val hariList = listOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
        val hari = hariList[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1]
        val tgl = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale("id", "ID")).format(cal.time)
        val jamMulai = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val jamSelesai = jamMulai + 1
        val timeStr = String.format("%02d.00-%02d.00", jamMulai, jamSelesai)
        val desc = when (jamMulai) {
            in 6..10 -> "Pagi"
            in 11..14 -> "Siang"
            in 15..17 -> "Sore"
            else -> "Malam"
        }
        return Triple("$hari, $tgl", timeStr, desc)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        orderListener?.remove()
        _binding = null
    }
}