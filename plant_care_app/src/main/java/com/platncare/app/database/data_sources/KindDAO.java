package com.platncare.app.database.data_sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import client.model.Kind;
import client.model.Treatment;
import com.google.common.collect.Lists;
import com.platncare.app.database.SQLiteHelper;
import java.sql.SQLException;
import java.util.List;

public class KindDAO {

    private SQLiteDatabase database;
    private final SQLiteHelper helper;
    private final String[] allColumns = {SQLiteHelper.COLUMN_KIND_ID,
            SQLiteHelper.COLUMN_KIND_NAME, SQLiteHelper.COLUMN_KIND_LATIN_NAME, SQLiteHelper.COLUMN_WATERING_SEASON,
            SQLiteHelper.COLUMN_WATERING_REST, SQLiteHelper.COLUMN_DRY_WATERING_SEASON, SQLiteHelper.COLUMN_DRY_WATERING_REST,
            SQLiteHelper.COLUMN_INSOLATION, SQLiteHelper.COLUMN_SEASON_TEMP_MIN, SQLiteHelper.COLUMN_SEASON_TEMP_MAX, SQLiteHelper.COLUMN_REST_TEMP_MIN,
            SQLiteHelper.COLUMN_REST_TEMP_MAX, SQLiteHelper.COLUMN_HUMIDITY, SQLiteHelper.COLUMN_COMMENT};

    public KindDAO(Context context) {
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }


    //TODO probably we will want to include Treatment directly in the Kind since it's not identifiable entity
    public Kind createKind(final Long id,
                           final String name,
                           final String latinName,
                           final String wateringSeason,
                           final String wateringRest,
                           final String insolation,
                           final int seasonTempMin,
                           final int seasonTempMax,
                           final int restTempMin,
                           final int restTempMax,
                           final String humidity,
                           final String comment) {

        final ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_KIND_ID, id);
        values.put(SQLiteHelper.COLUMN_KIND_NAME, name);
        values.put(SQLiteHelper.COLUMN_KIND_LATIN_NAME, latinName);
        values.put(SQLiteHelper.COLUMN_WATERING_SEASON, wateringSeason);
        values.put(SQLiteHelper.COLUMN_WATERING_REST, wateringRest);
        values.put(SQLiteHelper.COLUMN_DRY_WATERING_SEASON, "");
        values.put(SQLiteHelper.COLUMN_DRY_WATERING_REST, "");
        values.put(SQLiteHelper.COLUMN_INSOLATION, insolation);
        values.put(SQLiteHelper.COLUMN_SEASON_TEMP_MIN, seasonTempMin);
        values.put(SQLiteHelper.COLUMN_SEASON_TEMP_MAX, seasonTempMax);
        values.put(SQLiteHelper.COLUMN_REST_TEMP_MIN, restTempMin);
        values.put(SQLiteHelper.COLUMN_REST_TEMP_MAX, restTempMax);
        values.put(SQLiteHelper.COLUMN_HUMIDITY, humidity);
        values.put(SQLiteHelper.COLUMN_COMMENT, comment);


        // TODO fix the problem with the initial query. My guess is it's wrong id

        final Cursor cursor = database.query(SQLiteHelper.TABLE_KINDS, allColumns, SQLiteHelper.COLUMN_KIND_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Kind newKind = cursorToKind(cursor);
        cursor.close();

        return newKind;
    }

    public List<Kind> getAllKinds() {
        List<Kind> kinds = Lists.newArrayList();

        Cursor cursor = database.query(SQLiteHelper.TABLE_KINDS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Kind plant = cursorToKind(cursor);
            kinds.add(plant);
            cursor.moveToNext();
        }

        cursor.close();
        return kinds;
    }

    public Kind getKindById(final Long id) {
        return null;
    }

    private Kind cursorToKind(final Cursor cursor) {

        final Kind kind = new Kind();

        if (cursor != null && cursor.moveToFirst()) {
            final Treatment treatment = new Treatment();
            treatment.setWateringSeason(cursor.getString(3));
            treatment.setWateringRest(cursor.getString(4));
            treatment.setInsolation(cursor.getString(5));
            treatment.setSeasonTempMin(cursor.getInt(6));
            treatment.setSeasonTempMax(cursor.getInt(7));
            treatment.setRestTempMin(cursor.getInt(8));
            treatment.setRestTempMax(cursor.getInt(9));
            treatment.setHumidity(cursor.getString(10));

            kind.setId(cursor.getLong(0));
            kind.setName(cursor.getString(1));
            kind.setLatinName(cursor.getString(2));
            kind.setTreatment(treatment);
        }

        return kind;
    }
}
