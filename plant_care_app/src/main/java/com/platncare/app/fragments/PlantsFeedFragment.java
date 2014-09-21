package com.platncare.app.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import client.model.Plant;
import com.platncare.app.R;
import com.platncare.app.activities.PlantDetailsActivity;
import com.platncare.app.adapters.PlantAdapter;
import com.platncare.app.backend.GetPlantsListAsyncTask;
import com.platncare.app.backend.GetPlantsListExecutor;
import com.platncare.app.database.data_sources.PlantDAO;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.views.EndlessGridView;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlantsFeedFragment extends Fragment implements OnItemClickListener {

    private final static String TAG = PlantsFeedFragment.class.getSimpleName();

    private EndlessGridView endlessGridViewPlants;
    private PlantAdapter plantsAdapter;
    private String stringToken;
    private Context context;

    private PlantDAO plantStorage;

    //TODO move this item to the EndlessGridView
    private ProgressBar progressBarLoading;

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

        endlessGridViewPlants = (EndlessGridView) rootView.findViewById(R.id.endlessListViewPlants);
        endlessGridViewPlants.setOnItemClickListener(this);

        progressBarLoading = (ProgressBar) rootView.findViewById(R.id.progressBarLoading);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (plantsAdapter != null) {
            if (plantsAdapter.isEmpty()) {
                endlessGridViewPlants.setAdapter(plantsAdapter);
                requestPlantsArray();
            } else {
                endlessGridViewPlants.setAdapter(plantsAdapter);
            }
        } else {
            requestPlantsArray();
            endlessGridViewPlants.setAdapter(plantsAdapter);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        plantStorage = new PlantDAO(context);

        setRetainInstance(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
        intent.putExtra(IntentKeys.PLANT_KEY, plantsAdapter.getItem(position));
        startActivity(intent);
    }


    public void requestPlantsArray() {
        showProgress(true);
        new GetPlantsListAsyncTask(executor).execute(stringToken);
    }

    private GetPlantsListExecutor executor = new GetPlantsListExecutor() {

        @Override
        public void onSuccess(ArrayList<Plant> plants) {
            showProgress(false);

            try {
                plantStorage.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (Plant plant : plants) {
                plantStorage.createPlant(plant.getName(),
                        plant.getDescription(),
                        plant.getKind().getId());
            }

            plantsAdapter = new PlantAdapter(context, plantStorage.getAllPlants());
            endlessGridViewPlants.setAdapter(plantsAdapter);
            progressBarLoading.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Exception e) {
            showProgress(false);
            Toast.makeText(getActivity(), "Something went wrong.",
                    Toast.LENGTH_LONG).show();

            Log.e(TAG, String.format("Error on failure, message /n %s", e.getMessage()));
            Log.e(TAG, String.format("Error on failure, stacktrace /n %s", e.getStackTrace()));
        }
    };

    private void readExtras() {
        Bundle args = getArguments();
        stringToken = args.getString(IntentKeys.TOKEN_KEY);
    }

    private void showProgress(boolean show) {
        clearAdapter();
        endlessGridViewPlants.setLoading(show);
        progressBarLoading.setVisibility(View.VISIBLE);
    }

    private void clearAdapter() {
        if (plantsAdapter != null) {
            plantsAdapter.clear();
        }
    }
}
