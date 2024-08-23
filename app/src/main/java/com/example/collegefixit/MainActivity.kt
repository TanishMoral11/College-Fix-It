package com.example.collegefixit

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieListener
import com.example.collegefixit.Auth.LoginActivity
import com.example.collegefixit.Fragments.ComplaintsListFragment
import com.example.collegefixit.Fragments.fragment_profile
import com.example.collegefixit.databinding.ActivityMainBinding
import com.example.collegefixit.utils.Constants.Companion.auth
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


        auth.addAuthStateListener { firebaseAuth ->
            if(firebaseAuth.currentUser==null){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the complaints list fragment as the default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, ComplaintsListFragment())
                commit()
            }
        }

        // Handle bottom navigation item clicks
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_complaints -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, ComplaintsListFragment())
                        commit()
                    }
                    true
                }
                R.id.nav_add_complaint -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, AddComplaintFragment())
                        addToBackStack(null) // Add to backstack to allow navigation back
                        commit()
                    }
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, fragment_profile())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                else -> false
            }
        }
    }
}
