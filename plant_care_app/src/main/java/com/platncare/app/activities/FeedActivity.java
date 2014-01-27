package com.platncare.app.activities;

import android.app.Activity;
;
import android.os.Bundle;
import com.platncare.app.R;
import com.platncare.app.fragments.PlantsFeedFragment;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.IntentKeys;
import model.Plant;
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

        if(args != null) {
            if(args.containsKey(IntentKeys.TOKEN_KEY)) {
                token = (Token) args.getSerializable(IntentKeys.TOKEN_KEY);
            } else {
                new RuntimeException("To initiate we need Token Key.");
            }
        }
    }

    private void attachInitialFragment() {
        plantsFeedFragment = PlantsFeedFragment.newInstance(token);
        FragmentUtils.setFragment(this, plantsFeedFragment, R.id.frameLayoutContainer);
    }
}
