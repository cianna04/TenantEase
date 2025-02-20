package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DocumentActivity : AppCompatActivity() {

    private lateinit var documentDatabaseHelper: DocumentDatabaseHelper
    private lateinit var rvRecentlyEdited: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        // Initialize database helper
        documentDatabaseHelper = DocumentDatabaseHelper(this)

        // Setup Recently Edited RecyclerView
        rvRecentlyEdited = findViewById(R.id.rvRecentlyEdited)
        rvRecentlyEdited.layoutManager = LinearLayoutManager(this)
        rvRecentlyEdited.adapter = RecentlyEditedAdapter(getRecentlyEditedDocuments())

        // Insert a sample document (for testing purposes)
        insertSampleDocument()

        // Setup Category Clicks
        setupCategoryClicks()

        // Setup BottomNavigationView
        setupBottomNavigation()
    }

    private fun setupCategoryClicks() {
        val categories = listOf(
            "Lease Agreements",
            "Maintenance Records",
            "Inspection Reports",
            "Utility Bills",
            "Shared Property Guidelines or Rules"
        )

        // Loop through categories and attach click listeners
        val gridLayout = findViewById<GridLayout>(R.id.glDocumentCategories)
        for (i in 0 until gridLayout.childCount) {
            val categoryView = gridLayout.getChildAt(i) as LinearLayout
            categoryView.setOnClickListener {
                Toast.makeText(
                    this,
                    "No documents available in ${categories[i]}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getRecentlyEditedDocuments(): List<String> {
        // Mock data for recently edited documents
        // Retrieve document names from the database instead
        return documentDatabaseHelper.getAllDocuments()
    }

    private fun insertSampleDocument() {
        // Sample document insertion for testing
        val documentName = "Sample Lease Agreement.pdf"
        val documentPath = "/documents/sample_lease_agreement.pdf"
        val isInserted = documentDatabaseHelper.insertDocument(documentName, documentPath)

        if (isInserted) {
            Toast.makeText(this, "Document added successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to add document.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
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
    }
}
