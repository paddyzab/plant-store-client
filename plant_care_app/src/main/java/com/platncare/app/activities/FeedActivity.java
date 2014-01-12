package com.platncare.app.activities;

import android.app.Activity;
;
import android.os.Bundle;
import android.util.Log;
import com.platncare.app.R;
import com.platncare.app.fragments.PlantsFeedFragment;
import com.platncare.app.models.Plant;
import com.platncare.app.utils.FragmentUtils;
import model.Token;

import java.util.ArrayList;

public class FeedActivity extends Activity {

    private final static String LOG_TAG = FeedActivity.class.getSimpleName();

    private PlantsFeedFragment plantsFeedFragment;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getExtras();


        attachInitialFragment();
    }

    private void getExtras() {
        Bundle args = getIntent().getExtras();
        token = (Token) args.getSerializable("token");
    }

    private void attachInitialFragment() {
        plantsFeedFragment = PlantsFeedFragment.newInstance(testPlantsList());
        FragmentUtils.setFragment(this, plantsFeedFragment, R.id.frameLayoutContainer);
    }

    private ArrayList<Plant> testPlantsList() {

        ArrayList<Plant> plants = new ArrayList<Plant>();

        Plant plant1 = new Plant();
        plant1.setPlantName("Plant 1");
        plant1.setPlantImageUrl("http://www.tvorlica.pl/wp-content/uploads/2012/03/roza-300x300.jpg");

        Plant plant2 = new Plant();
        plant2.setPlantName("Plant 2");
        plant2.setPlantImageUrl("http://bi.gazeta.pl/im/9/11698/z11698679Q,Marchewka.jpg");

        Plant plant3 = new Plant();
        plant3.setPlantName("Plant 3");
        plant3.setPlantImageUrl("http://www.tvorlica.pl/wp-content/uploads/2012/03/roza-300x300.jpg");

        Plant plant4 = new Plant();
        plant4.setPlantName("Plant 4");
        plant4.setPlantImageUrl("http://bi.gazeta.pl/im/9/11698/z11698679Q,Marchewka.jpg");

        plants.add(plant1);
        plants.add(plant2);
        plants.add(plant3);
        plants.add(plant4);

        return plants;
    }
}
