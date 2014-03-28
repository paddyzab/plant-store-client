package com.platncare.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.platncare.app.R;
import client.model.Plant;
import com.platncare.app.backend.models.Humidity;
import com.platncare.app.backend.models.Insolation;
import com.platncare.app.backend.models.Watering;

public class PlantDetailsFragment extends Fragment {

    private static final String LOG_TAG = PlantDetailsFragment.class.getSimpleName();

    private Plant plant;
    private ImageView imageViewWatering;
    private ImageView imageViewInsolation;
    private ImageView imageViewHumidity;
    private TextView textViewTemperatureMax;
    private TextView textViewTemperatureMin;

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
        imageViewHumidity = (ImageView) rootView.findViewById(R.id.imageViewHumidity);
        textViewTemperatureMax = (TextView) rootView.findViewById(R.id.textViewTemperatureMax);
        textViewTemperatureMin = (TextView) rootView.findViewById(R.id.textViewTemperatureMin);

        readExtras();
        populatePlantData();

        return rootView;
    }

    private void readExtras() {
        Bundle args = getArguments();
        this.plant = (Plant) args.get(PLANT_KEY);
    }

    private void populatePlantData() {
        imageViewWatering.setImageDrawable(getResources().getDrawable(R.drawable.watering_max));
        imageViewInsolation.setImageDrawable(getResources().getDrawable(R.drawable.insolation_direct));
        imageViewHumidity.setImageDrawable(getResources().getDrawable(R.drawable.humidity_max));

        //TODO: cast in a sane way
        textViewTemperatureMax.setText("" + plant.getKind().getTreatment().getSeasonTempMax());
        textViewTemperatureMin.setText("" + plant.getKind().getTreatment().getSeasonTempMin());

        setInsolationDrawable(imageViewInsolation, Insolation.fromString(plant.getKind().getTreatment().getInsolation()));
        setWateringDrawable(imageViewWatering, Watering.fromString(plant.getKind().getTreatment().getWateringSeason()));
        stHumidityDrawable(imageViewHumidity, Humidity.fromString(plant.getKind().getTreatment().getHumidity()));
    }

    //TODO: Put this in models, or in localmodels - this is duplicated in the Adapter too.
    private void setInsolationDrawable(ImageView imageView, Insolation insolation) {
        switch (insolation) {
            case FULL:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.insolation_direct));
                break;

            case INDIRECT:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.insolation_partly_couded));
                break;

            case PARTIALLY_SHADED:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.insolation_shadowed));
                break;
        }
    }

    private void stHumidityDrawable(ImageView imageView, Humidity humidity) {
        switch (humidity) {
            case LOW:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.humidity_min));
                break;

            case MODERATE:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.humidity_mid));
                break;

            case HGH:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.humidity_max));
                break;
        }
    }

    private void setWateringDrawable(ImageView imageView, Watering wateringSeason) {
        switch (wateringSeason) {
            case FUGAL:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.watering_low));
                break;
            case MODERATE:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.watering_mid));
                break;
            case PLENTIFUL:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.watering_max));
                break;
        }
    }
}
