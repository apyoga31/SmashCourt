package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.smashcourt.databinding.ActivityBookingBinding
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var selectedDate: String = "Juni 2025"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupDateRecycler()
        setupTimeChips()
        setupButtonListener()
    }

    private fun setupDateRecycler() {
        val hariList = listOf("MIN", "SEN", "SEL", "RAB", "KAM", "JUM", "SAB")
        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        val dateFormat = SimpleDateFormat("dd MMMM", Locale("id", "ID"))

        val tanggalList = List(7) {
            val tanggal = dateFormat.format(calendar.time)
            val hari = hariList[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            tanggal to hari
        }

        binding.rvDates.apply {
            layoutManager = LinearLayoutManager(this@BookingActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = DateAdapter(tanggalList) { selected ->
                selectedDate = "$selected 2025"
                Toast.makeText(context, "Tanggal terpilih: $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTimeChips() {
        val timeSlots = (6..21).map { hour ->
            String.format("%02d:00-%02d:00", hour, hour + 1)
        }

        binding.chipGroupWaktu.apply {
            removeAllViews()
            timeSlots.forEach { slot ->
                addView(Chip(context).apply {
                    text = slot
                    isCheckable = true
                    isClickable = true
                })
            }
        }
    }

    private fun setupButtonListener() {
        binding.btnPesan.setOnClickListener {
            val selectedChipId = binding.chipGroupWaktu.checkedChipId
            val selectedCourtId = binding.rgLapangan.checkedRadioButtonId

            if (selectedChipId != -1 && selectedCourtId != -1) {
                val selectedChip = findViewById<Chip>(selectedChipId)
                val selectedCourt = findViewById<RadioButton>(selectedCourtId).text.toString()
                val courtNameFromIntent = intent.getStringExtra("court_name") ?: "-"
                val providerId = intent.getStringExtra("provider_id") ?: ""

                val intent = Intent(this, CheckOutActivity::class.java).apply {
                    putExtra("SELECTED_TIME", selectedChip.text.toString())
                    putExtra("SELECTED_DATE", selectedDate)
                    putExtra("SELECTED_COURT", selectedCourt) // Lapangan 1 / 2
                    putExtra("COURT_NAME", courtNameFromIntent)
                    putExtra("PROVIDER_ID", providerId)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Pilih lapangan, tanggal & jam terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
