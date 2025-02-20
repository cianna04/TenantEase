package com.example.tenantease

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream

class MaintenanceActivity : AppCompatActivity() {

    private lateinit var subjectEditText: EditText
    private lateinit var detailsEditText: EditText
    private lateinit var attachFileButton: Button
    private lateinit var sendButton: Button
    private lateinit var dbHelper: MaintenanceDatabaseHelper
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        // Initialize views
        subjectEditText = findViewById(R.id.edittext_subject)
        detailsEditText = findViewById(R.id.edittext_details)
        attachFileButton = findViewById(R.id.button_attach_file)
        sendButton = findViewById(R.id.button_send)

        // Initialize Database Helper
        dbHelper = MaintenanceDatabaseHelper(this)

        // Set up BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_announcement -> {
                    startActivity(Intent(this, AnnouncementActivity::class.java))
                    true
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set up file picker
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                Toast.makeText(this, "Image attached successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        attachFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        sendButton.setOnClickListener {
            val subject = subjectEditText.text.toString().trim()
            val details = detailsEditText.text.toString().trim()

            if (subject.isEmpty() || details.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert URI to Bitmap
            val bitmap = selectedImageUri?.let { uri ->
                contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            }

            // Save to database
            val success = dbHelper.insertMaintenanceRequest(subject, details, bitmap)

            if (success) {
                Toast.makeText(this, "Maintenance request submitted!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to submit request.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
