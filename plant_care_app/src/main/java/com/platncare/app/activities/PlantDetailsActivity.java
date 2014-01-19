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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import client.endpoint.PlantEndpoint;
import client.http.exception.HTTPClientException;
import com.platncare.app.R;
import com.platncare.app.backend.GetPlantAsyncTask;
import com.platncare.app.backend.GetPlantExecutor;
import com.platncare.app.fragments.PlantDetailsFragment;
import com.platncare.app.nfc.MimeType;
import com.platncare.app.utils.FragmentUtils;
import com.platncare.app.utils.MessagesUtils;
import com.platncare.app.utils.Preferences;
import model.Plant;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class PlantDetailsActivity extends Activity {

    ActionBar actionBar;
    private Plant plant;
    private String PLANT_KEY = "plant";
    private PlantDetailsFragment plantDetailsFragment;
    private GetPlantAsyncTask getPlantAsyncTask;

    private NfcAdapter adapter;
    private boolean inWriteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        adapter = NfcAdapter.getDefaultAdapter(this);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        populatePlantData(getIntent());
        initFragments();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableWriteMode();
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

        } else if (intent.hasExtra(PLANT_KEY)) {
            plant = (Plant) intent.getSerializableExtra(PLANT_KEY);
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

    private void writeTag() {
            enableWriteMode();
    }

    private void displayData(Plant plant) {
        actionBar.setTitle(plant.getName());
    }

    private void initFragments() {
        plantDetailsFragment = new PlantDetailsFragment();
        FragmentUtils.setFragment(this, plantDetailsFragment, R.id.fragment_container);
    }

    /**
     * Format a tag and write our NDEF message
     */
    private boolean writeTag(Tag tag) {
        //TODO: after publishing app to the play store, create appRecord
        // record to launch Play Store if app is not installed
        //NdefRecord appRecord = NdefRecord.createApplicationRecord("com.plantcare.app");

        byte[] payload = ByteBuffer.allocate(8).putLong(plant.getId()).array();
        byte[] mimeBytes = MimeType.PLANT_CARE_TYPE.getBytes(Charset.forName("US-ASCII"));
        NdefRecord plantRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes,
                new byte[0], payload);

        //TODO later to NdefRecord appRecord
        NdefMessage message = new NdefMessage(new NdefRecord[] {plantRecord});

        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    MessagesUtils.displayToastMessage(R.string.message_read_only_tag, this);
                    return false;
                }

                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    MessagesUtils.displayToastMessage(R.string.message_not_enough_free_space, this);
                    return false;
                }

                ndef.writeNdefMessage(message);
                MessagesUtils.displayToastMessage(R.string.message_tag_written_successfully, this);
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        MessagesUtils.displayToastMessage(R.string.message_tag_written_successfully, this);
                        return true;
                    } catch (IOException e) {
                        MessagesUtils.displayToastMessage(R.string.message_unable_to_format, this);
                        return false;
                    }
                } else {
                    MessagesUtils.displayToastMessage(R.string.message_no_ndef_support, this);
                    return false;
                }
            }
        } catch (Exception e) {
            MessagesUtils.displayToastMessage(R.string.message_failed_to_write, this);
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

        adapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }

    private void disableWriteMode() {
        adapter.disableForegroundDispatch(this);
    }
}
