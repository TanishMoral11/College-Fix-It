package com.example.collegefixit.guardactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collegefixit.R

class GuardLoginActivity : AppCompatActivity() {
    private lateinit var etGuardId: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guard_login)

        etGuardId = findViewById(R.id.etGuardId)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val guardId = etGuardId.text.toString()
            val password = etPassword.text.toString()

            if (guardId.isNotEmpty() && password.isNotEmpty()) {
                // TODO: Implement actual authentication logic
                if (guardId == "guard123" && password == "password") {
                    val intent = Intent(this, GuardMainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}