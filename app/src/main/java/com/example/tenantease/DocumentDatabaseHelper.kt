package com.example.tenantease

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DocumentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TenantEaseDocs.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "documents"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PATH = "path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the table for storing documents
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_PATH TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop and recreate the table if the database is upgraded
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert a document into the database
    fun insertDocument(name: String, path: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PATH, path)
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    // Retrieve all document names from the database
    fun getAllDocuments(): List<String> {
        val documents = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                documents.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return documents
    }
}
