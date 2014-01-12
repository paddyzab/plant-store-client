package com.platncare.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.platncare.app.R;
import model.Plant;

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
            holder.plantImage = (ImageView) rowView.findViewById(R.id.imageViewPlant);
            holder.plantName = (TextView) rowView.findViewById(R.id.textViewPlantName);

            rowView.setTag(holder);
        }

        PlantHolder holder = (PlantHolder) rowView.getTag();
        holder.plantName.setText(getItem(position).getName());

        return rowView;
    }

    static class PlantHolder {
        public ImageView plantImage;
        public TextView plantName;
    }
}
