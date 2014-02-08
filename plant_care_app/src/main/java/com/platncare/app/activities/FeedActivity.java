package com.platncare.app.activities;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.platncare.app.R;
import com.platncare.app.fragments.PlantsFeedFragment;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.utils.Preferences;
import model.Token;

public class FeedActivity extends Activity {


    private String token;

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
            case R.id.menu_logout:
                startLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startLogout() {
        Preferences.clearAllPreferences(FeedActivity.this);
        finish();
    }

    private void getExtras() {
        Bundle args = getIntent().getExtras();

        if(args != null) {
            if(args.containsKey(IntentKeys.TOKEN_KEY)) {
                token = args.getString(IntentKeys.TOKEN_KEY);
            } else {
               throw new RuntimeException("To initiate we need TokenKey.");
            }
        }
    }

    private void attachInitialFragment() {
        PlantsFeedFragment plantsFeedFragment = PlantsFeedFragment.newInstance(token);
        FragmentUtils.setFragment(this, plantsFeedFragment, R.id.frameLayoutContainer);
    }
}
