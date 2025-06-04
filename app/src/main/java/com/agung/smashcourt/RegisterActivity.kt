package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {

            showToast("Register button clicked")

            val name = binding.tlName.text.toString().trim()
            val email = binding.tlEmail.text.toString().trim()
            val password = binding.tlPassword.text.toString()
            val confirmPassword = binding.tlPasswordNew.text.toString()
            val isTermsChecked = binding.termsCheckbox.isChecked

            // Validasi input
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Semua field harus diisi.")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Email tidak valid.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showToast("Password minimal 6 karakter.")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showToast("Konfirmasi password tidak cocok.")
                return@setOnClickListener
            }

            if (!isTermsChecked) {
                showToast("Harap setujui ketentuan & kebijakan.")
                return@setOnClickListener
            }

            // Proses registrasi Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val userId = result.user?.uid
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email
                    )

                    db.collection("users").document(userId ?: "")
                        .set(userMap)
                        .addOnSuccessListener {
                            showToast("Registrasi berhasil.")
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            showToast("Gagal simpan data: ${it.message}")
                        }
                }
                .addOnFailureListener {
                    showToast("Registrasi gagal: ${it.message}")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
