package com.plantcare.app.storage.greendao.model;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table KIND_OBJECT.
 */
public class KindObject extends com.plantcare.app.storage.greendao.model.AbstractServerEntity implements java.io.Serializable {

    private Long id;
    private String name;
    private String latin_name;
    private String watering_season;
    private String watering_rest;
    private Boolean dry_between_watering_season;
    private Boolean dry_between_watering_rest;
    private String insolation;
    private Integer season_temp_min;
    private Integer season_temp_max;
    private Integer rest_temp_min;
    private Integer rest_temp_max;
    private String humidity;
    private String comment;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient KindObjectDao myDao;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public KindObject() {
    }

    public KindObject(Long id) {
        this.id = id;
    }

    public KindObject(Long id, String name, String latin_name, String watering_season, String watering_rest, Boolean dry_between_watering_season, Boolean dry_between_watering_rest, String insolation, Integer season_temp_min, Integer season_temp_max, Integer rest_temp_min, Integer rest_temp_max, String humidity, String comment) {
        this.id = id;
        this.name = name;
        this.latin_name = latin_name;
        this.watering_season = watering_season;
        this.watering_rest = watering_rest;
        this.dry_between_watering_season = dry_between_watering_season;
        this.dry_between_watering_rest = dry_between_watering_rest;
        this.insolation = insolation;
        this.season_temp_min = season_temp_min;
        this.season_temp_max = season_temp_max;
        this.rest_temp_min = rest_temp_min;
        this.rest_temp_max = rest_temp_max;
        this.humidity = humidity;
        this.comment = comment;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getKindObjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatin_name() {
        return latin_name;
    }

    public void setLatin_name(String latin_name) {
        this.latin_name = latin_name;
    }

    public String getWatering_season() {
        return watering_season;
    }

    public void setWatering_season(String watering_season) {
        this.watering_season = watering_season;
    }

    public String getWatering_rest() {
        return watering_rest;
    }

    public void setWatering_rest(String watering_rest) {
        this.watering_rest = watering_rest;
    }

    public Boolean getDry_between_watering_season() {
        return dry_between_watering_season;
    }

    public void setDry_between_watering_season(Boolean dry_between_watering_season) {
        this.dry_between_watering_season = dry_between_watering_season;
    }

    public Boolean getDry_between_watering_rest() {
        return dry_between_watering_rest;
    }

    public void setDry_between_watering_rest(Boolean dry_between_watering_rest) {
        this.dry_between_watering_rest = dry_between_watering_rest;
    }

    public String getInsolation() {
        return insolation;
    }

    public void setInsolation(String insolation) {
        this.insolation = insolation;
    }

    public Integer getSeason_temp_min() {
        return season_temp_min;
    }

    public void setSeason_temp_min(Integer season_temp_min) {
        this.season_temp_min = season_temp_min;
    }

    public Integer getSeason_temp_max() {
        return season_temp_max;
    }

    public void setSeason_temp_max(Integer season_temp_max) {
        this.season_temp_max = season_temp_max;
    }

    public Integer getRest_temp_min() {
        return rest_temp_min;
    }

    public void setRest_temp_min(Integer rest_temp_min) {
        this.rest_temp_min = rest_temp_min;
    }

    public Integer getRest_temp_max() {
        return rest_temp_max;
    }

    public void setRest_temp_max(Integer rest_temp_max) {
        this.rest_temp_max = rest_temp_max;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
