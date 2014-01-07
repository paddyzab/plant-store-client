package com.platncare.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.platncare.app.R;
import com.platncare.app.fragments.PlantDetailsFragment;
import com.platncare.app.models.Plant;
import com.platncare.app.utils.FragmentUtils;

public class PlantDetailsActivity extends Activity {

    private Plant plant;
    private String PLANT_KEY = "plant";

    private PlantDetailsFragment plantDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        Intent intent = getIntent();
        plant = (Plant) intent.getSerializableExtra(PLANT_KEY);

        initActionBar();
        initFragments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(plant.getPlantName());
    }

    private void initFragments() {
        plantDetailsFragment = new PlantDetailsFragment();
        FragmentUtils.setFragment(this, plantDetailsFragment, R.id.fragment_container);
    }


}
