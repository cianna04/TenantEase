package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize buttons
        val btnMaintenanceRequest = findViewById<Button>(R.id.btnMaintenanceRequest)
        val btnBillandPayments = findViewById<Button>(R.id.btnBillandPayments)
        val btnDocuments = findViewById<Button>(R.id.btnDocuments)

        // Set up button click listeners
        btnMaintenanceRequest.setOnClickListener {
            val intent = Intent(this, MaintenanceActivity::class.java)
            startActivity(intent)
        }

        btnBillandPayments.setOnClickListener {
            val intent = Intent(this, BillingActivity::class.java) // Correct target activity
            startActivity(intent)
        }

        btnDocuments.setOnClickListener {
            val intent = Intent(this, DocumentActivity::class.java) // Correct target activity
            startActivity(intent)
        }

        // Setup BottomNavigationView
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already in HomeActivity, no action needed
                    true
                }
                R.id.nav_announcement -> {
                    startActivity(Intent(this, AnnouncementActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java)) // Correct target activity
                    true
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the selected item to highlight the current activity
        bottomNavigationView.selectedItemId = R.id.nav_home
    }
}
