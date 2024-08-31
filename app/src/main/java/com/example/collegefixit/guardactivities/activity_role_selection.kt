package com.example.collegefixit.guardactivities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.collegefixit.MainActivity
import com.example.collegefixit.databinding.ActivityRoleSelectionBinding

class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectionBinding
    private var role = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardStudent.setOnClickListener {
            role = "student"
            saveRole(role)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.cardGuard.setOnClickListener {
            role = "guard"
            saveRole(role)
            startActivity(Intent(this, GuardMainActivity::class.java))
            finish()
        }
    }

    private fun saveRole(role: String) {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("USER_ROLE", role)
            apply()
        }
    }
}
