package com.platncare.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.platncare.app.R;
import client.model.Plant;
import java.awt.Image;

public class PlantDetailsFragment extends Fragment {

    private static final String LOG_TAG = PlantDetailsFragment.class.getSimpleName();

    private Plant plant;
    ImageView imageViewWatering;
    ImageView imageViewInsolation;
    ImageView imageViewFertiliser;
    ImageView imageViewTemperature;

    private static final String PLANT_KEY = "plant";

    public static PlantDetailsFragment newInstance(Plant plant) {

        PlantDetailsFragment fragment = new PlantDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLANT_KEY, plant);

        fragment.setArguments(args);
        return fragment;
    }

    private PlantDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plant_details, container, false);

        imageViewWatering = (ImageView) rootView.findViewById(R.id.imageViewWatering);
        imageViewInsolation = (ImageView) rootView.findViewById(R.id.imageViewInsolation);
        imageViewFertiliser = (ImageView) rootView.findViewById(R.id.imageViewFertilizer);
        imageViewTemperature = (ImageView) rootView.findViewById(R.id.imageViewTemperature);

        readExtras();
        populatePlantData();

        return rootView;
    }

    private void readExtras() {
        Bundle args = getArguments();
        Plant plant = (Plant) args.get(PLANT_KEY);
        this.plant = plant;
    }

    private void populatePlantData() {
        imageViewWatering.setImageDrawable(getResources().getDrawable(R.drawable.watering_max));
        imageViewInsolation.setImageDrawable(getResources().getDrawable(R.drawable.insolation_direct));
        imageViewFertiliser.setImageDrawable(getResources().getDrawable(R.drawable.humidity_max));
    }
}
