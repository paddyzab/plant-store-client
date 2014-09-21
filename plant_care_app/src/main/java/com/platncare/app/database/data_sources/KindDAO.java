package com.platncare.app.database.data_sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import client.model.Kind;
import com.platncare.app.database.SQLiteHelper;
import java.sql.SQLException;

public class KindDAO {

    private SQLiteDatabase database;
    private SQLiteHelper helper;
    private String[] allColumns = {SQLiteHelper.COLUMN_KIND_ID,
            SQLiteHelper.COLUMN_KIND_NAME, SQLiteHelper.COLUMN_KIND_TREATMENT_ID};

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
    public Kind createKind(Long id,
                           String name,
                           String latinName) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_KIND_ID, id);
        values.put(SQLiteHelper.COLUMN_KIND_NAME, name);
        values.put(SQLiteHelper.COLUMN_KIND_LATIN_NAME, latinName);

        Cursor cursor = database.query(SQLiteHelper.TABLE_PLANTS, allColumns, SQLiteHelper.COLUMN_KIND_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Kind newKind = cursorToKind(cursor);
        cursor.close();

        return newKind;
    }

    public Kind getKindById(Long id) {
        return null;
    }

    private Kind cursorToKind(Cursor cursor) {
        Kind kind = new Kind();
        kind.setId(cursor.getLong(0));
        kind.setName(cursor.getString(1));
        kind.setLatinName(cursor.getString(2));

        return kind;
    }
}
