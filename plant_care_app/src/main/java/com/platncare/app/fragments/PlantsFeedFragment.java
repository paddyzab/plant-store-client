package com.platncare.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import com.platncare.app.R;
import com.platncare.app.activities.PlantDetailsActivity;
import com.platncare.app.adapters.PlantAdapter;
import com.platncare.app.models.Plant;
import com.platncare.app.views.EndlessListView;

import java.util.ArrayList;

public class PlantsFeedFragment extends Fragment implements OnItemClickListener {

    /*TODO:
    * 1. implement hasMore and loadMore Listeners after backend service will be connected
    * */

    private static final String LOG_TAG = PlantsFeedFragment.class.getSimpleName();
    private EndlessListView endlessListViewPlants;
    private ArrayList<Plant> plants;
    private PlantAdapter plantsAdapter;
    private static final String PLANTS_KEY = "plants";
    private static final String PLANT_KEY = "plant";

    public static PlantsFeedFragment newInstance(ArrayList<Plant> plants) {
        PlantsFeedFragment fragment = new PlantsFeedFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLANTS_KEY, plants);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        readExtras();

        endlessListViewPlants = (EndlessListView) rootView.findViewById(R.id.endlessListViewPlants);

        plantsAdapter = new PlantAdapter(getActivity(), plants);
        endlessListViewPlants.setAdapter(plantsAdapter);
        endlessListViewPlants.setOnItemClickListener(this);

        return rootView;
    }

    private void readExtras() {
        Bundle args = getArguments();
        plants = (ArrayList<Plant>) args.getSerializable(PLANTS_KEY);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
        intent.putExtra(PLANT_KEY, plantsAdapter.getItem(position));
        startActivity(intent);
    }
}
