package com.plantcare.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import client.model.Kind;
import client.model.Plant;
import client.model.Token;
import com.plantcare.app.R;
import com.plantcare.app.backend.GetPlantsListAsyncTask;
import com.plantcare.app.backend.GetPlantsListExecutor;
import com.plantcare.app.backend.RequestTokenAsyncTask;
import com.plantcare.app.backend.RequestTokenExecutor;
import com.plantcare.app.storage.greendao.model.DaoMaster;
import com.plantcare.app.storage.greendao.model.DaoSession;
import com.plantcare.app.storage.greendao.model.KindObject;
import com.plantcare.app.storage.greendao.model.KindObjectDao;
import com.plantcare.app.storage.greendao.model.KindToPlantObject;
import com.plantcare.app.storage.greendao.model.KindToPlantObjectDao;
import com.plantcare.app.storage.greendao.model.PlantObject;
import com.plantcare.app.storage.greendao.model.PlantObjectDao;
import com.plantcare.app.utils.IntentKeys;
import com.plantcare.app.utils.Preferences;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import java.util.ArrayList;

public class LoginActivity extends Activity implements OnClickListener {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private View relativeLayoutLoginForm;
    private View linearLayoutLoginStatus;
    private TextView textViewMessageView;
    private String stringToken;
    private Form validationForm;
    private Button buttonSignIn;

    private static final String EMAILKEY = "EMAIL_KEY";
    private static final String PASSWORDKEY = "PASSWORD_KEY";

    private PlantObjectDao plantDao;
    private KindObjectDao kindDao;
    private KindToPlantObjectDao kindToPlantObjectDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.up_in, R.anim.up_out);

        setContentView(R.layout.activity_login);
        prepareActionBar();
        initializeViews();
        initializeListeners();
        createValidationForm();
        retriveAndPopulateEditTexts(savedInstanceState);

        editTextEmail.setText("patryk.zabicki@gmail.com");
        editTextPassword.setText("wiosna12");

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(LoginActivity.this, "PLANT_OBJECT", null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession daoSession = daoMaster.newSession();

        plantDao = daoSession.getPlantObjectDao();
        kindDao = daoSession.getKindObjectDao();
        kindToPlantObjectDao = daoSession.getKindToPlantObjectDao();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in_button:
                attemptLogin();
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EMAILKEY, editTextEmail.getText().toString());
        outState.putString(PASSWORDKEY, editTextPassword.getText().toString());
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.up_in, R.anim.up_out);
        String stringToken = Preferences.getAppToken(LoginActivity.this);

        //When we have token persisted just start FeedActivity
        if (stringToken != null && !TextUtils.isEmpty(stringToken)) {
            this.stringToken = stringToken;
            startFeedActivity();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login validationForm.
     * If there are validationForm errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        editTextEmail.setError(null);
        editTextPassword.setError(null);

        if (validationForm.validate()) {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            textViewMessageView.setText(R.string.login_progress_signing_in);
            hideKeyboard();

            showProgress(true);

            new RequestTokenAsyncTask(tokenExecutor).execute(email, password);
        } else {
            showProgress(false);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private RequestTokenExecutor tokenExecutor = new RequestTokenExecutor() {

        @Override
        public void onSuccess(Token token) {
            LoginActivity.this.stringToken = token.getToken();
            Preferences.saveAppToken(stringToken, LoginActivity.this);
            fetchData();
        }

        @Override
        public void onFailure(Exception e) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, getString(R.string.toast_wrong_credentials), Toast.LENGTH_LONG).show();
        }
    };

    private GetPlantsListExecutor plantsExecutor = new GetPlantsListExecutor() {

        @Override
        public void onSuccess(final ArrayList<Plant> plants) {
            showProgress(false);

            for (final Plant plant : plants) {
                final Kind kind = plant.getKind();
                final PlantObject plantObject = new PlantObject(plant.getId(), plant.getName(), plant.getDescription(), kind.getId());
                plantDao.insert(plantObject);

                final KindObject kindObject = new KindObject(kind.getId(),
                        kind.getName(),
                        kind.getLatinName(),
                        kind.getTreatment().getWateringSeason(),
                        kind.getTreatment().getWateringRest(),
                        kind.getTreatment().isDryBetweenWateringInSeason(),
                        kind.getTreatment().isDryBetweenWateringInRest(),
                        kind.getTreatment().getInsolation(),
                        kind.getTreatment().getSeasonTempMin(),
                        kind.getTreatment().getSeasonTempMax(),
                        kind.getTreatment().getRestTempMin(),
                        kind.getTreatment().getRestTempMax(),
                        kind.getTreatment().getHumidity(),
                        kind.getTreatment().getComment());
                kindObject.setId(kind.getId());
                kindDao.insert(kindObject);

                KindToPlantObject kindToPlantObject = new KindToPlantObject(kind.getId(), plant.getId());
                kindToPlantObjectDao.insert(kindToPlantObject);
            }

            startFeedActivity();
        }

        @Override
        public void onFailure(Exception e) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, "Something went wrong.",
                    Toast.LENGTH_LONG).show();

            Log.e(TAG, String.format("Error on failure, message /n %s", e.getMessage()));
            Log.e(TAG, String.format("Error on failure, stacktrace /n %s", e.getStackTrace()));
        }
    };

    private void fetchData() {
        new GetPlantsListAsyncTask(plantsExecutor).execute(stringToken);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            linearLayoutLoginStatus.setVisibility(View.VISIBLE);
            linearLayoutLoginStatus.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            linearLayoutLoginStatus.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            relativeLayoutLoginForm.setVisibility(View.VISIBLE);
            relativeLayoutLoginForm.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            relativeLayoutLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            linearLayoutLoginStatus.setVisibility(show ? View.VISIBLE : View.GONE);
            relativeLayoutLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private TextView.OnEditorActionListener passwordOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        }
    };

    private void prepareActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void retriveAndPopulateEditTexts(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            editTextEmail.setText(savedInstanceState.getString(EMAILKEY));
            editTextPassword.setText(savedInstanceState.getString(PASSWORDKEY));
        }
    }

    private void initializeViews() {
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword.setOnEditorActionListener(passwordOnEditorActionListener);
        relativeLayoutLoginForm = findViewById(R.id.login_form);
        linearLayoutLoginStatus = findViewById(R.id.login_status);
        textViewMessageView = (TextView) findViewById(R.id.login_status_message);
        buttonSignIn = (Button) findViewById(R.id.sign_in_button);
    }

    private void initializeListeners() {
        buttonSignIn.setOnClickListener(this);
    }

    private void createValidationForm() {
        Validate emailField = new Validate(editTextEmail);
        Validate passwordField = new Validate(editTextPassword);

        emailField.addValidator(new NotEmptyValidator(LoginActivity.this));
        emailField.addValidator(new EmailValidator(LoginActivity.this));
        passwordField.addValidator(new NotEmptyValidator(LoginActivity.this));

        validationForm = new Form();
        validationForm.addValidates(emailField);
        validationForm.addValidates(passwordField);
    }

    private void startFeedActivity() {
        //Start FeedActivity after successful Login
        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
        intent.putExtra(IntentKeys.TOKEN_KEY, stringToken);
        startActivity(intent);

        //Destroy this activity to remove it from the activity stack
        LoginActivity.this.finish();
    }
}
