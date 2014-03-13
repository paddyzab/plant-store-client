package com.platncare.app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.platncare.app.R;
import client.model.Plant;

import java.util.ArrayList;

public class PlantAdapter extends ArrayAdapter<Plant> {

    Context context;
    private static final String TAG = PlantAdapter.class.getSimpleName();

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
            holder.imageViewPlant = (ImageView) rowView.findViewById(R.id.imageViewPlant);
            holder.textViewPlantName = (TextView) rowView.findViewById(R.id.textViewPlantName);
            holder.textViewLatin = (TextView) rowView.findViewById(R.id.textViewLatin);
            holder.imageViewInsolation = (ImageView) rowView.findViewById(R.id.imageViewInsolation);
            holder.imageViewWatering = (ImageView) rowView.findViewById(R.id.imageViewWatering);
            holder.imageViewHumidity = (ImageView) rowView.findViewById(R.id.imageViewHumidity);

            rowView.setTag(holder);
        }

        //TODO: connect treatment images to correct backend treatment values.

        PlantHolder holder = (PlantHolder) rowView.getTag();
        holder.textViewPlantName.setText(getItem(position).getName().toUpperCase());
        holder.textViewLatin.setText(getItem(position).getKind().getLatinName());

        return rowView;
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
