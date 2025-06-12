package com.agung.smashcourt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.smashcourt.databinding.ActivityDataOrderBinding
import com.google.firebase.firestore.FirebaseFirestore

class DataOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataOrderBinding
    private lateinit var adapter: OrderAdapter
    private val orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = OrderAdapter(orderList)
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = adapter

        fetchAllOrders()
    }

    private fun fetchAllOrders() {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .get()
            .addOnSuccessListener { userDocs ->
                for (userDoc in userDocs) {
                    val userId = userDoc.id
                    db.collection("users").document(userId).collection("orders")
                        .get()
                        .addOnSuccessListener { orderDocs ->
                            for (orderDoc in orderDocs) {
                                val order = orderDoc.toObject(Order::class.java)
                                orderList.add(order)
                            }
                            adapter.notifyDataSetChanged()
                        }
                }
            }
    }
}