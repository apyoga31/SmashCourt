package com.agung.smashcourt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.smashcourt.databinding.ActivityDataUserBinding
import com.google.firebase.firestore.FirebaseFirestore

class DataUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataUserBinding
    private val userList = mutableListOf<User>()
    private lateinit var userAdapter: UserAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter(userList)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewUsers.adapter = userAdapter

        fetchUsersFromFirestore()
    }

    private fun fetchUsersFromFirestore() {
        db.collection("users")
            .get()
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