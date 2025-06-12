package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agung.smashcourt.databinding.ActivityAdminMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Panggil fungsi tampilkan nama dan email admin
        showAdminInfo()

        // ✅ Panggil fungsi statistik
        getTotalUser()
        getTotalOrders()

        binding.menuLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        binding.menuPrivacy.setOnClickListener {
            startActivity(Intent(this, DataUserActivity::class.java))
        }

        binding.menuTerms.setOnClickListener {
            startActivity(Intent(this, DataOrderActivity::class.java))
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Keluar")
            .setMessage("Apakah Anda yakin ingin keluar dari akun admin?")
            .setPositiveButton("Ya") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showAdminInfo() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            binding.textEmail.text = email ?: "Email tidak ditemukan"

            firestore.collection("admins").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val name = document.getString("name") ?: "Admin"
                    binding.textName.text = name
                }
                .addOnFailureListener {
                    binding.textName.text = "Admin"
                }
        } else {
            binding.textEmail.text = "Tidak ada user"
            binding.textName.text = "Admin"
        }
    }

    private fun getTotalUser() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val totalUsers = result.size()
                binding.infoUser.text = totalUsers.toString()
            }
            .addOnFailureListener {
                binding.infoUser.text = "0"
            }
    }

    private fun getTotalOrders() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { userSnapshot ->
                var totalOrders = 0
                val users = userSnapshot.documents
                if (users.isEmpty()) {
                    binding.infoOrder.text = "0"
                    return@addOnSuccessListener
                }

                var completed = 0
                for (userDoc in users) {
                    firestore.collection("users").document(userDoc.id).collection("orders")
                        .get()
                        .addOnSuccessListener { orderSnapshot ->
                            totalOrders += orderSnapshot.size()
                            completed++
                            if (completed == users.size) {
                                binding.infoOrder.text = totalOrders.toString()
                            }
                        }
                        .addOnFailureListener {
                            completed++
                            if (completed == users.size) {
                                binding.infoOrder.text = totalOrders.toString()
                            }
                        }
                }
            }
    }
}
