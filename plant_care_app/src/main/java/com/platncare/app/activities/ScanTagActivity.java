package com.platncare.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.*;
import com.platncare.app.R;
import com.platncare.app.nfc.MimeType;
import com.platncare.app.utils.IntentKeys;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ScanTagActivity extends Activity {

    private TextView textViewStatus;
    private TextView textViewCounter;
    private Button buttonWrite;

    private NfcAdapter adapter;
    private boolean inWriteMode;
    private long plantId;
    private Timer timerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_tag);

        getExtras();
        initActionBar();
        initViews();
        adapter = NfcAdapter.getDefaultAdapter(this);
        timerCount = new Timer();
        buttonWrite.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableWriteMode();
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

    private void getExtras() {
        Bundle args = getIntent().getExtras();

        if(args != null) {
            if(args.containsKey(IntentKeys.PLANT_KEY)) {
                plantId = args.getLong(IntentKeys.PLANT_KEY);
            } else {
                throw new RuntimeException("To initiate we need PlantKey");
            }
        }
    }

    private void initViews() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/sourcesansproregular.ttf");

        textViewStatus = (TextView) findViewById(R.id.textViewMessage);
        textViewStatus.setTypeface(typeface);

        textViewCounter = (TextView) findViewById(R.id.textViewCounter);
        textViewCounter.setTypeface(typeface);

        buttonWrite = (Button) findViewById(R.id.buttonWrite);
        buttonWrite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonWrite.setEnabled(true);
                enableWriteMode();
            }
        });
    }

    /**
     * Format a tag and write our NDEF message
     */
    private boolean writeTag(Tag tag) {
        //TODO: after publishing app to the play store, create appRecord
        // record to launch Play Store if app is not installed
        //NdefRecord appRecord = NdefRecord.createApplicationRecord("com.plantcare.app");

        byte[] payload = ByteBuffer.allocate(8).putLong(plantId).array();
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
                    displayMessage(R.string.message_read_only_tag);
                    return false;
                }

                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    displayMessage(R.string.message_not_enough_free_space);
                    return false;
                }

                ndef.writeNdefMessage(message);
                startCounting();
                displayMessage(R.string.message_tag_written_successfully);
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        displayMessage(R.string.message_tag_written_successfully);
                        return true;
                    } catch (IOException e) {
                        displayMessage(R.string.message_unable_to_format);
                        return false;
                    }
                } else {
                    displayMessage(R.string.message_no_ndef_support);
                    return false;
                }
            }
        } catch (Exception e) {
            displayMessage(R.string.message_failed_to_write);
        }

        return false;
    }

    private void startCounting() {
        textViewCounter.setVisibility(View.VISIBLE);
        timerCount.start();
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

    private void displayMessage(int messageId) {
        textViewStatus.setText(getResources().getString(messageId));
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
    }

    private class Timer extends CountDownTimer {
        public Timer() {
            super(3000, 1000);
        }

        @Override
        public void onFinish() {
            textViewCounter.setText("seconds remaining: " + 0);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            textViewCounter.setVisibility(View.VISIBLE);
            Toast.makeText(ScanTagActivity.this, "Tag written", Toast.LENGTH_LONG).show();
            buttonWrite.setEnabled(true);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textViewCounter.setText("seconds remaining: " + millisUntilFinished / 1000);

        }
    }
}
