package com.example.collegefixit.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collegefixit.MainActivity
import com.example.collegefixit.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        if(auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
       animate(findViewById<MaterialCardView>(R.id.cardView))
        // Access the TextInputLayouts and their contained TextInputEditTexts
        val emailInputLayout: TextInputLayout = findViewById(R.id.emailInputLayout)
        val emailEditText: TextInputEditText = emailInputLayout.editText as TextInputEditText

        val passwordInputLayout: TextInputLayout = findViewById(R.id.passwordInputLayout)
        val passwordEditText: TextInputEditText = passwordInputLayout.editText as TextInputEditText

        val loginButton: Button = findViewById(R.id.loginButton)
        val createAccountTextView: TextView = findViewById(R.id.dontHaveAccountText)

        // Set up the login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sign in with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Navigate to home screen
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()  // Finish the login activity
                    } else {
                        Toast.makeText(this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Set up the create account click listener
        createAccountTextView.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
    private fun animate(card:MaterialCardView)
    {
        val animation=android.view.animation.AnimationUtils.loadAnimation(this,R.anim.bottom_to_top)
        card.startAnimation(animation)
    }
}
