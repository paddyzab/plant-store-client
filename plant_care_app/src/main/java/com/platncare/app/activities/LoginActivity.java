package com.platncare.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import client.endpoint.TokenEndpoint;
import client.http.exception.HTTPClientException;
import com.platncare.app.R;
import com.platncare.app.backend.RequestTokenAsyncTask;
import com.platncare.app.backend.RequestTokenExecutor;
import com.platncare.app.utils.IntentKeys;
import com.platncare.app.utils.Preferences;
import model.Token;

import java.io.IOException;

public class LoginActivity extends Activity implements OnClickListener {

    private String email;
    private String password;

    private EditText emailView;
    private EditText passwordView;
    private View loginFormView;
    private View loginStatusView;
    private TextView loginStatusMessageView;
    private Token token;

    private final String emailKey = "com.plantcare.app.email";
    private final String passwordKey = "com.plantcare.app.password";
    private SharedPreferences sharedPreferences;
    private RequestTokenAsyncTask requestTokenAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        prepareActionBar();

        emailView = (EditText) findViewById(R.id.email);
        emailView.setText(email);

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(passwordOnEditorActionListener);

        loginFormView = findViewById(R.id.login_form);
        loginStatusView = findViewById(R.id.login_status);
        loginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        Button buttonGooglePlus = (Button) findViewById(R.id.buttonGooglePlus);
        Button buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
        Button buttonTwitter = (Button) findViewById(R.id.buttonTwitter);

        buttonGooglePlus.setOnClickListener(this);
        buttonFacebook.setOnClickListener(this);
        buttonTwitter.setOnClickListener(this);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        requestTokenAsyncTask = new RequestTokenAsyncTask(executor);
        sharedPreferences = getPreferences(MODE_PRIVATE);
    }

    private RequestTokenExecutor executor = new RequestTokenExecutor() {

        @Override
        public void onSuccess(Token token) {
            LoginActivity.this.token = token;
            persistCredentials();
            startFeedActivity();
        }

        @Override
        public void onFailure(Exception e) {
            passwordView.setError(getString(R.string.error_incorrect_password));
            passwordView.requestFocus();

            Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    protected void onPause() {
        super.onPause();

        if(requestTokenAsyncTask != null) {
            requestTokenAsyncTask.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sharedPreferences.contains(emailKey)) {
            email = sharedPreferences.getString(emailKey, "default");
        }
        if(sharedPreferences.contains(passwordKey)) {
            password = sharedPreferences.getString(passwordKey, "default");
        }

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);

            requestTokenAsyncTask.execute(email, password);
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
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        emailView.setError(null);
        passwordView.setError(null);

        if(emailView.getText() != null) {
            email = emailView.getText().toString();
        }
        if(passwordView.getText() != null) {
            password = passwordView.getText().toString();
        }

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (password.length() < 4) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!email.contains("@")) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            loginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            requestTokenAsyncTask.execute(email, password);
        }
    }

    private void prepareActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
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

    private void persistCredentials() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(emailKey, email);
        editor.putString(passwordKey, password);

        editor.commit();
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

    private void startFeedActivity() {
        //Start FeedActivity after successful Login
        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
        intent.putExtra(IntentKeys.TOKEN_KEY, token);
        startActivity(intent);

        //Destroy this activity to remove it from the activity stack
        LoginActivity.this.finish();
    }
}
