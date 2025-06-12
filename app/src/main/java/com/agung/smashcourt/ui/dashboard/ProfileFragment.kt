package com.agung.smashcourt.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agung.smashcourt.LoginActivity
import com.agung.smashcourt.R
import com.agung.smashcourt.databinding.FragmentProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        firestore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            userId = user.uid
            binding.textEmail.text = user.email

            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    val name = document.getString("name") ?: "Pelanggan"
                    binding.textName.text = name
                }
                .addOnFailureListener {
                    binding.textName.text = "Gagal memuat nama"
                }
        }

        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.menuEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.menuLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        binding.menuTerms.setOnClickListener {
            showDialog(
                "Syarat dan Ketentuan",
                "1. Pengguna wajib memiliki akun untuk menggunakan aplikasi ini.\n\n" +
                        "2. Dilarang menggunakan fasilitas untuk kegiatan ilegal atau merugikan pihak lain.\n\n" +
                        "3. Semua transaksi bersifat final dan tidak dapat dikembalikan, kecuali pembatalan dilakukan oleh admin.\n\n" +
                        "4. Pengguna bertanggung jawab atas penggunaan fasilitas selama masa sewa.\n\n" +
                        "5. Dengan menggunakan aplikasi Smash Court, Anda dianggap telah menyetujui seluruh syarat dan ketentuan ini."
            )
        }

        binding.menuPrivacy.setOnClickListener {
            showDialog(
                "Keamanan dan Kebijakan Privasi",
                "1. Kami berkomitmen menjaga privasi dan keamanan data pribadi Anda.\n\n" +
                        "2. Informasi pribadi seperti nama, email, dan histori transaksi disimpan secara aman di Firebase.\n\n" +
                        "3. Data Anda tidak akan dibagikan kepada pihak ketiga tanpa persetujuan Anda.\n\n" +
                        "4. Aplikasi ini mengikuti standar keamanan industri untuk melindungi informasi pengguna. "
            )
        }

        return root
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_profile_dialog, null)

        val editName = dialogView.findViewById<EditText>(R.id.edit_name)
        val editEmail = dialogView.findViewById<EditText>(R.id.edit_email)
        val editPassword = dialogView.findViewById<EditText>(R.id.edit_password)

        editName.setText(binding.textName.text)
        editEmail.setText(binding.textEmail.text)
        editEmail.isEnabled = false  // 🔒 Disable agar tidak bisa diedit

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profil")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = editName.text.toString().trim()
                val newEmail = editEmail.text.toString().trim()
                val password = editPassword.text.toString()

                if (newName.isEmpty() || newEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val user = FirebaseAuth.getInstance().currentUser
                val currentEmail = user?.email ?: return@setPositiveButton

                if (newEmail == currentEmail && newName == binding.textName.text.toString()) {
                    Toast.makeText(requireContext(), "Tidak ada perubahan", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val credential = EmailAuthProvider.getCredential(currentEmail, password)

                user.reauthenticate(credential)
                    .addOnSuccessListener {
                        if (newEmail != currentEmail) {
                            user.updateEmail(newEmail)
                                .addOnSuccessListener {
                                    updateFirestoreProfile(newName, newEmail)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Gagal update email: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            updateFirestoreProfile(newName, newEmail)
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Re-auth gagal: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Keluar")
            .setMessage("Apakah Anda yakin ingin keluar dari akun?")
            .setPositiveButton("Ya") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun updateFirestoreProfile(name: String, email: String) {
        val updates = hashMapOf(
            "name" to name,
            "email" to email
        )

        firestore.collection("users").document(userId!!)
            .update(updates as Map<String, Any>)
            .addOnSuccessListener {
                binding.textName.text = name
                binding.textEmail.text = email
                Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Gagal update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
