package com.example.collegefixit.Fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.collegefixit.Auth.LoginActivity
import com.example.collegefixit.R
import com.example.collegefixit.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
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
    ): View {
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
        } ?: run {
            Log.e("FragmentProfile", "No current user found")
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

            loadProfileImage(user)
        } ?: run {
            Log.e("FragmentProfile", "User email is null")
        }
    }

    private fun loadProfileImage(user: FirebaseUser) {
        val photoUrl = user.photoUrl
        Log.d("FragmentProfile", "Attempting to load profile image. PhotoUrl: $photoUrl")

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.defaultprofile)
                .error(R.drawable.defaultprofile)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Log.e("FragmentProfile", "Failed to load image", e)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.d("FragmentProfile", "Image loaded successfully")
                        return false
                    }
                })
                .into(binding.profileImageView)
        } else {
            Log.d("FragmentProfile", "PhotoUrl is null, setting default image")
            binding.profileImageView.setImageResource(R.drawable.defaultprofile)
        }
    }

    private fun extractRollNumber(email: String): String {
        // Assuming the roll number is always the first 10 characters of the email
        return email.substring(0, 10).uppercase(Locale.getDefault())
    }

    private fun calculateCurrentYear(rollNo: String): String {
        val admissionYear = rollNo.substring(3, 7).toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val acadYear = currentYear - admissionYear + 1
        return acadYear.toString() + getSuffix(acadYear)
    }

    private fun getSuffix(year: Int): String {
        if (year in 11..13) {
            return "th"
        }
        return when (year % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
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
