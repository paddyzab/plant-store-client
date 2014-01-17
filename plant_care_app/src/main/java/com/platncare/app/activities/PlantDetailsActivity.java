package com.platncare.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;
import com.platncare.app.R;
import com.platncare.app.fragments.PlantDetailsFragment;
import com.platncare.app.nfc.MimeType;
import com.platncare.app.utils.FragmentUtils;
import model.Plant;

import java.io.IOException;
import java.nio.charset.Charset;

public class PlantDetailsActivity extends Activity {

    private Plant plant;
    private String PLANT_KEY = "plant";
    private PlantDetailsFragment plantDetailsFragment;

    private NfcAdapter mAdapter;
    private boolean inWriteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        // see if app was started from a tag and show game console
        Intent intent = getIntent();
        if(intent.getType() != null && intent.getType().equals(MimeType.PLANT_CARE_TYPE)) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord plantRecord = msg.getRecords()[0];
            //TODO: using plantRecord we should have a way to request plant data from backend (more less unique)
        } else if (intent.hasExtra(PLANT_KEY)) {
            plant = (Plant) intent.getSerializableExtra(PLANT_KEY);
        } else {
            throw new RuntimeException("Intent data are empty, this should not happen.");
        }
        displayData(plant);
        initActionBar();
        //TODO add menu and from there save tag feature
        initFragments();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableWriteMode();
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

    /**
     * Called when our blank tag is scanned executing the PendingIntent
     */
    @Override
    public void onNewIntent(Intent intent) {
        if(inWriteMode) {
            inWriteMode = false;

            // write to newly scanned tag
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(tag);
        }
    }


    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(plant.getName());
    }

    private void writeTag() {
            enableWriteMode();
    }

    private void displayData(Plant plant) {
        //TODO populate UI elements with the plant Data
    }

    private void initFragments() {
        plantDetailsFragment = new PlantDetailsFragment();
        FragmentUtils.setFragment(this, plantDetailsFragment, R.id.fragment_container);
    }

    /**
     * Format a tag and write our NDEF message
     */
    private boolean writeTag(Tag tag) {
        // record to launch Play Store if app is not installed
        NdefRecord appRecord = NdefRecord.createApplicationRecord("com.plantcare.app");

        // record that contains our custom "retro console" game data, using custom MIME_TYPE
        byte[] payload = plant.getDescription().getBytes();
        byte[] mimeBytes = MimeType.PLANT_CARE_TYPE.getBytes(Charset.forName("US-ASCII"));
        NdefRecord plantRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes,
                new byte[0], payload);
        NdefMessage message = new NdefMessage(new NdefRecord[] { plantRecord, appRecord});

        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    displayMessage("Read-only tag.");
                    return false;
                }

                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    displayMessage("Tag doesn't have enough free space.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                displayMessage("Tag written successfully.");
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        displayMessage("Tag written successfully!\nClose this app and scan tag.");
                        return true;
                    } catch (IOException e) {
                        displayMessage("Unable to format tag to NDEF.");
                        return false;
                    }
                } else {
                    displayMessage("Tag doesn't appear to support NDEF format.");
                    return false;
                }
            }
        } catch (Exception e) {
            displayMessage("Failed to write tag");
        }

        return false;
    }

    /**
     * Force this Activity to get NFC events first
     */
    private void enableWriteMode() {
        inWriteMode = true;

        // set up a PendingIntent to open the app when a tag is scanned
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] filters = new IntentFilter[] { tagDetected };

        mAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }

    private void disableWriteMode() {
        mAdapter.disableForegroundDispatch(this);
    }

    private void displayMessage(String message) {
        Toast.makeText(PlantDetailsActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
