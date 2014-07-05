package com.platncare.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import client.model.Plant;
import com.platncare.app.R;
import com.platncare.app.fragments.CalendarFragment;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.IntentKeys;

//TODO PlantDetailsActivity can be super class for this class, or this will be
//TODO a fragment only.
public class CalendarActivity extends Activity {

    private Plant plant;
    private static final String PLANT_KEY = "plant_key";
    private Bundle activityState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initActionBar();

        if(savedInstanceState != null) {
            plant = (Plant) savedInstanceState.getSerializable(PLANT_KEY);
            displayData(plant);
            initFragments(plant);
        } else {
            populatePlantData();
        }
    }

    protected void onPause() {
        super.onPause();

        if(activityState == null) {
            activityState = new Bundle();
        }

        activityState.putSerializable(PLANT_KEY, plant);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(activityState != null) {
            plant = (Plant) activityState.getSerializable(PLANT_KEY);
            displayData(plant);
            initFragments(plant);
        }else {
            populatePlantData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLANT_KEY, plant);
    }

    private void populatePlantData() {

        final Intent intent = getIntent();

        if (intent.hasExtra(IntentKeys.PLANT_KEY)) {
            plant = (Plant) intent.getSerializableExtra(IntentKeys.PLANT_KEY);
            displayData(plant);
            initFragments(plant);
        } else {
            throw new RuntimeException("Intent data are empty, this should not happen.");
        }
    }

    private void initFragments(Plant plant) {
        CalendarFragment calendarFragment = CalendarFragment.newInstance(plant);
        FragmentUtils.setFragment(this, calendarFragment, R.id.fragment_container);
    }

    private void displayData(Plant plant) {
        getActionBar().setTitle(plant.getName());
    }

    private void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}