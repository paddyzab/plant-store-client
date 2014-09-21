package com.platncare.app.database.data_sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import client.model.Kind;
import client.model.Plant;
import client.model.Treatment;
import com.google.common.collect.Lists;
import com.platncare.app.database.SQLiteHelper;
import java.sql.SQLException;
import java.util.List;

public class PlantDAO {

    private SQLiteDatabase database;
    private SQLiteHelper helper;
    private String[] allColumns = {SQLiteHelper.COLUMN_PLANT_ID,
            SQLiteHelper.COLUMN_PLANT_NAME, SQLiteHelper.COLUMN_PLANT_DESCRIPTION, SQLiteHelper.COLUMN_PLANT_KIND_ID};

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
                             String description,
                             Long kindId) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_PLANT_NAME, name);
        values.put(SQLiteHelper.COLUMN_PLANT_DESCRIPTION, description);
        values.put(SQLiteHelper.COLUMN_PLANT_KIND_ID, kindId);

        long insertId = database.insert(SQLiteHelper.TABLE_PLANTS, null, values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_PLANTS, allColumns, SQLiteHelper.COLUMN_PLANT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Plant newPlant = cursorToPlant(cursor);
        cursor.close();

        return newPlant;
    }

    public List<Plant> getAllPlants() {
        List<Plant> plants = Lists.newArrayList();

        Cursor cursor = database.query(SQLiteHelper.TABLE_PLANTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Plant plant = cursorToPlant(cursor);
            plants.add(plant);
            cursor.moveToNext();
        }

        cursor.close();
        return plants;
    }

    public Plant getPlantById(String id) {
        return null;
    }

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
