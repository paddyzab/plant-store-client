package com.plantcare.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import com.plantcare.app.R;
import com.plantcare.app.fragments.PlantDetailsFragment;
import com.plantcare.app.nfc.MimeType;
import com.plantcare.app.storage.greendao.model.DaoMaster;
import com.plantcare.app.storage.greendao.model.DaoSession;
import com.plantcare.app.storage.greendao.model.KindObject;
import com.plantcare.app.storage.greendao.model.KindObjectDao;
import com.plantcare.app.storage.greendao.model.PlantObject;
import com.plantcare.app.storage.greendao.model.PlantObjectDao;
import com.plantcare.app.utils.FragmentUtils;
import com.plantcare.app.utils.IntentKeys;
import java.nio.ByteBuffer;

public class PlantDetailsActivity extends Activity {

    private final static String LOG_TAG = PlantDetailsActivity.class.getSimpleName();

    private ActionBar actionBar;
    private static final String PLANT_KEY = "plant_key";
    private PlantObjectDao plantObjectDao;
    private KindObjectDao kindObjectDao;

    private Long plantId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "PLANT_OBJECT", null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession daoSession = daoMaster.newSession();

        plantObjectDao = daoSession.getPlantObjectDao();
        kindObjectDao = daoSession.getKindObjectDao();

        initActionBar();

        if (savedInstanceState != null) {
            plantId = (Long) savedInstanceState.getSerializable(PLANT_KEY);

            PlantObject plantObject = plantObjectDao.loadByRowId(plantId);
            KindObject kindObject = kindObjectDao.load(plantObject.getKindId());
            displayData(plantObject);
            initFragments(kindObject);
        } else {
            populatePlantData();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLANT_KEY, plantId);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.plant_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                startWriteTagActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startWriteTagActivity() {
        final Intent intent = new Intent(PlantDetailsActivity.this, ScanTagActivity.class);
        intent.putExtra(IntentKeys.PLANT_KEY, plantId);

        startActivity(intent);
    }

    private void populatePlantData() {

        final Intent intent = getIntent();

        Long plantId;

        if (intent.getType() != null && intent.getType().equals(MimeType.PLANT_CARE_TYPE)) {
            plantId = getPlantIdFromNDEFMessage();
        } else if (intent.hasExtra(IntentKeys.PLANT_KEY)) {
            plantId = (Long) intent.getSerializableExtra(IntentKeys.PLANT_KEY);
        } else {
            throw new RuntimeException("Intent data are empty, this should not happen.");
        }

        PlantObject plantObject = plantObjectDao.loadByRowId(plantId);
        KindObject kindObject = kindObjectDao.load(plantObject.getKindId());
        displayData(plantObject);
        initFragments(kindObject);
    }

    private long getPlantIdFromNDEFMessage() {
        final Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        final NdefMessage msg = (NdefMessage) rawMsgs[0];
        final NdefRecord plantRecord = msg.getRecords()[0];
        byte[] payload = plantRecord.getPayload();
        return ByteBuffer.wrap(payload).getLong();
    }

    private void displayData(final PlantObject plantObject) {
        actionBar.setTitle(plantObject.getName());
    }

    private void initFragments(final KindObject kind) {
        final PlantDetailsFragment plantDetailsFragment = PlantDetailsFragment.newInstance(kind);
        FragmentUtils.setFragment(this, plantDetailsFragment, R.id.fragment_container);
    }

    private void initActionBar() {
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
