package com.plantcare.app.backend;

import client.model.Plant;
import java.util.ArrayList;

public interface GetPlantsListExecutor {

    public void onSuccess(ArrayList<Plant> plants);

    public void onFailure(Exception e);
}
