package com.application.ravenrankboost.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.application.ravenrankboost.R;

import java.io.ByteArrayOutputStream;


public class DatabaseLoginHelper extends SQLiteOpenHelper {

    public static final String database_name = "LOGIN.db";
    public static final String table_name = "LOGIN";
    public static final String col1 = "EMAIL";
    public static final String col2 = "USERNAME";
    public static final String col3 = "PASSWORD";
    public static final String col4 = "SINCE";
    public static final String col5 = "PROFILEIMAGE";
    Context context;

    public DatabaseLoginHelper(Context context) {
        super(context, database_name, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table_name + " (EMAIL TEXT PRIMARY KEY,USERNAME TEXT,PASSWORD TEXT,SINCE TEXT,PROFILEIMAGE BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public boolean insertdata(String email, String username, String password, String since) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1, email);
        contentValues.put(col2, username);
        contentValues.put(col3, password);
        contentValues.put(col4, since);
        byte[] bitMapData = null;
        contentValues.put(col5, bitMapData);

        long result = db.insert(table_name, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resultset = db.rawQuery("select * from " + table_name, null);
        return resultset;
    }

    public boolean updateProfileImage(String email,byte[] imageByte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col5, imageByte);
        db.update(table_name, contentValues, "EMAIL = ?", new String[]{email});
        return true;
    }

    public Integer deleteData(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name, "EMAIL = ?", new String[]{email});
    }

}