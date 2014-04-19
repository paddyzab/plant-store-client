package com.platncare.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.platncare.app.R;
import client.model.Plant;

import com.platncare.app.backend.models.Humidity;
import com.platncare.app.backend.models.Insolation;
import com.platncare.app.backend.models.Watering;
import java.util.ArrayList;

public class PlantAdapter extends ArrayAdapter<Plant> {

    Context context;

    public PlantAdapter(Context context, ArrayList<Plant> plants) {
        super(context, R.layout.adapter_plant, plants);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if(rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_plant, parent, false);

            PlantHolder holder = new PlantHolder();
            holder.textViewPlantName = (TextView) rowView.findViewById(R.id.textViewPlantName);
            holder.textViewLatin = (TextView) rowView.findViewById(R.id.textViewLatin);
            holder.imageViewInsolation = (ImageView) rowView.findViewById(R.id.imageViewInsolation);
            holder.imageViewWatering = (ImageView) rowView.findViewById(R.id.imageViewWatering);
            holder.imageViewHumidity = (ImageView) rowView.findViewById(R.id.imageViewHumidity);

            rowView.setTag(holder);
        }

        PlantHolder holder = (PlantHolder) rowView.getTag();
        holder.textViewPlantName.setText(getItem(position).getName().toUpperCase());
        holder.textViewLatin.setText(getItem(position).getKind().getLatinName());

        setWateringDrawable(holder, Watering.fromString(getItem(position).getKind().getTreatment().getWateringSeason()));
        stHumidityDrawable(holder, Humidity.fromString(getItem(position).getKind().getTreatment().getHumidity()));
        setInsolationDrawable(holder, Insolation.fromString(getItem(position).getKind().getTreatment().getInsolation()));

        return rowView;
    }

    private void setInsolationDrawable(PlantHolder holder, Insolation insolation) {
        switch (insolation) {
            case FULL:
                holder.imageViewInsolation.setImageDrawable(context.getResources().getDrawable(R.drawable.insolation_direct));
                break;

            case INDIRECT:
                holder.imageViewInsolation.setImageDrawable(context.getResources().getDrawable(R.drawable.insolation_partly_couded));
                break;

            case PARTIALLY_SHADED:
                holder.imageViewInsolation.setImageDrawable(context.getResources().getDrawable(R.drawable.insolation_shadowed));
                break;
        }
    }

    private void stHumidityDrawable(PlantHolder holder, Humidity humidity) {
        switch (humidity) {
            case LOW:
                holder.imageViewHumidity.setImageDrawable(context.getResources().getDrawable(R.drawable.humidity_min));
                break;

            case MODERATE:
                holder.imageViewHumidity.setImageDrawable(context.getResources().getDrawable(R.drawable.humidity_mid));
                break;

            case HGH:
                holder.imageViewHumidity.setImageDrawable(context.getResources().getDrawable(R.drawable.humidity_max));
                break;
        }
    }

    private void setWateringDrawable(PlantHolder holder, Watering wateringSeason) {
        switch (wateringSeason) {
            case FUGAL:
                holder.imageViewWatering.setImageDrawable(context.getResources().getDrawable(R.drawable.watering_low));
                break;
            case MODERATE:
                holder.imageViewWatering.setImageDrawable(context.getResources().getDrawable(R.drawable.watering_mid));
                break;
            case PLENTIFUL:
                holder.imageViewWatering.setImageDrawable(context.getResources().getDrawable(R.drawable.watering_max));
                break;
        }
    }

    static class PlantHolder {
        public ImageView imageViewPlant;
        public TextView textViewPlantName;
        public TextView textViewLatin;
        public ImageView imageViewInsolation;
        public ImageView imageViewHumidity;
        public ImageView imageViewWatering;
    }
}
