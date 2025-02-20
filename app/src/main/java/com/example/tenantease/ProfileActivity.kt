package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize database helper
        userDatabaseHelper = UserDatabaseHelper(this)

        // User Info Section
        val ivProfilePicture = findViewById<ImageView>(R.id.ivProfilePicture)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvusername = findViewById<TextView>(R.id.USERNAME)
        val tvemail = findViewById<TextView>(R.id.EMAIL)
        val tvPhoneNumber = findViewById<TextView>(R.id.PHONE_NUMBER)

        // Information Section
        val tvInformation = findViewById<TextView>(R.id.tvInformation)

        // Preferences Section
        val tvPreferencesHeader = findViewById<TextView>(R.id.tvPreferencesHeader)
        val switchNotification = findViewById<Switch>(R.id.switchNotification)

        // Account Section
        val tvAccountHeader = findViewById<TextView>(R.id.tvAccountHeader)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Back Button Functionality
        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Optionally finish ProfileActivity to prevent going back to it
        }

        // Retrieve stored username from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val loggedInUsername = sharedPreferences.getString("loggedInUsername", null)

        if (loggedInUsername != null) {
            // Fetch user details from the database based on the username
            val user = userDatabaseHelper.getUserByUsername(loggedInUsername)

            if (user != null) {
                // Set the TextViews to display the user's info
                tvUsername.text = user.username // Display the username
                tvEmail.text = user.email // Display the email
                tvusername.text = "Username: ${user.username}"
                tvemail.text = "Email: ${user.email}"
                tvPhoneNumber.text = "Phone Number: ${user.contactNumber}"


                // Set up the user info display (First name, Last name, etc.)


                // Optionally set a profile picture, if you have one
                // ivProfilePicture.setImageResource(R.drawable.profile_picture)  // Example for setting an image
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }

        // Example of switching notification on/off
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Notification is ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification is OFF", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle logout action
        btnLogout.setOnClickListener {
            // Clear the user from SharedPreferences when logging out
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Show a toast message and navigate to LoginActivity
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Optionally finish the current activity to prevent going back to it
            finish()
        }
    }
}
