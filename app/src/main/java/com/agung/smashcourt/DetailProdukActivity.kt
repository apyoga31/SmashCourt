package com.agung.smashcourt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityDetailProdukBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProdukBinding
    private var quantity = 0
    private val hargaPerItem = 7000 // Harga per raket (bisa kamu ubah)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol close
        binding.buttonclose.setOnClickListener {
            finish()
        }

        // Tombol tambah jumlah
        binding.buttonPlus.setOnClickListener {
            quantity++
            updateQuantityUI()
        }

        // Tombol kurang jumlah
        binding.buttonMinus.setOnClickListener {
            if (quantity > 0) {
                quantity--
                updateQuantityUI()
            }
        }

        // Tombol tambah ke keranjang
        binding.buttonKeranjang.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (quantity == 0) {
                Toast.makeText(this, "Jumlah tidak boleh 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val namaProduk = binding.textNama.text.toString()
            val totalHarga = hargaPerItem * quantity

            val orderData = hashMapOf(
                "itemName" to namaProduk,
                "price" to totalHarga,
                "quantity" to quantity,
                "type" to "sewa alat",
                "isPay" to false,
                "date" to Timestamp.now()
            )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("orders")
                .add(orderData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menambahkan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateQuantityUI() {
        binding.textQuantity.text = quantity.toString()
    }
}
