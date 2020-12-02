package com.application.ravenrankboost.Databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOrderHelper(var context: Context) :
    SQLiteOpenHelper(context, database_name, null, 1) {
    companion object {
        const val database_name = "RAVENORDER.db"
        const val table_name = "RAVENORDER"
        const val col1 = "ID"
        const val col2 = "EMAIL"
        const val col3 = "USERNAME"
        const val col4 = "PASSWORD"
        const val col5 = "ORDEREDRANK"
        const val col6 = "CURRENTRANK"
        const val col7 = "WANTEDRANK"
        const val col8 = "SUBTOTAL"
        const val col9 = "STATUS"
        const val col10 = "STATUSGAME"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $table_name (ID INTEGER PRIMARY KEY AUTOINCREMENT,EMAIL TEXT,USERNAME TEXT,PASSWORD TEXT,ORDEREDRANK TEXT,CURRENTRANK TEXT,WANTEDRANK TEXT,SUBTOTAL TEXT,STATUS TEXT,STATUSGAME TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $table_name")
        onCreate(db)
    }

    fun insertdata(
        email: String?,
        username: String?,
        password: String?,
        orderedRank: String,
        currentRank: String?,
        wantedRank: String,
        subTotal: String,
        status: String,
        statusGame: String
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
        contentValues.put(col3, username)
        contentValues.put(col4, password)
        contentValues.put(col5, orderedRank)
        contentValues.put(col6, currentRank)
        contentValues.put(col7, wantedRank)
        contentValues.put(col8, subTotal)
        contentValues.put(col9, status)
        contentValues.put(col10, statusGame)
        val result = db.insert(table_name, null, contentValues)
        return result != (-1).toLong()
    }

    val allData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("select * from $table_name", null)
        }

    fun updateCurrentRank(id: String, rank: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(col6, rank)
        db.update(table_name, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun updateStatus(id: String, status: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(col9, status)
        db.update(table_name, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun updateStatusGame(id: String, statusGame: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(col10, statusGame)
        db.update(table_name, contentValues, "ID = ?", arrayOf(id))
        return true
    }

}