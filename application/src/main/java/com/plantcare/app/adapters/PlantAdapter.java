package com.plantcare.app.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.plantcare.app.R;
import com.plantcare.app.backend.models.Humidity;
import com.plantcare.app.backend.models.Insolation;
import com.plantcare.app.backend.models.Watering;
import com.plantcare.app.storage.greendao.model.DaoMaster;
import com.plantcare.app.storage.greendao.model.DaoSession;
import com.plantcare.app.storage.greendao.model.KindObject;
import com.plantcare.app.storage.greendao.model.KindObjectDao;
import com.plantcare.app.storage.greendao.model.PlantObject;
import java.util.List;

public class PlantAdapter extends ArrayAdapter<PlantObject> {

    private final Context context;
    private final KindObjectDao kindObjectDao;

    public PlantAdapter(final Context context, final List<PlantObject> plants) {
        super(context, R.layout.adapter_plant, plants);
        this.context = context;

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "PLANT_OBJECT", null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession daoSession = daoMaster.newSession();
        kindObjectDao = daoSession.getKindObjectDao();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_plant, parent, false);

            final PlantHolder holder = new PlantHolder();
            holder.textViewPlantName = (TextView) rowView.findViewById(R.id.textViewPlantName);
            holder.textViewLatin = (TextView) rowView.findViewById(R.id.textViewLatin);
            holder.imageViewInsolation = (ImageView) rowView.findViewById(R.id.imageViewInsolation);
            holder.imageViewWatering = (ImageView) rowView.findViewById(R.id.imageViewWatering);
            holder.imageViewHumidity = (ImageView) rowView.findViewById(R.id.imageViewHumidity);

            rowView.setTag(holder);
        }

        final PlantHolder holder = (PlantHolder) rowView.getTag();
        holder.textViewPlantName.setText(getItem(position).getName().toUpperCase());

        final Long kindId = getItem(position).getKindId();
        final KindObject kindObject = kindObjectDao.loadByRowId(kindId);

        holder.textViewLatin.setText(kindObject.getLatin_name());
        setWateringDrawable(holder, Watering.fromString(kindObject.getWatering_season()));
        stHumidityDrawable(holder, Humidity.fromString(kindObject.getHumidity()));
        setInsolationDrawable(holder, Insolation.fromString(kindObject.getInsolation()));

        return rowView;
    }

    private void setInsolationDrawable(final PlantHolder holder, final Insolation insolation) {
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

    private void stHumidityDrawable(final PlantHolder holder, final Humidity humidity) {
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

    private void setWateringDrawable(final PlantHolder holder, final Watering wateringSeason) {
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
