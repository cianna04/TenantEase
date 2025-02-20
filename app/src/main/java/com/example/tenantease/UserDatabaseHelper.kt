package com.example.tenantease

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_MIDDLE_NAME = "middle_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_UNIT_NO = "unit_no"
        const val COLUMN_CONTACT_NUMBER = "contact_number"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FIRST_NAME TEXT,
                $COLUMN_MIDDLE_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_UNIT_NO TEXT,
                $COLUMN_CONTACT_NUMBER TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_ROLE TEXT
            )
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Password hashing function (SHA-256)
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(password.toByteArray())
        val stringBuilder = StringBuilder()
        for (b in hashBytes) {
            stringBuilder.append(String.format("%02x", b))
        }
        return stringBuilder.toString()
    }

    // Add user to the database with hashed password
    fun addUser(
        firstName: String, middleName: String, lastName: String, unitNo: String,
        contactNumber: String, email: String, username: String, password: String, role: String
    ): Boolean {
        val db = writableDatabase

        val hashedPassword = hashPassword(password)

        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_MIDDLE_NAME, middleName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_UNIT_NO, unitNo)
            put(COLUMN_CONTACT_NUMBER, contactNumber)
            put(COLUMN_EMAIL, email)
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, hashedPassword)
            put(COLUMN_ROLE, role)
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        // Return true if insert was successful, false otherwise
        return result != -1L
    }

    // Check if the user exists in the database
    fun isUserExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?",
            arrayOf(username)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Validate user by checking hashed password
    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        // Hash the entered password before querying the database
        val hashedPassword = hashPassword(password)

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, hashedPassword)
        )
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    // Retrieve user by username
    fun getUserByUsername(username: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?",
            arrayOf(username)
        )

        var user: User? = null
        if (cursor != null && cursor.moveToFirst()) {
            user = User(
                cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_MIDDLE_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_NO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER)),
                cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ROLE))
            )
        }
        cursor?.close()
        db.close()

        return user
    }
}
