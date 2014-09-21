package com.platncare.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = SQLiteHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "plants.db";

    public static final String TABLE_PLANTS = "plants";
    public static final String COLUMN_PLANT_ID = "_id";
    public static final String COLUMN_PLANT_NAME = "name";
    public static final String COLUMN_PLANT_DESCRIPTION = "description";
    public static final String COLUMN_PLANT_KIND_ID = "kind";

    public static final String TABLE_KINDS = "kinds";
    public static final String COLUMN_KIND_ID = "_id";
    public static final String COLUMN_KIND_NAME = "name";
    public static final String COLUMN_KIND_LATIN_NAME = "latin_name";
    public static final String COLUMN_KIND_TREATMENT_ID = "treatment";
    // TODO: add Treatment columns to Kind Table

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_PLANTS_TABLE = "create table "
            + TABLE_PLANTS + "(" + COLUMN_PLANT_ID
            + " integer primary key autoincrement, " +
            COLUMN_PLANT_NAME + " text not null, " +
            COLUMN_PLANT_DESCRIPTION + " text not null, " +
            COLUMN_PLANT_KIND_ID + " integer not null);";

    private static final String CREATE_KINDS_TABLE = "create table "
            + TABLE_KINDS + "(" + COLUMN_KIND_ID
            + " integer primary key, " +
            COLUMN_KIND_NAME + " text not null, " +
            COLUMN_KIND_LATIN_NAME + " text not null, " +
            COLUMN_KIND_TREATMENT_ID + " integer not null);";



    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_PLANTS_TABLE);
        database.execSQL(CREATE_KINDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version: " + oldVersion + " to "
                + newVersion + " which will destroy all the data!");
        database.execSQL("DROP TABLE IF EXISTS " + CREATE_PLANTS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + CREATE_KINDS_TABLE);
        onCreate(database);
    }
}
