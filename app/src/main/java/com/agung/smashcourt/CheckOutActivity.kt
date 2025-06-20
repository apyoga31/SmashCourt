package com.agung.smashcourt

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityCheckOutBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class CheckOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckOutBinding
    private val bookingDurationMillis = 15 * 60 * 1000L // 15 menit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Ambil data dari intent
        val selectedTime = intent.getStringExtra("SELECTED_TIME") ?: "-"
        val selectedDate = intent.getStringExtra("SELECTED_DATE") ?: "-"
        val selectedCourt = intent.getStringExtra("SELECTED_COURT") ?: "-"
        val courtName = intent.getStringExtra("COURT_NAME") ?: "-"
        val providerId = intent.getStringExtra("PROVIDER_ID") ?: ""

        // Tampilkan detail transaksi
        binding.transactionDetail.text = "$courtName\n$selectedCourt\n$selectedDate, $selectedTime"

        startCountdown()

        binding.addToCartButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateTimeStr = "$selectedDate $selectedTime".replace(".", ":")
            val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))
            val date = sdf.parse(dateTimeStr)

            if (date == null) {
                Toast.makeText(this, "Format tanggal atau jam salah", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val timestamp = Timestamp(date)
            val type = if (courtName.lowercase().contains("alat")) "sewa alat" else "sewa lapangan"
            val price = 20000

            val orderData = hashMapOf(
                "orderName" to "$courtName - $selectedCourt",
                "price" to price,
                "type" to type,
                "isPay" to false,
                "date" to timestamp,
                "providerId" to providerId,
                "userId" to userId
            )

            val firestore = FirebaseFirestore.getInstance()

            // Simpan ke orders milik provider
            firestore.collection("providers")
                .document(providerId)
                .collection("orders")
                .add(orderData)

            // Simpan ke orders milik user
            firestore.collection("users")
                .document(userId)
                .collection("orders")
                .add(orderData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menambahkan ke keranjang: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun startCountdown() {
        object : CountDownTimer(bookingDurationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.countdownTimer.text = getString(R.string.timer, minutes, seconds)
            }

            override fun onFinish() {
                binding.countdownText.text = "Waktu transaksi habis"
                binding.addToCartButton.isEnabled = false
            }
        }.start()
    }
}
