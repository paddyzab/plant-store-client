package com.platncare.app.backend;

import model.Plant;
import java.util.ArrayList;

public interface GetPlantsListExecutor {

    public void onSuccess(ArrayList<Plant> plants);

    public void onFailure(Exception e);
}
