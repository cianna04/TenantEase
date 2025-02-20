package com.example.tenantease

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class MaintenanceDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TenantEaseMainte.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "maintenance_requests"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SUBJECT = "subject"
        private const val COLUMN_DETAILS = "details"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_SUBJECT TEXT NOT NULL, 
                $COLUMN_DETAILS TEXT NOT NULL, 
                $COLUMN_IMAGE BLOB
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertMaintenanceRequest(subject: String, details: String, bitmap: Bitmap?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_SUBJECT, subject)
        values.put(COLUMN_DETAILS, details)

        bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            values.put(COLUMN_IMAGE, outputStream.toByteArray())
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        return result != -1L
    }

    fun getAllMaintenanceRequests(): List<MaintenanceRequest> {
        val db = this.readableDatabase
        val requestList = mutableListOf<MaintenanceRequest>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT))
            val details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS))
            val imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            val image = imageByteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

            requestList.add(MaintenanceRequest(id, subject, details, image))
        }
        cursor.close()
        return requestList
    }
}

data class MaintenanceRequest(val id: Int, val subject: String, val details: String, val image: Bitmap?)
