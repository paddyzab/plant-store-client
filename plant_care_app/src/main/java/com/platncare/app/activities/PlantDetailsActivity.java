package com.platncare.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import com.platncare.app.R;
import com.platncare.app.backend.GetPlantAsyncTask;
import com.platncare.app.backend.GetPlantExecutor;
import com.platncare.app.fragments.PlantDetailsFragment;
import com.platncare.app.nfc.MimeType;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.IntentKeys;
import model.Plant;

import java.nio.ByteBuffer;

public class PlantDetailsActivity extends Activity {

    private ActionBar actionBar;
    private Plant plant;
    private PlantDetailsFragment plantDetailsFragment;
    private GetPlantAsyncTask getPlantAsyncTask;

    private NfcAdapter adapter;
    private boolean inWriteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        adapter = NfcAdapter.getDefaultAdapter(this);
        initActionBar();

        populatePlantData(getIntent());
        initFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plant_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                writeTag();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeTag() {
        Intent intent = new Intent(PlantDetailsActivity.this, ScanTagActivity.class);
        intent.putExtra(IntentKeys.PLANT_KEY, plant.getId());

        startActivity(intent);
    }

    private void populatePlantData(Intent intent) {

        if(intent.getType() != null && intent.getType().equals(MimeType.PLANT_CARE_TYPE)) {

            getPlantAsyncTask = new GetPlantAsyncTask(new GetPlantExecutor() {
                @Override
                public void onSuccess(Plant plant) {
                    displayData(plant);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
            getPlantAsyncTask.execute(this, getPlantIdFromNDEFMessage());

        } else if (intent.hasExtra(IntentKeys.PLANT_KEY)) {
            plant = (Plant) intent.getSerializableExtra(IntentKeys.PLANT_KEY);
            displayData(plant);
        } else {
            throw new RuntimeException("Intent data are empty, this should not happen.");
        }

    }

    private long getPlantIdFromNDEFMessage() {
        Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        NdefRecord plantRecord = msg.getRecords()[0];
        byte[] payload = plantRecord.getPayload();
        return ByteBuffer.wrap(payload).getLong();
    }

    private void displayData(Plant plant) {
        actionBar.setTitle(plant.getName());
    }

    private void initFragments() {
        plantDetailsFragment = new PlantDetailsFragment();
        FragmentUtils.setFragment(this, plantDetailsFragment, R.id.fragment_container);
    }

    private void initActionBar() {
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
