package com.platncare.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import client.endpoint.TokenEndpoint;
import client.http.exception.HTTPClientException;
import com.platncare.app.R;
import model.Token;

import java.io.IOException;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well. Supports Facebook, Google Plus and Twitter login.
 */
public class LoginActivity extends Activity implements OnClickListener {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String email;
    private String password;

    // UI references.
    private EditText emailView;
    private EditText passwordView;
    private View loginFormView;
    private View loginStatusView;
    private TextView loginStatusMessageView;
    private Button buttonGooglePlus;
    private Button buttonFacebook;
    private Button buttonTwitter;
    private Token token;

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

        buttonGooglePlus = (Button) findViewById(R.id.buttonGooglePlus);
        buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
        buttonTwitter = (Button) findViewById(R.id.buttonTwitter);

        buttonGooglePlus.setOnClickListener(this);
        buttonFacebook.setOnClickListener(this);
        buttonTwitter.setOnClickListener(this);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //TODO: remove it later, for testing we need it.
        populateWithTestData();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonGooglePlus:
                startFeedActivity();
                break;
            case R.id.buttonFacebook:
                startFeedActivity();
                break;

            case R.id.buttonTwitter:
                startFeedActivity();
                break;
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        email = emailView.getText().toString();
        password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (password.length() < 4) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
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




            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
        }
    }

    private void populateWithTestData() {
        emailView.setText("patryk.zabicki@gmail.com");
        passwordView.setText("wiosna12");
    }

    private void prepareActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
    }

    private void requestToken() {
        try {
            token = new TokenEndpoint().getToken(email, password);
            Log.d(LOG_TAG, String.format("token is: %s", token.getToken()));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (HTTPClientException e) {
            e.printStackTrace();
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

    //TODO: move AsyncTasks to separate package
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
                requestToken();
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startFeedActivity();
            } else {
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
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

    private void startFeedActivity() {
        //Start FeedActivity after successful Login
        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);

        //Destroy this activity to remove it from the activity stack
        LoginActivity.this.finish();
    }
}
