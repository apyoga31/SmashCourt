package com.agung.smashcourt

import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KelolaLapanganActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LapanganAdapter
//    private lateinit var spinnerFilter: Spinner

    private val orders = mutableListOf<OrderModel>()
    private val allOrders = mutableListOf<OrderModel>() // untuk menyimpan semua data agar bisa difilter ulang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelola_lapangan)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recycler_view_lapangan)
//        spinnerFilter = findViewById(R.id.spinnerFilter)

        adapter = LapanganAdapter(orders)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

//        setupSpinner()
        loadOrders()
    }

//    private fun setupSpinner() {
//        val adapterSpinner = ArrayAdapter.createFromResource(
//            this,
//            R.array.filter_options, // ini ada di strings.xml
//            android.R.layout.simple_spinner_item
//        )
//        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerFilter.adapter = adapterSpinner
//
//        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                filterOrders(spinnerFilter.selectedItem.toString())
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//    }

    private fun filterOrders(filter: String) {
        orders.clear()
        when (filter) {
            "Lunas" -> {
                orders.addAll(allOrders.filter { it.isPay })
            }
            "Belum Lunas" -> {
                orders.addAll(allOrders.filter { !it.isPay })
            }
            else -> { // Semua
                orders.addAll(allOrders)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun loadOrders() {
        val providerId = auth.currentUser?.uid ?: return

        db.collection("providers")
            .document(providerId)
            .collection("orders")
            .get()
            .addOnSuccessListener { documents ->
                orders.clear()
                allOrders.clear()
                for (doc in documents) {
                    val order = doc.toObject(OrderModel::class.java)
                    orders.add(order)
                    allOrders.add(order)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
    }
}
