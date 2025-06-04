package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agung.smashcourt.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // === Pindah ke RegisterActivity ===
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // === Pindah ke RepassActivity ===
        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, RepassActivity::class.java)
            startActivity(intent)
        }

        // === Pindah ke MainActivity ===
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Validasi dan aksi tombol
//        binding.loginButton.setOnClickListener {
//            val Password = binding.tlPassword.text.toString()
//            val confirmEmail = binding.tlEmail.text.toString()
//
//            if (Password.isEmpty() || confirmEmail.isEmpty()) {
//                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
//            } else if (Password != confirmEmail) {
//                Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
//            } else {
//                // TODO: Implementasi logika update password, jika ada API
//                Toast.makeText(this, "Berhasil Masuk", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
    }
}
