package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRememberMe: CheckBox
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        cbRememberMe = findViewById(R.id.cbRememberMe)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnSignIn = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignup)

        // Initialize the database helper
        userDatabaseHelper = UserDatabaseHelper(this)

        // Sign In button click listener
        btnSignIn.setOnClickListener {
            val username = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Check if user exists
                if (!userDatabaseHelper.isUserExists(username)) {
                    Toast.makeText(this, "User does not exist. Please register first.", Toast.LENGTH_SHORT).show()
                } else {
                    // Hash the entered password before checking it
                    val hashedPassword = hashPassword(password)

                    // Validate password
                    if (userDatabaseHelper.validateUser(username, hashedPassword)) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        // ✅ Save logged-in user in SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()

                        // Retrieve the user details (e.g., first name) from the database
                        val user = userDatabaseHelper.getUserByUsername(username)

                        // Save the user's name (or full name)
                        editor.putString("loggedInUsername", username)
                        editor.putString("userName", user?.firstName) // Save the first name or full name
                        editor.apply()

                        // Redirect to HomeActivity after successful login
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Close LoginActivity
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Forgot Password click listener
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Sign Up button click listener
        btnSignUp.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    // Password hashing function (SHA-256)
    private fun hashPassword(password: String): String {
        return try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val hashBytes = messageDigest.digest(password.toByteArray())
            val hexString = StringBuilder()
            for (byte in hashBytes) {
                hexString.append(String.format("%02x", byte))
            }
            hexString.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            password // Return original password if hashing fails
        }
    }
}
