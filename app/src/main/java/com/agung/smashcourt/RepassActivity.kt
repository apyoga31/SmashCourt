package com.agung.smashcourt

import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agung.smashcourt.databinding.ActivityRepassBinding

class RepassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Validasi dan aksi tombol
        binding.resetPasswordButton.setOnClickListener {
            val newPassword = binding.tlPassword.text.toString()
            val confirmPassword = binding.tlPasswordNew.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Implementasi logika update password, jika ada API
                Toast.makeText(this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
