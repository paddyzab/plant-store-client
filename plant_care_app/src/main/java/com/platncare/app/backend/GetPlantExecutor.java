package com.platncare.app.backend;

import model.Plant;

public interface GetPlantExecutor {

    public void onSuccess(Plant plant);

    public void onFailure(Exception e);
}
