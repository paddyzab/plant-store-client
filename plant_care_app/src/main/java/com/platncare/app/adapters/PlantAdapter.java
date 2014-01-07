package com.platncare.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.platncare.app.R;
import com.platncare.app.models.Plant;
import com.squareup.picasso.Picasso;

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
        holder.plantName.setText(getItem(position).getPlantName());

        String url = getItem(position).getPlantImageUrl();
        Picasso.with(context).load(url).resize(100, 100).centerInside().placeholder(context.getResources().getDrawable(R.drawable.placeholder)).into(holder.plantImage);

        return rowView;
    }

    static class PlantHolder {
        public ImageView plantImage;
        public TextView plantName;
    }
}
