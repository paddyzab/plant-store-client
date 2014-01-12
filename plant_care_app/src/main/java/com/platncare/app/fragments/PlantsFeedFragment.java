package com.platncare.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.platncare.app.views.EndlessListView;
import model.Plant;
import model.Token;

import java.io.IOException;
import java.util.ArrayList;

public class PlantsFeedFragment extends Fragment implements OnItemClickListener {

    private static final String LOG_TAG = PlantsFeedFragment.class.getSimpleName();

    private EndlessListView endlessListViewPlants;
    private ArrayList<Plant> plants = new ArrayList<Plant>();
    private PlantAdapter plantsAdapter;
    private PlantsTask plantsTask;
    private static final String TOKEN_KEY = "token";
    private static final String PLANT_KEY = "plant";
    private Token token;

    public static PlantsFeedFragment newInstance(Token token) {
        PlantsFeedFragment fragment = new PlantsFeedFragment();
        Bundle args = new Bundle();
        args.putSerializable(TOKEN_KEY, token);

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
        token = (Token) args.getSerializable(TOKEN_KEY);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
        intent.putExtra(PLANT_KEY, plantsAdapter.getItem(position));
        startActivity(intent);
    }

    public class PlantsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            requestPlants(token);
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

    private void requestPlants(Token token) {

        try {
            plants = new PlantEndpoint().list(token.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HTTPClientException e) {
            e.printStackTrace();
        }
    }
}
