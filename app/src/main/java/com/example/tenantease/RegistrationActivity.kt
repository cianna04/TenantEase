package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var spinnerRole: Spinner
    private lateinit var btnNext: Button
    private lateinit var edtFirstName: EditText
    private lateinit var edtMiddleName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtUnitNo: EditText
    private lateinit var edtContactNumber: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize views
        spinnerRole = findViewById(R.id.spinnerRole)
        btnNext = findViewById(R.id.btnNext)
        edtFirstName = findViewById(R.id.edtFirstName)
        edtMiddleName = findViewById(R.id.edtMiddleName)
        edtLastName = findViewById(R.id.edtLastName)
        edtUnitNo = findViewById(R.id.edtUnitNo)
        edtContactNumber = findViewById(R.id.edtContactNumber)
        edtEmail = findViewById(R.id.edtEmail)
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)

        // Database helper instance
        userDatabaseHelper = UserDatabaseHelper(this)

        // Setup the spinner for role selection
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.roles,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        // Handle the 'Next' button click
        btnNext.setOnClickListener {
            val firstName = edtFirstName.text.toString().trim()
            val middleName = edtMiddleName.text.toString().trim()
            val lastName = edtLastName.text.toString().trim()
            val unitNo = edtUnitNo.text.toString().trim()
            val contactNumber = edtContactNumber.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val selectedRole = spinnerRole.selectedItem.toString()

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || contactNumber.isEmpty() ||
                email.isEmpty() || username.isEmpty() || password.isEmpty() ||
                selectedRole == "Select Role") {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.PHONE.matcher(contactNumber).matches()) {
                Toast.makeText(this, "Please enter a valid contact number", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                // Check if username already exists
                if (userDatabaseHelper.isUserExists(username)) {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Encrypt password before saving
                    val encryptedPassword = encryptPassword(password)

                    // Add user to the database
                    val userAdded = userDatabaseHelper.addUser(
                        firstName, middleName, lastName, unitNo, contactNumber,
                        email, username, encryptedPassword, selectedRole
                    )

                    if (userAdded) {
                        Toast.makeText(this, "User Registered successfully!", Toast.LENGTH_SHORT).show()

                        // ✅ Store the registered user's name and username in SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("loggedInUsername", username)
                        editor.putString("userName", "$firstName $lastName") // Save full name
                        editor.apply()

                        // Redirect to HomeActivity after successful registration
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Password encryption logic (SHA-256)
    private fun encryptPassword(password: String): String {
        return try {
            val messageDigest = java.security.MessageDigest.getInstance("SHA-256")
            val hashBytes = messageDigest.digest(password.toByteArray())
            val hexString = StringBuilder()
            for (byte in hashBytes) {
                hexString.append(String.format("%02x", byte))
            }
            hexString.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            password // Return original password if encryption fails
        }
    }
}
