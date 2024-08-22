package com.example.collegefixit.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.collegefixit.MainActivity
import com.example.collegefixit.R

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val confirmpasswordEditText: EditText = findViewById(R.id.confirmPasswordEditText)
        val signupButton: Button = findViewById(R.id.signUpButton)
        val tv_already_have_account: TextView = findViewById(R.id.tvalreadyHaveAccount)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmpasswordEditText.text.toString()


            if(password != confirmPassword){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Navigate to home screen
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }


        }
        tv_already_have_account.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
