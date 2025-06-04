package com.agung.smashcourt.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.smashcourt.BookingActivity
import com.agung.smashcourt.Court
import com.agung.smashcourt.CourtAdapter
import com.agung.smashcourt.courts
import com.agung.smashcourt.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = CourtAdapter(courts) { selectedCourt ->
            val intent = Intent(requireContext(), BookingActivity::class.java)
            intent.putExtra("court_name", selectedCourt.name)
            startActivity(intent)
        }

        binding.rvCourt.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourt.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
