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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var chipGroup: ChipGroup
    private lateinit var rvDates: RecyclerView
    private lateinit var btnPesan: Button

    private var selectedDate: String = "Juni 2025"

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

        // Generate tanggal 7 hari ke depan, dimulai dari besok
        val hariList = listOf("MIN", "SEN", "SEL", "RAB", "KAM", "JUM", "SAB")
        val tanggalList = mutableListOf<Pair<String, String>>()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1) // mulai dari besok
        val dateFormat = SimpleDateFormat("dd MMMM", Locale("id", "ID"))
        for (i in 0 until 7) {
            val tanggal = dateFormat.format(calendar.time)
            val hari = hariList[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            tanggalList.add(tanggal to hari)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        rvDates.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDates.adapter = DateAdapter(tanggalList) { selected ->
            selectedDate = "$selected 2025"
            Toast.makeText(this, "Tanggal terpilih: $selectedDate", Toast.LENGTH_SHORT).show()
        }

        // Generate jam dinamis dari 06:00 sampai 22:00 dengan interval 1 jam
        val timeSlots = mutableListOf<String>()
        for (hour in 6..21) {
            val start = String.format("%02d:00", hour)
            val end = String.format("%02d:00", hour + 1)
            timeSlots.add("$start-$end")
        }

        populateChipGroup(timeSlots)

        btnPesan.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId
            if (selectedChipId != -1 && selectedDate != null) {
                val courtName = intent.getStringExtra("court_name") ?: "-"
                val selectedChip = findViewById<Chip>(selectedChipId)
                val intent = Intent(this, CheckOutActivity::class.java)
                intent.putExtra("SELECTED_TIME", selectedChip.text.toString())
                intent.putExtra("SELECTED_DATE", selectedDate)
                intent.putExtra("COURT_NAME", courtName)
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
