package com.plantcare.app.storage.greendao.model.v1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table KIND_TO_PLANT_OBJECT.
 */
public class KindToPlantObjectDao extends AbstractDao<KindToPlantObject, Void> {

    public static final String TABLENAME = "KIND_TO_PLANT_OBJECT";

    /**
     * Properties of entity KindToPlantObject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property KindObjectId = new Property(0, Long.class, "KindObjectId", false, "KIND_OBJECT_ID");
        public final static Property PlantObjectId = new Property(1, Long.class, "PlantObjectId", false, "PLANT_OBJECT_ID");
    }

    ;

    private DaoSession daoSession;


    public KindToPlantObjectDao(DaoConfig config) {
        super(config);
    }

    public KindToPlantObjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'KIND_TO_PLANT_OBJECT' (" + //
                "'KIND_OBJECT_ID' INTEGER," + // 0: KindObjectId
                "'PLANT_OBJECT_ID' INTEGER);"); // 1: PlantObjectId
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'KIND_TO_PLANT_OBJECT'";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, KindToPlantObject entity) {
        stmt.clearBindings();

        Long KindObjectId = entity.getKindObjectId();
        if (KindObjectId != null) {
            stmt.bindLong(1, KindObjectId);
        }

        Long PlantObjectId = entity.getPlantObjectId();
        if (PlantObjectId != null) {
            stmt.bindLong(2, PlantObjectId);
        }
    }

    @Override
    protected void attachEntity(KindToPlantObject entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }

    /**
     * @inheritdoc
     */
    @Override
    public KindToPlantObject readEntity(Cursor cursor, int offset) {
        KindToPlantObject entity = new KindToPlantObject( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // KindObjectId
                cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1) // PlantObjectId
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, KindToPlantObject entity, int offset) {
        entity.setKindObjectId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlantObjectId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Void updateKeyAfterInsert(KindToPlantObject entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Void getKey(KindToPlantObject entity) {
        return null;
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}