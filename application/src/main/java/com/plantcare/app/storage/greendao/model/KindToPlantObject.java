package com.plantcare.app.storage.greendao.model;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table KIND_TO_PLANT_OBJECT.
 */
public class KindToPlantObject extends com.plantcare.app.storage.greendao.model.AbstractServerEntity implements java.io.Serializable {

    private Long KindObjectId;
    private Long PlantObjectId;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient KindToPlantObjectDao myDao;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public KindToPlantObject() {
    }

    public KindToPlantObject(Long KindObjectId, Long PlantObjectId) {
        this.KindObjectId = KindObjectId;
        this.PlantObjectId = PlantObjectId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getKindToPlantObjectDao() : null;
    }

    public Long getKindObjectId() {
        return KindObjectId;
    }

    public void setKindObjectId(Long KindObjectId) {
        this.KindObjectId = KindObjectId;
    }

    public Long getPlantObjectId() {
        return PlantObjectId;
    }

    public void setPlantObjectId(Long PlantObjectId) {
        this.PlantObjectId = PlantObjectId;
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
