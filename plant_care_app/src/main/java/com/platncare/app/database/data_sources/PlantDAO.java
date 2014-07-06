package com.platncare.app.database.data_sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import client.model.Kind;
import client.model.Plant;
import client.model.Treatment;
import com.platncare.app.database.SQLiteHelper;
import java.sql.SQLException;

public class PlantDAO {

    private SQLiteDatabase database;
    private SQLiteHelper helper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_NAME, SQLiteHelper.COLUMN_DESCRIPTION, SQLiteHelper.COLUMN_KIND};

    public PlantDAO(Context context) {
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Plant createPlant(String name,
                             String description) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_NAME, name);
        values.put(SQLiteHelper.COLUMN_DESCRIPTION, description);

        long insertId = database.insert(SQLiteHelper.TABLE_PLANTS, null, values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_PLANTS, allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Plant newPlant = cursorToPlant(cursor);
        cursor.close();

        return newPlant;
    }

    // TODO: use current Kind data, provide new DAO, and add relation.

    private Plant cursorToPlant(Cursor cursor) {
        Plant plant = new Plant();
        plant.setId(cursor.getLong(0));
        plant.setName(cursor.getString(1));
        plant.setDescription(cursor.getString(2));
        plant.setKind(testKindRemoveMe());

        return plant;
    }


    // ---------------- TEST METHODS -----------------------------
    private Kind testKindRemoveMe() {

        Treatment treatment = new Treatment();
        treatment.setHumidity("low");
        treatment.setInsolation("full");
        treatment.setWateringRest("fugal");
        treatment.setWateringSeason("fugal");
        treatment.setRestTempMax(66);
        treatment.setRestTempMin(66);

        Kind kind = new Kind();
        kind.setId(1L);
        kind.setName("Test");
        kind.setLatinName("Test");
        kind.setTreatment(treatment);

        return kind;
    }

}
