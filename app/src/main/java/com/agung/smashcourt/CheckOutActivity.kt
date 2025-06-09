package com.agung.smashcourt

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class CheckOutActivity : AppCompatActivity() {

    private lateinit var countdownText: TextView
    private lateinit var addToCartButton: Button
    private lateinit var countdownTimer: TextView

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
        countdownTimer = findViewById(R.id.countdownTimer)

        // ✅ Ambil data dari intent (dari BookingActivity)
        val selectedTime = intent.getStringExtra("SELECTED_TIME") ?: "-"
        val selectedDate = intent.getStringExtra("SELECTED_DATE") ?: "-"
        val selectedCourt = intent.getStringExtra("COURT_NAME") ?: "Not Found"

        // ✅ Tampilkan detail transaksi
        val transactionDetail = findViewById<TextView>(R.id.transactionDetail)
        transactionDetail.text = "$selectedCourt \n$selectedDate,\n$selectedTime"

        startCountdown()

        addToCartButton.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val db = FirebaseFirestore.getInstance()

                // Convert selectedDate and selectedTime to Timestamp
                val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))
                val dateTimeStr = "$selectedDate $selectedTime".replace(".", ":")
                val date = sdf.parse(dateTimeStr)

                if (date != null) {
                    val timestamp = Timestamp(date)

                    // Tentukan tipe berdasarkan nama lapangan
                    val type = if (selectedCourt.lowercase().contains("alat")) "sewa alat" else "sewa lapangan"

                    // Simulasikan harga, bisa juga ambil dari intent atau database
                    val price = 20000

                    val orderData = hashMapOf(
                        "orderName" to selectedCourt,
                        "price" to price,
                        "type" to type,
                        "isPay" to false,
                        "date" to timestamp
                    )

                    db.collection("users")
                        .document(userId)
                        .collection("orders")
                        .add(orderData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                            finish() // kembali ke layar sebelumnya
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal menambahkan ke keranjang: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Format tanggal atau jam salah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startCountdown() {
        object : CountDownTimer(bookingDurationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                countdownTimer.text = String.format(getString(R.string.timer), minutes, seconds)
            }

            override fun onFinish() {
                countdownText.text = "Waktu transaksi habis"
                addToCartButton.isEnabled = false
            }
        }.start()
    }
}

