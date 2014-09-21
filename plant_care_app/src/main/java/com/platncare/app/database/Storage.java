package com.platncare.app.database;

import android.content.Context;
import client.model.Plant;
import com.platncare.app.database.data_sources.PlantDAO;
import java.util.List;

public class Storage {
    private final PlantDAO plants;
    // createPlant
    // getPlantById

    public Storage(Context context) {
        plants = new PlantDAO(context);
    }

    public List<Plant> getAllPlants() {
        return plants.getAllPlants();
    }

    public Plant getPlantById(String id) {
        return plants.getPlantById(id);
    }
}
