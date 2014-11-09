
package com.android.daob.database;

import com.android.daob.utils.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;

    private static String DATABASE_NAME = "DAOB";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + Constants.HOSPITAL_TABLE + " (" + Constants.ID
                + " integer primary key, " + Constants.NAME + " text, " + Constants.ADDRESS + " text)");
        db.execSQL("Create table " + Constants.SPECIALTY_TABLE + " (" + Constants.ID
                + " integer primary key, " + Constants.NAME + " text)");
        db.execSQL("Create table " + Constants.DOCTOR_TABLE + " (" + Constants.ID
                + " integer primary key, " + Constants.NAME + " text)"
                + " (" + Constants.DESCRIPTION + " text"
                + " (" + Constants.EDUCATION + " text"
                + " (" + Constants.WORKING_PLACE + " interger");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.HOSPITAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.SPECIALTY_TABLE);
        onCreate(db);
    }
}
