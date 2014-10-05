package com.plantcare.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.plantcare.app.R;
import com.plantcare.app.backend.models.Humidity;
import com.plantcare.app.backend.models.Insolation;
import com.plantcare.app.backend.models.Watering;
import com.plantcare.app.storage.greendao.model.KindObject;

public class PlantDetailsFragment extends Fragment {

    private KindObject kindObject;
    private ImageView imageViewWatering;
    private ImageView imageViewInsolation;
    private ImageView imageViewHumidity;
    private TextView textViewTemperatureMax;
    private TextView textViewTemperatureMin;

    private static final String PLANT_KEY = "plant";
    private static final String KIND_KEY = "kind";

    public static PlantDetailsFragment newInstance(KindObject kind) {

        PlantDetailsFragment fragment = new PlantDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KIND_KEY, kind);

        fragment.setArguments(args);
        return fragment;
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
        this.kindObject = (KindObject) args.get(KIND_KEY);
    }

    private void populatePlantData() {
        imageViewWatering.setImageDrawable(getResources().getDrawable(R.drawable.watering_max));
        imageViewInsolation.setImageDrawable(getResources().getDrawable(R.drawable.insolation_direct));
        imageViewHumidity.setImageDrawable(getResources().getDrawable(R.drawable.humidity_max));

        textViewTemperatureMax.setText(String.valueOf(kindObject.getSeason_temp_max()));
        textViewTemperatureMin.setText(String.valueOf(kindObject.getSeason_temp_min()));

        setInsolationDrawable(imageViewInsolation, Insolation.fromString(kindObject.getInsolation()));
        setWateringDrawable(imageViewWatering, Watering.fromString(kindObject.getWatering_season()));
        stHumidityDrawable(imageViewHumidity, Humidity.fromString(kindObject.getHumidity()));
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
