package com.example.tenantease

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class BillingActivity : AppCompatActivity() {

    private lateinit var tvDragDropHint: TextView
    private lateinit var ivProofImage: ImageView
    private lateinit var btnSubmit: Button
    private lateinit var billingDatabaseHelper: BillingDatabaseHelper
    private var proofOfPaymentUri: Uri? = null
    private var proofOfPaymentBitmap: Bitmap? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                proofOfPaymentUri = it
                proofOfPaymentBitmap = null
                displayProofImage(it)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                proofOfPaymentBitmap = it
                proofOfPaymentUri = null
                ivProofImage.setImageBitmap(it)
                ivProofImage.visibility = ImageView.VISIBLE
                tvDragDropHint.visibility = TextView.GONE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        tvDragDropHint = findViewById(R.id.tvDragDropHint)
        ivProofImage = findViewById(R.id.ivProofOfPaymentImage)
        btnSubmit = findViewById(R.id.btnSubmit)
        billingDatabaseHelper = BillingDatabaseHelper(this)

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

        tvDragDropHint.setOnClickListener { openAttachmentOptions() }

        btnSubmit.setOnClickListener {
            if (proofOfPaymentUri != null || proofOfPaymentBitmap != null) {
                saveImageToDatabase()
            } else {
                Toast.makeText(this, "Please attach proof of payment!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openAttachmentOptions() {
        val options = arrayOf("Select from Gallery", "Capture from Camera")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Attach Proof of Payment")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> imagePickerLauncher.launch("image/*")
                1 -> cameraLauncher.launch(null)
            }
        }
        builder.show()
    }

    private fun displayProofImage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            ivProofImage.setImageBitmap(bitmap)
            ivProofImage.visibility = ImageView.VISIBLE
            tvDragDropHint.visibility = TextView.GONE
            proofOfPaymentBitmap = bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToDatabase() {
        proofOfPaymentBitmap?.let { bitmap ->
            lifecycleScope.launch {
                val success = withContext(Dispatchers.IO) {
                    billingDatabaseHelper.insertPaymentImage(bitmap)
                }
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this@BillingActivity, "Payment proof saved!", Toast.LENGTH_SHORT).show()
                        resetProofImage()
                    } else {
                        Toast.makeText(this@BillingActivity, "Failed to save image!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun resetProofImage() {
        proofOfPaymentUri = null
        proofOfPaymentBitmap = null
        ivProofImage.setImageDrawable(null)
        ivProofImage.visibility = ImageView.GONE
        tvDragDropHint.visibility = TextView.VISIBLE
    }
}
