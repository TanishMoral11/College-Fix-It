package com.example.collegefixit.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.collegefixit.Auth.LoginActivity
import com.example.collegefixit.R
import com.example.collegefixit.databinding.FragmentProfileBinding
import com.example.collegefixit.guardactivities.RoleSelectionActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class FragmentProfile : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            updateUIWithUserInfo(user)
        }

        binding.logoutButton.setOnClickListener {
            signOut()
        }
    }

    private fun updateUIWithUserInfo(user: FirebaseUser) {
        val email = user.email
        email?.let { mail ->
            val rollNo = extractRollNumber(mail)
            val currentYear = calculateCurrentYear(rollNo)
            binding.RollNo.text = rollNo
            binding.year.text = "$currentYear Year"
            binding.batchTextView.text = calculateBatch(rollNo)
            binding.nameTextView.text = user.displayName ?: "User"

            // Check if the user signed in with Google
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            if (account != null) {
                binding.profileImageView.setImageURI(account.photoUrl)
            } else {
                // Set a default profile image if not signed in with Google
                binding.profileImageView.setImageResource(R.drawable.defaultprofile)
            }
        }
    }

    private fun extractRollNumber(email: String): String {
        // Assuming the roll number is always the first 10 characters of the email
        return email.substring(0, 10).uppercase(Locale.getDefault())
    }

    private fun calculateCurrentYear(rollNo: String): Int {
        val admissionYear = rollNo.substring(3, 7).toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - admissionYear + 1
    }

    private fun calculateBatch(rollNo: String): String {
        return rollNo.substring(3, 7)
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            navigateToLoginActivity()
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}