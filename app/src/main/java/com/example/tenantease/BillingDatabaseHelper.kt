package com.example.tenantease

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class BillingDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TenantEaseBill.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "payments"
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_IMAGE BLOB)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPaymentImage(bitmap: Bitmap): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageByteArray = outputStream.toByteArray()

        values.put(COLUMN_IMAGE, imageByteArray)

        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        return result != -1L
    }

    fun getLastPaymentImage(): Bitmap? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT 1", null)

        return if (cursor.moveToFirst()) {
            val imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            cursor.close()
            BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        } else {
            cursor.close()
            null
        }
    }
}
