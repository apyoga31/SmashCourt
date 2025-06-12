package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, RepassActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.editText?.text.toString().trim()
            val password = binding.passwordEditText.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        // Ambil dokumen berdasarkan UID dari koleksi "users" (huruf kecil sesuai Firebase)
                        firestore.collection("users").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val role = document.getString("role") ?: "User"
                                    Log.d("LoginDebug", "UID: $uid, Role: $role")

                                    if (role == "Admin") {
                                        Toast.makeText(this, "Login sebagai Admin", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, AdminMainActivity::class.java))
                                    } else {
                                        Toast.makeText(this, "Login sebagai User", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                    }
                                    finish()
                                } else {
                                    Toast.makeText(this, "Dokumen tidak ditemukan", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Gagal membaca data pengguna", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
