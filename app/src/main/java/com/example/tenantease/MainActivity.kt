package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Buttons
        val signInButton: Button = findViewById(R.id.signInButton)
        val signUpButton: Button = findViewById(R.id.signUpButton)

        // Sign In Button click listener
        signInButton.setOnClickListener {
            // Navigate to Sign In Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Sign Up Button click listener
        signUpButton.setOnClickListener {
            // Navigate to Sign Up Activity
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
