package com.plantcare.app.backend;

import android.content.Context;
import android.os.AsyncTask;
import client.endpoint.PlantEndpoint;
import client.model.Plant;
import com.plantcare.app.utils.Preferences;


public class GetPlantAsyncTask extends AsyncTask<Object, Void, Plant> {

    private GetPlantExecutor executor;
    private Exception exception;

    public GetPlantAsyncTask(GetPlantExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected Plant doInBackground(Object... params) {
        try {
            Context context = (Context) params[0];
            Long plantId = (Long) params[1];
            return new PlantEndpoint().read(Preferences.getAppToken(context), plantId);

        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Plant plant) {
        if (exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(plant);
        }
    }
}
