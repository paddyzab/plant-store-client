package com.platncare.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import client.endpoint.PlantEndpoint;
import client.http.exception.HTTPClientException;
import com.platncare.app.R;
import com.platncare.app.activities.PlantDetailsActivity;
import com.platncare.app.adapters.PlantAdapter;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.views.EndlessListView;
import model.Plant;

import java.io.IOException;
import java.util.ArrayList;

public class PlantsFeedFragment extends Fragment implements OnItemClickListener {

    private static final String LOG_TAG = PlantsFeedFragment.class.getSimpleName();

    private EndlessListView endlessListViewPlants;
    private ArrayList<Plant> plants = new ArrayList<Plant>();
    private PlantAdapter plantsAdapter;
    private PlantsTask plantsTask;
    private String stringToken;

    public static PlantsFeedFragment newInstance(String stringToken) {
        PlantsFeedFragment fragment = new PlantsFeedFragment();
        Bundle args = new Bundle();
        args.putString(IntentKeys.TOKEN_KEY, stringToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        readExtras();

        endlessListViewPlants = (EndlessListView) rootView.findViewById(R.id.endlessListViewPlants);
        endlessListViewPlants.setAdapter(plantsAdapter);
        endlessListViewPlants.setOnItemClickListener(this);

        plantsTask = new PlantsTask();
        plantsTask.execute((Void) null);

        return rootView;
    }

    private void readExtras() {
        Bundle args = getArguments();
        stringToken = args.getString(IntentKeys.TOKEN_KEY);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
        intent.putExtra(IntentKeys.PLANT_KEY, plantsAdapter.getItem(position));
        startActivity(intent);
    }

    public class PlantsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            requestPlants(stringToken);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            plantsTask = null;

            if(success) {
                showProgress(false);
                plantsAdapter = new PlantAdapter(getActivity(), plants);
                endlessListViewPlants.setAdapter(plantsAdapter);
            }
        }
    }

    private void showProgress(boolean show) {
        endlessListViewPlants.setLoading(show);
    }

    private void requestPlants(String token) {

        try {
            plants = new PlantEndpoint().list(token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HTTPClientException e) {
            e.printStackTrace();
        }
    }
}
