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

    public static final String COLUMN_WATERING_SEASON = "watering_season";
    public static final String COLUMN_WATERING_REST = "watering_rest";
    public static final String COLUMN_DRY_WATERING_SEASON = "dry_between_watering_season";
    public static final String COLUMN_DRY_WATERING_REST = "dry_between_watering_rest";
    public static final String COLUMN_INSOLATION = "insolation";
    public static final String COLUMN_SEASON_TEMP_MIN = "season_temp_min";
    public static final String COLUMN_SEASON_TEMP_MAX = "season_temp_max";
    public static final String COLUMN_REST_TEMP_MIN = "rest_temp_min";
    public static final String COLUMN_REST_TEMP_MAX = "rest_temp_max";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_COMMENT = "comment";

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
            COLUMN_KIND_LATIN_NAME + " text, " +
            COLUMN_WATERING_SEASON + " text not null, " +
            COLUMN_WATERING_REST + " text not null, " +
            COLUMN_DRY_WATERING_SEASON + " integer, " +
            COLUMN_DRY_WATERING_REST + " integer, " +
            COLUMN_INSOLATION + " text not null, " +
            COLUMN_SEASON_TEMP_MIN + " integer not null, " +
            COLUMN_SEASON_TEMP_MAX + " integer not null, " +
            COLUMN_REST_TEMP_MIN + " integer not null, " +
            COLUMN_REST_TEMP_MAX + " integer not null, " +
            COLUMN_HUMIDITY + " text not null, " +
            COLUMN_COMMENT + " text );";

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
