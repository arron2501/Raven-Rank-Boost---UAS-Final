package com.application.ravenrankboost.Databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseNotifHelper(var context: Context) :
    SQLiteOpenHelper(context, database_name, null, 1) {
    companion object {
        const val database_name = "RAVENNOTIF.db"
        const val table_name = "NOTIF"
        const val col1 = "ID"
        const val col2 = "EMAIL"
        const val col3 = "DATE"
        const val col4 = "NOTIF"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $table_name (ID INTEGER PRIMARY KEY AUTOINCREMENT,EMAIL TEXT ,DATE TEXT,NOTIF TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $table_name")
        onCreate(db)
    }

    fun insertdata(
        email: String?,
        date: String?,
        notif: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val cursor = allData
        val id: Int
        id = if (cursor.count == 0) {
            0
        } else {
            cursor.moveToLast()
            cursor.getInt(0) + 1
        }
        contentValues.put(col1, id)
        contentValues.put(col2, email)
        contentValues.put(col3, date)
        contentValues.put(col4, notif)
        val result = db.insert(table_name, null, contentValues)
        return result != (-1).toLong()
    }

    val allData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("select * from $table_name", null)
        }

}