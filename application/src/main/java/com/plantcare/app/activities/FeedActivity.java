package com.plantcare.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.plantcare.app.R;
import com.plantcare.app.fragments.PlantsFeedFragment;
import com.plantcare.app.utils.FragmentUtils;
import com.plantcare.app.utils.IntentKeys;
import com.plantcare.app.utils.Preferences;

public class FeedActivity extends Activity {


    private String token;
    private PlantsFeedFragment plantsFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getExtras();

        attachInitialFragment();
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
                finish();
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
            //TODO implement refresh module (maybe not a request based but on the push messages from the server)
            //TODO oooor each request will trigger full data synch
            //plantsFeedFragment.requestPlantsArray();
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
