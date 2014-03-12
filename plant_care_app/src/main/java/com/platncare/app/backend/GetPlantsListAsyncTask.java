package com.platncare.app.backend;

import android.os.AsyncTask;
import client.endpoint.PlantEndpoint;
import client.model.Plant;

import java.util.ArrayList;

public class GetPlantsListAsyncTask extends AsyncTask<String, Void, ArrayList<Plant>>  {

    private GetPlantsListExecutor executor;
    private Exception exception;

    public GetPlantsListAsyncTask(GetPlantsListExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected ArrayList<Plant> doInBackground(String... strings) {

        try {
            String token = strings[0];
            return new PlantEndpoint().list(token);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Plant> plants) {
        if(exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(plants);
        }
    }
}
