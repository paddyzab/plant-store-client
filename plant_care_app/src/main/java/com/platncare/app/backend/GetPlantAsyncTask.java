package com.platncare.app.backend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import client.endpoint.PlantEndpoint;
import client.http.exception.HTTPClientException;
import com.platncare.app.utils.Preferences;
import model.Plant;

import java.io.IOException;

public class GetPlantAsyncTask extends AsyncTask<Object, Void, Plant> {

    private final static String LOG_TAG = GetPlantAsyncTask.class.getSimpleName();

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

        } catch (IOException e) {
            this.exception = e;
            return null;
        } catch (HTTPClientException e) {
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
