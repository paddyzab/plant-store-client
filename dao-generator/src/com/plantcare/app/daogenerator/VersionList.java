package com.plantcare.app.daogenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class VersionList extends ArrayList<SchemaVersion> {

    private SchemaVersion getLastElement() {
        SchemaVersion latest = get(0);

        for (SchemaVersion version : this) {
            if (version.getVersionNumber() > latest.getVersionNumber()) {
                latest = version;
            }
        }

        return latest;
    }

    public void addAllSchemasInPackages(String packageName) throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends SchemaVersion>> subTypes = reflections.getSubTypesOf(SchemaVersion.class);

        for (Class subClass : subTypes) {
            add((SchemaVersion) subClass.newInstance());
        }
    }

    public void generateHistoricalEntities(String outDir) throws Exception {
        for (SchemaVersion version : this) {
            version.generate(outDir, true);
        }
    }

    public void generateWorkingCopyEntities(String schemaOutputDir) throws Exception {
        getLastElement().generate(schemaOutputDir, false);
    }

    public void validate() {

        if (size() == 0) {
            throw new IllegalArgumentException("you need at least one version");
        }

        Set<Integer> versionNumbers = new HashSet<Integer>();

        for (SchemaVersion version : this) {

            int versionNumber = version.getVersionNumber();
            if (versionNumbers.contains(versionNumber)) {
                throw new IllegalArgumentException(
                        "Unable to process schema com.plantcare.app.daogenerator.versions, multiple instances with version number : "
                                + version.getVersionNumber());
            }
            versionNumbers.add(versionNumber);
        }
    }
}
