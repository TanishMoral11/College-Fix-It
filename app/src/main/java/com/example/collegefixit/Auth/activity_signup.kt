package com.example.collegefixit.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collegefixit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.card.MaterialCardView

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        animate(findViewById(R.id.cardView))

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val signupButton: Button = findViewById(R.id.signUpButton)
        val tvAlreadyHaveAccount: TextView = findViewById(R.id.tvalreadyHaveAccount)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Check for valid college email format
            if (!email.endsWith("@iiitl.ac.in")) {
                Toast.makeText(this, "Please use a valid college email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.let { user ->
                            user.sendEmailVerification()
                                .addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } ?: run {
                            Toast.makeText(this, "Failed to get user", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        tvAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun animate(card: MaterialCardView) {
        val animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)
        card.startAnimation(animation)
    }
}
