package com.plantcare.app.daogenerator.versions;

import com.plantcare.app.daogenerator.SchemaVersion;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Version1 extends SchemaVersion {


    protected static final String PLANT_OBJECT_NAME = "PlantObject";
    protected static final String KIND_OBJECT_NAME = "KindObject";
    protected static final String KIND_TO_PLANT_NAME = "KindToPlantObject";

    @Override
    public int getVersionNumber() {
        return 1;
    }

    @Override
    public void generateAllEntities(final Schema schema) {
        generatePlantObject(schema);
        generateKindObject(schema);
        generatePlantToKindObject(schema);
    }

    private Entity generateEntity(String objectName, Schema schema) {
        final Entity entity = schema.addEntity(objectName);
        entity.setActive(new Boolean(true));
        entity.setSuperclass("com.plantcare.app.storage.greendao.model.AbstractServerEntity");
        entity.implementsInterface("java.io.Serializable");
        return entity;
    }

    protected Entity generatePlantObject(final Schema schema) {
        final Entity plantObject = generateEntity(PLANT_OBJECT_NAME, schema);
        plantObject.addLongProperty("id").primaryKey().index();
        plantObject.addStringProperty("name");
        plantObject.addStringProperty("description");
        plantObject.addLongProperty("kindId").notNull();

        return plantObject;
    }

    protected Entity generateKindObject(final Schema schema) {
        final Entity kindObject = generateEntity(KIND_OBJECT_NAME, schema);
        kindObject.addLongProperty("id").primaryKey().index();
        kindObject.addStringProperty("name");
        kindObject.addStringProperty("latin_name");
        kindObject.addStringProperty("watering_season");
        kindObject.addStringProperty("watering_rest");
        kindObject.addBooleanProperty("dry_between_watering_season");
        kindObject.addBooleanProperty("dry_between_watering_rest");
        kindObject.addStringProperty("insolation");
        kindObject.addIntProperty("season_temp_min");
        kindObject.addIntProperty("season_temp_max");
        kindObject.addIntProperty("rest_temp_min");
        kindObject.addIntProperty("rest_temp_max");
        kindObject.addStringProperty("humidity");
        kindObject.addStringProperty("comment");

        return kindObject;
    }

    protected Entity generatePlantToKindObject(final Schema schema) {
        final Entity kindToPlantObject = generateEntity(KIND_TO_PLANT_NAME, schema);
        kindToPlantObject.addLongProperty("KindObjectId");
        kindToPlantObject.addLongProperty("PlantObjectId");

        return kindToPlantObject;
    }
}
