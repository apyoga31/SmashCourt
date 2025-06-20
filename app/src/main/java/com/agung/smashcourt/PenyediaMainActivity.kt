package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityPenyediaMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PenyediaMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenyediaMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenyediaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val uid = auth.currentUser?.uid ?: return

        // Tombol kembali
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Ambil nama dan email dari koleksi "users/{uid}"
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val name = document.getString("name") ?: "Tanpa Nama"
                val email = document.getString("email") ?: "-"
                binding.textName.text = name
                binding.textEmail.text = email
            }
            .addOnFailureListener {
                binding.textName.text = "Tanpa Nama"
                binding.textEmail.text = "-"
            }

        // Ambil jumlah total order dari providers/{uid}/orders
        firestore.collection("providers").document(uid).collection("orders")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val total = querySnapshot.size()
                binding.infoOrder.text = total.toString()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat data order", Toast.LENGTH_SHORT).show()
            }

        // Aksi ke KelolaLapanganActivity
        binding.menuKelolaLapangan.setOnClickListener {
            startActivity(Intent(this, KelolaLapanganActivity::class.java))
        }

        // Logout
        binding.menuLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
