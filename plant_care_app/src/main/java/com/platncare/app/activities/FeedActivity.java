package com.platncare.app.activities;

import android.app.Activity;

import android.os.Bundle;
import com.platncare.app.R;
import com.platncare.app.fragments.PlantsFeedFragment;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.IntentKeys;
import model.Token;

public class FeedActivity extends Activity {


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
               throw new RuntimeException("To initiate we need TokenKey.");
            }
        }
    }

    private void attachInitialFragment() {
        PlantsFeedFragment plantsFeedFragment = PlantsFeedFragment.newInstance(token);
        FragmentUtils.setFragment(this, plantsFeedFragment, R.id.frameLayoutContainer);
    }
}
