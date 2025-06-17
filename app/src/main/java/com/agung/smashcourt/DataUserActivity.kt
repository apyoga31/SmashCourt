package com.agung.smashcourt

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.smashcourt.databinding.ActivityDataUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DataUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataUserBinding
    private val userList = mutableListOf<User>()
    private lateinit var userAdapter: UserAdapter
    private val db = FirebaseFirestore.getInstance()
    private val roles = listOf("Semua", "Admin", "Penyedia", "Pelanggan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        // Setup RecyclerView
        userAdapter = UserAdapter(userList)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewUsers.adapter = userAdapter

        // Setup Spinner (Filter Dropdown)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = spinnerAdapter

        // Spinner item selected listener
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRole = roles[position]
                fetchUsersFromFirestore(selectedRole)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun fetchUsersFromFirestore(role: String) {
        val query: Query = if (role != "Semua") {
            db.collection("users").whereEqualTo("role", role) // gunakan huruf besar sesuai Firestore
        } else {
            db.collection("users")
        }

        query.get()
            .addOnSuccessListener { documents ->
                userList.clear()
                for (doc in documents) {
                    val user = doc.toObject(User::class.java)
                    userList.add(user)
                }
                userAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengambil data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
