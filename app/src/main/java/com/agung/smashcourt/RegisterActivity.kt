package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
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

        // Siapkan opsi role untuk spinner
        val roles = listOf("Pelanggan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roleSpinner.adapter = adapter

        // Tombol daftar ditekan
        binding.registerButton.setOnClickListener {

            val name = binding.tlName.text.toString().trim()
            val email = binding.tlEmail.text.toString().trim()
            val password = binding.tlPassword.text.toString()
            val confirmPassword = binding.tlPasswordNew.text.toString()
            val isTermsChecked = binding.termsCheckbox.isChecked
            val selectedRole = binding.roleSpinner.selectedItem.toString()

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

            if (selectedRole == "Pilih Peran") {
                showToast("Silakan pilih peran terlebih dahulu.")
                return@setOnClickListener
            }

            // Registrasi dengan Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val userId = result.user?.uid ?: return@addOnSuccessListener

                    // Simpan data pengguna ke Firestore
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "role" to selectedRole
                    )

                    db.collection("users").document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            showToast("Registrasi berhasil.")
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            showToast("Gagal menyimpan data: ${it.message}")
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
