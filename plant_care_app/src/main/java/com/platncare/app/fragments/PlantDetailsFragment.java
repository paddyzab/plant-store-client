package com.platncare.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.platncare.app.R;
import com.platncare.app.models.Plant;

public class PlantDetailsFragment extends Fragment {

    private static final String LOG_TAG = PlantDetailsFragment.class.getSimpleName();

    private TextView textViewPlant;
    private ImageView imageViewPlant;
    private Plant plant;
    private FrameLayout frameLayoutWatering;
    private FrameLayout frameLayoutSun;
    private FrameLayout frameLayoutFertiliser;
    private FrameLayout frameLayoutTemperature;


    private static final String PLANT_KEY = "plant";

    public static PlantDetailsFragment newInstance(Plant plant) {

        PlantDetailsFragment fragment = new PlantDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLANT_KEY, plant);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plant_details, container, false);

        //TODO obviously we're missing some icons here
        frameLayoutWatering = (FrameLayout) rootView.findViewById(R.id.frameLayoutWatering);
        frameLayoutSun = (FrameLayout) rootView.findViewById(R.id.frameLayoutSun);
        frameLayoutFertiliser = (FrameLayout) rootView.findViewById(R.id.frameLayoutFertiliser);
        frameLayoutTemperature = (FrameLayout) rootView.findViewById(R.id.frameLayoutTemperature);

        return rootView;
    }
}
