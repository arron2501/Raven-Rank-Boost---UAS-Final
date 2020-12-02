package com.application.ravenrankboost.Databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseMatchHelper(var context: Context) :
    SQLiteOpenHelper(context, database_name, null, 1) {
    companion object {
        const val database_name = "RAVENMATCH.db"
        const val table_name = "MATCHS"
        const val col1 = "ID"
        const val col2 = "ORDERID"
        const val col3 = "RESULT"
        const val col4 = "SCORE"
        const val col5 = "KDA"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $table_name (ID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INTEGER,RESULT BOOLEAN,SCORE TEXT,KDA TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $table_name")
        onCreate(db)
    }

    fun insertdata(
        orderId: Int?,
        result: Boolean?,
        score: String?,
        kda: String
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
        contentValues.put(col2, orderId)
        contentValues.put(col3, result)
        contentValues.put(col4, score)
        contentValues.put(col5, kda)
        val result = db.insert(table_name, null, contentValues)
        return result != (-1).toLong()
    }

    val allData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("select * from $table_name", null)
        }

}