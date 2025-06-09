package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class BookingActivity : AppCompatActivity() {

    private lateinit var chipGroup: ChipGroup
    private lateinit var rvDates: RecyclerView
    private lateinit var btnPesan: Button

    private var selectedDate: String = "Juni 2025" // ✅ default jika user belum memilih tanggal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        chipGroup = findViewById(R.id.chipGroupWaktu)
        rvDates = findViewById(R.id.rvDates)
        btnPesan = findViewById(R.id.btnPesan)

        val tanggalList = listOf(
            "09 Juni" to "SEN",
            "10 Juni" to "SEL",
            "11 Juni" to "RAB",
            "12 Juni" to "KAM",
            "13 Juni" to "JUM",
            "14 Juni" to "SAB",
            "15 Juni" to "MIN",
        )

        rvDates.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDates.adapter = DateAdapter(tanggalList) { selected ->
            selectedDate = "$selected 2025"
            Toast.makeText(this, "Tanggal terpilih: $selectedDate", Toast.LENGTH_SHORT).show()
        }

        val timeSlots = listOf(
            "06:00-07:00", "08:00-09:00", "10:00-11:00",
            "13:00-14:00", "15:00-16:00", "17:00-18:00",
            "19:00-20:00", "21:00-22:00"
        )

        populateChipGroup(timeSlots)

        btnPesan.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId
            if (selectedChipId != -1 && selectedDate != null) {
                val selectedChip = findViewById<Chip>(selectedChipId)
                val intent = Intent(this, CheckOutActivity::class.java)
                intent.putExtra("SELECTED_TIME", selectedChip.text.toString())
                intent.putExtra("SELECTED_DATE", selectedDate)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Pilih tanggal & jam terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateChipGroup(timeSlots: List<String>) {
        chipGroup.removeAllViews()
        for (slot in timeSlots) {
            val chip = Chip(this).apply {
                text = slot
                isCheckable = true
                isClickable = true
            }
            chipGroup.addView(chip)
        }
    }
}
