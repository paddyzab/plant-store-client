package com.plantcare.app.daogenerator;

public class PlantCareDaoGenerator {

    private static final String SCHEMA_OUTPUT_DIR = "application/src/main/java";

    public static void main(String[] args) throws Exception {
        VersionList versions = new VersionList();

        versions.addAllSchemasInPackages("com.plantcare.app.daogenerator.versions");
        versions.validate();

        versions.generateHistoricalEntities(SCHEMA_OUTPUT_DIR);
        versions.generateWorkingCopyEntities(SCHEMA_OUTPUT_DIR);
    }
}
