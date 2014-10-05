package com.plantcare.app.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public abstract class SchemaVersion {

    public static final String CURRENT_SCHEMA_PACKAGE = "com.plantcare.app.storage.greendao.model";

    protected Entity getEntity(String name, Schema schema) {

        for (Entity entity : schema.getEntities()) {
            if (entity.getClassName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public abstract int getVersionNumber();

    public abstract void generateAllEntities(Schema schema);

    public void generate(String schemaOutputDir, boolean useSubPackage) throws Exception {
        String packageName = CURRENT_SCHEMA_PACKAGE;
        final int currentVersionNumber = getVersionNumber();

        if (useSubPackage) {
            packageName += ".v" + currentVersionNumber;
        }

        Schema schema = new Schema(currentVersionNumber, packageName);
        schema.enableKeepSectionsByDefault();
        generateAllEntities(schema);

        if (useSubPackage) {
            makeAllEntitiesPojos(schema);
        }

        new DaoGenerator().generateAll(schema, schemaOutputDir);
    }

    private void makeAllEntitiesPojos(Schema schema) {
        for (Entity entity : schema.getEntities()) {
            entity.setSuperclass(null);
            entity.getInterfacesToImplement().clear();
        }
    }
}
