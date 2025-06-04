package com.agung.smashcourt

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class CheckOutActivity : AppCompatActivity() {

    private lateinit var countdownText: TextView
    private lateinit var addToCartButton: Button

    private val bookingDurationMillis = 15 * 60 * 1000L // 15 menit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_out)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        countdownText = findViewById(R.id.countdownText)
        addToCartButton = findViewById(R.id.addToCartButton)

        // ✅ Ambil data dari intent (dari BookingActivity)
        val selectedTime = intent.getStringExtra("SELECTED_TIME") ?: "-"
        val selectedDate = intent.getStringExtra("SELECTED_DATE") ?: "-"

        // ✅ Tampilkan detail transaksi
        val transactionDetail = findViewById<TextView>(R.id.transactionDetail)
        transactionDetail.text = "Lapangan 1\n$selectedDate,\n$selectedTime"

        startCountdown()

        addToCartButton.setOnClickListener {
            // Logika untuk menambah ke keranjang
        }
    }

    private fun startCountdown() {
        object : CountDownTimer(bookingDurationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                countdownText.text = String.format("Lakukan transaksi dalam %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                countdownText.text = "Waktu transaksi habis"
                addToCartButton.isEnabled = false
            }
        }.start()
    }
}
