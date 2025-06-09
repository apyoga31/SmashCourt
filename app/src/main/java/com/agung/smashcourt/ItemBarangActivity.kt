package com.agung.smashcourt

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ItemBarangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_barang)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔗 Navigasi ke detail raket
        findViewById<LinearLayout>(R.id.itemRaket).setOnClickListener {
            startActivity(Intent(this, DetailProdukActivity::class.java))
        }

        // 🔗 Navigasi ke detail shuttlecock
        findViewById<LinearLayout>(R.id.itemShuttlecock).setOnClickListener {
            startActivity(Intent(this, ProductKokActivity::class.java))
        }
    }
}