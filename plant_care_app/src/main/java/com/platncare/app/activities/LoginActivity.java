package com.platncare.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import client.model.Token;
import com.platncare.app.R;
import com.platncare.app.backend.RequestTokenAsyncTask;
import com.platncare.app.backend.RequestTokenExecutor;
import com.platncare.app.database.data_sources.PlantDAO;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.utils.Preferences;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import java.sql.SQLException;

public class LoginActivity extends Activity implements OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private View relativeLayoutLoginForm;
    private View linearLayoutLoginStatus;
    private TextView textViewMessageView;
    private String stringToken;
    private Form validationForm;
    private Button buttonSignIn;

    private PlantDAO dataSource;

    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.up_in, R.anim.up_out);

        setContentView(R.layout.activity_login);
        prepareActionBar();
        initializeDatabase();
        initializeViews();
        initializeListeners();
        createValidationForm();
        retriveAndPopulateEditTexts(savedInstanceState);
    }

    private void initializeDatabase() {
        dataSource = new PlantDAO(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataSource.getAllPlants();

        // TODO merge current data with the remote ones.
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
        outState.putString(EMAIL_KEY, editTextEmail.getText().toString());
        outState.putString(PASSWORD_KEY, editTextPassword.getText().toString());
    }

    private void retriveAndPopulateEditTexts(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            editTextEmail.setText(savedInstanceState.getString(EMAIL_KEY));
            editTextPassword.setText(savedInstanceState.getString(PASSWORD_KEY));
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

    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        overridePendingTransition(R.anim.up_in, R.anim.up_out);
        String stringToken = Preferences.getAppToken(LoginActivity.this);

        //When we have token persisted just start FeedActivity
        if(stringToken != null && !TextUtils.isEmpty(stringToken)) {
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

        if(validationForm.validate()) {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            textViewMessageView.setText(R.string.login_progress_signing_in);
            hideKeyboard();

            showProgress(true);

            new RequestTokenAsyncTask(executor).execute(email, password);
        } else {
            showProgress(false);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private RequestTokenExecutor executor = new RequestTokenExecutor() {

        @Override
        public void onSuccess(Token token) {
            LoginActivity.this.stringToken = token.getToken();
            Preferences.saveAppToken(stringToken, LoginActivity.this);
            startFeedActivity();
        }

        @Override
        public void onFailure(Exception e) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, getString(R.string.toast_wrong_credentials), Toast.LENGTH_LONG).show();
        }
    };

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
        if(actionBar != null) {
            actionBar.hide();
        }
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
