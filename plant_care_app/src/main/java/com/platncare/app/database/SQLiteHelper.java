package com.platncare.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = SQLiteHelper.class.getSimpleName();

    public static final String TABLE_PLANTS = "plants";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_KIND = "kind";

    private static final String DATABASE_NAME = "plants.db";
    private static final int DATABASE_VERSION = 1;

    //Create Database SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PLANTS + "(" + COLUMN_ID
            + "integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_DESCRIPTION + " text not null, " +
            COLUMN_KIND + " text not null);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgreading database from version: " + oldVersion + " to "
            + newVersion + " which will destroy all the data!");
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE);
        onCreate(database);
    }
}
