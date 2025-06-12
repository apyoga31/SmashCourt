package com.agung.smashcourt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agung.smashcourt.databinding.ActivityProductKokBinding

class ProductKokActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductKokBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductKokBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
