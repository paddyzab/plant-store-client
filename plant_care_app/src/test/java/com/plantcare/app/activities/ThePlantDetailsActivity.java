package com.plantcare.app.activities;

import android.app.Activity;
import com.platncare.app.activities.PlantDetailsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ThePlantDetailsActivity {

    private Activity testedActivity;

    @Before
    public void setUp() {
        testedActivity = Robolectric.buildActivity(PlantDetailsActivity.class).create().get();
    }

    @Test
    public void shouldInitFragmentData() {
    }

}
