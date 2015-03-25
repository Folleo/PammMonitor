package ru.pamm_trend.fxmonitor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


/**
 * A login screen that offers login via API key.
 */
public class LoginActivity extends Activity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();

    private static final String HOST_NAME = "api.fx-trend.com";
    private static final String URL = "http://api.fx-trend.com";
    //private static final String USER_NAME_CONST = "folleo";
    //private static final String PASSWORD_CONST = "bb95038342090b2f3626f9914e26b1c8";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mApiKeyView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.login);

        mApiKeyView = (EditText) findViewById(R.id.api_key);
        mApiKeyView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginSignInButton = (Button) findViewById(R.id.login_sign_in_button);
        mLoginSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid login, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);
        mApiKeyView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String apiKey = mApiKeyView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid API key.
        if (TextUtils.isEmpty(apiKey)){
            mApiKeyView.setError(getString(R.string.error_field_required));
            focusView = mApiKeyView;
            cancel = true;
        } else if (!isApiKeyValid(apiKey)) {
            mApiKeyView.setError(getString(R.string.error_invalid_api_key));
            focusView = mApiKeyView;
            cancel = true;
        }

        // Check for a valid login.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(login, apiKey);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isLoginValid(String login) {
        return login.length() > 4;
    }

    private boolean isApiKeyValid(String apiKey) {
        // Note: Actually API key's length is 32
        return apiKey.length() > 30;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mLogin;
        private final String mApiKey;

        UserLoginTask(String login, String apiKey) {
            mLogin = login;
            mApiKey = apiKey;
        }

        /**
         * Connect to api.fx-trend.com server and try to authenticate
         * with login and infoAPI key using Basic HTTP authorization.
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean authStatus = false;

            DefaultHttpClient httpclient = new DefaultHttpClient();
            String credentials = mLogin + ":" + mApiKey;
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            try {
                HttpGet httpGet = new HttpGet(URL + "/info/get_user_investors_info");
                httpGet.setHeader("Authorization", "Basic " + base64EncodedCredentials);
                httpGet.setHeader("Accept", "text/json");

                Log.d(LOG_TAG, "Executing request: " + httpGet.getRequestLine());
                HttpResponse response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                Log.d(LOG_TAG, "----------------------------------------\n" + response.getStatusLine());
                if (entity != null) {
                    Log.d(LOG_TAG, "Response content length: " + entity.getContentLength());
                    Log.d(LOG_TAG, EntityUtils.toString(entity));

                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == HttpStatus.SC_OK) {
                        authStatus = true;
                    }
                }

            } catch(Exception e){
                authStatus = false;
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                // When HttpClient instance is no longer needed,
                // shut down the connection manager
                httpclient.getConnectionManager().shutdown();
            }
            return authStatus;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                // Since API key is not a password, we can store it in SharedPreferences
                PrefUtils.saveToPrefs(getBaseContext(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, mLogin);
                PrefUtils.saveToPrefs(getBaseContext(), PrefUtils.PREFS_LOGIN_API_KEY, mApiKey);
                finish();
            } else {
                mApiKeyView.setError(getString(R.string.error_incorrect_api_key));
                mApiKeyView.requestFocus();
                PrefUtils.saveToPrefs(getApplicationContext(), PrefUtils.PREFS_LOGIN_API_KEY, null);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



