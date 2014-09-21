package com.platncare.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.platncare.app.R;
import com.platncare.app.database.data_sources.PlantDAO;
import com.platncare.app.fragments.PlantsFeedFragment;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.utils.Preferences;
import java.sql.SQLException;

public class FeedActivity extends Activity {


    private String token;
    private PlantsFeedFragment plantsFeedFragment;
    private PlantDAO dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getExtras();

        dataSource = new PlantDAO(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        attachInitialFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startLogout();
                break;
            case R.id.menu_refresh:
                refreshPlantsList();
                break;
            case R.id.menu_logout:
                startLogout();
                Toast.makeText(FeedActivity.this, R.string.toast_logout, Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshPlantsList() {
        if (plantsFeedFragment != null) {
            plantsFeedFragment.requestPlantsArray();
        }
    }

    private void startLogout() {
        Preferences.clearAllPreferences(FeedActivity.this);
        finish();
    }

    private void getExtras() {
        Bundle args = getIntent().getExtras();

        if (args != null) {
            if (args.containsKey(IntentKeys.TOKEN_KEY)) {
                token = args.getString(IntentKeys.TOKEN_KEY);
            } else {
                token = Preferences.getAppToken(this);
            }
        }
    }

    private void attachInitialFragment() {
        plantsFeedFragment = PlantsFeedFragment.newInstance(token);
        FragmentUtils.setFragment(this, plantsFeedFragment, R.id.frameLayoutContainer);
    }
}
