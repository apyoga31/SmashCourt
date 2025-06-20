package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityItemBarangBinding

class ItemBarangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemBarangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔙 Fungsi tombol kembali
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 🔗 Navigasi ke detail raket
        binding.itemRaket.setOnClickListener {
            startActivity(Intent(this, DetailProdukActivity::class.java))
        }

        // 🔗 Navigasi ke detail shuttlecock
//        binding.itemShuttlecock.setOnClickListener {
//            startActivity(Intent(this, ProductKokActivity::class.java))
//        }
    }
}
