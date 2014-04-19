package com.platncare.app.backend;

import client.model.Plant;

public interface GetPlantExecutor {

    public void onSuccess(Plant plant);

    public void onFailure(Exception e);
}
