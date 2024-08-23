package com.example.collegefixit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieListener
import com.example.collegefixit.Fragments.ComplaintsListFragment
import com.example.collegefixit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    // Handle profile navigation (not implemented in this example)
                    true
                }
                else -> false
            }
        }
    }
}
