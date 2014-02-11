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
import com.platncare.app.R;
import com.platncare.app.backend.RequestTokenAsyncTask;
import com.platncare.app.backend.RequestTokenExecutor;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.utils.Preferences;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import model.Token;


public class LoginActivity extends Activity implements OnClickListener {

    private EditText emailView;
    private EditText passwordView;
    private View loginFormView;
    private View loginStatusView;
    private TextView loginStatusMessageView;
    private String stringToken;
    private Form validationForm;
    private Button buttonGooglePlus;
    private Button buttonFacebook;
    private Button buttonTwitter;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        prepareActionBar();
        initializeViews();
        initializeListeners();
        createValidationForm();
    }

    private void initializeViews() {
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(passwordOnEditorActionListener);
        loginFormView = findViewById(R.id.login_form);
        loginStatusView = findViewById(R.id.login_status);
        loginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
        buttonGooglePlus = (Button) findViewById(R.id.buttonGooglePlus);
        buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
        buttonTwitter = (Button) findViewById(R.id.buttonTwitter);
        signInButton = (Button) findViewById(R.id.sign_in_button);
    }

    private void initializeListeners() {
        buttonGooglePlus.setOnClickListener(this);
        buttonFacebook.setOnClickListener(this);
        buttonTwitter.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    private void createValidationForm() {
        Validate emailField = new Validate(emailView);
        Validate passwordField = new Validate(passwordView);

        emailField.addValidator(new NotEmptyValidator(LoginActivity.this));
        emailField.addValidator(new EmailValidator(LoginActivity.this));
        passwordField.addValidator(new NotEmptyValidator(LoginActivity.this));

        validationForm = new Form();
        validationForm.addValidates(emailField);
        validationForm.addValidates(passwordField);
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String stringToken = Preferences.getAppToken(LoginActivity.this);

        //When we have token persisted just start FeedActivity
        if(stringToken != null && !TextUtils.isEmpty(stringToken)) {
            this.stringToken = stringToken;
            startFeedActivity();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonGooglePlus:
                //startFeedActivity();
                break;
            case R.id.buttonFacebook:
                //startFeedActivity();
                break;

            case R.id.buttonTwitter:
                //startFeedActivity();
                break;
            case R.id.sign_in_button:
                attemptLogin();
                break;
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login validationForm.
     * If there are validationForm errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        emailView.setError(null);
        passwordView.setError(null);

        if(validationForm.validate()) {
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();

            loginStatusMessageView.setText(R.string.login_progress_signing_in);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginStatusView.setVisibility(View.VISIBLE);
            loginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            loginFormView.setVisibility(View.VISIBLE);
            loginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
