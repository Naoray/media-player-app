package de.codematch.naoray.media_player_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    protected SharedPreferences spref;
    protected Set<String> emailAutocompleteList;
    protected SharedPreferences.Editor editor;
    protected DatabaseManager db;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox mKeepMeLoggedInCheckBox;
    private Boolean keepMeLoggedInChecked = false;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getAppPreferences();

        if (this.checkLoginState()) {
            this.login();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        //setErrorEnabled(true) makes sure that there is space to be able to show an error below the EditText field without having to change the layout
        textInputLayoutEmail.setErrorEnabled(true);
        textInputLayoutPassword.setErrorEnabled(true);

        // calling DatabaseManager to Init DB
        db = new DatabaseManager(this);

        mEmailView.setText(spref.getString(getString(R.string.e_mail_address_preferences_key), ""));
        if (!mEmailView.getText().toString().equals("")) {
            mPasswordView.requestFocus();
        }
        mKeepMeLoggedInCheckBox = (CheckBox) findViewById(R.id.stay_logged);
        //restores the status of the keepmeloggedin checkbox
        if (spref.getBoolean("KeepMeLoggedIn", false)) {
            mKeepMeLoggedInCheckBox.setChecked(true);
        }
        //reacts to the changes of the keepmeloggedin checkbox
        mKeepMeLoggedInCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                keepMeLoggedInChecked = isChecked;
                editor.putBoolean("KeepMeLoggedIn", keepMeLoggedInChecked);
                editor.apply();
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        this.addEmailsToAutoComplete();


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        textInputLayoutEmail.setError(null);
        textInputLayoutPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            textInputLayoutEmail.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            textInputLayoutEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            textInputLayoutPassword.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
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
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Checks if the inserted email is Valid
     *
     * @param email
     * @return true if email is valid
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        final String EMAIL_PATTERN = "[a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        /** On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
         * for very easy animations. If available, use these APIs to fade-in
         * the progress spinner.
         */
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
     * Delivers the SharedPreferences and sets the Editor for it
     */
    private void getAppPreferences() {
        spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = spref.edit();
    }

    /**
     * Adds the history-list of used e-mails to the dropdown menu for the e-mail field
     */
    private void addEmailsToAutoComplete() {
        emailAutocompleteList = spref.getStringSet("emailAutocompleteList", new HashSet<String>());
        ArrayList<String> newList = new ArrayList<>(emailAutocompleteList);
        if (!newList.isEmpty()) {
            //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(LoginActivity.this,
                            android.R.layout.simple_dropdown_item_1line, newList);

            mEmailView.setAdapter(adapter);
        }
    }

    /**
     * starts the mainmenu-activity
     */
    protected void login() {
        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
        //Shows a TOAST to welcome the current user and wishes him fun with the app
        String usernamePreferencesKey = getString(R.string.e_mail_address_preferences_key);
        String currentUsername = spref.getString(usernamePreferencesKey, "");

        String wishText = getString(R.string.wish_text);
        Toast.makeText(LoginActivity.this, getString(R.string.welcome_text) + ", " + currentUsername + "!" + "\n" + wishText, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return the current login-state
     */
    private Boolean checkLoginState() {

        return spref.getBoolean("LoginState", false);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        //Boolean variable for checking if a server response arrived
        public boolean responsebool = false;
        private String encryptedEmail;
        private String encryptedPassword;
        private Boolean verified = false;

        UserLoginTask(String email, String password) {
            mEmail = email.toLowerCase();
            mPassword = password;
        }

        /**
         * @return true if internet connection exists
         */
        private Boolean checkInternetConnection() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }

        /**
         * Does an asynchronous Task and triggers the server communication or uses the local database
         *
         * @param params provided from AsyncTask
         * @return true if authentication is successful
         */
        @Override
        protected Boolean doInBackground(Void... params) {

            // encrypt e-mail and password
            try {
                encryptedEmail = AES.encrypt(mEmail);
                encryptedPassword = AES.encrypt(mPassword);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // if internetConnection exists -> communicate with server + update local database if necessary (new User or new Password)
            if (checkInternetConnection()) {
                try {
                    // Volley-Code
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("Username", encryptedEmail);
                    parameters.put("pw", encryptedPassword);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    String url = ""; // an url for database connection should be inserted


                    LoginRequester jsObjRequest = new LoginRequester(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                        /**
                         * is executed when server responds
                         *
                         * @param response from Server
                         */
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("Response: ", response.toString());
                                responsebool = true;
                                verified = response.getBoolean("response");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        /**
                         * Logs error Message
                         *
                         * @param response from server
                         */
                        @Override
                        public void onErrorResponse(VolleyError response) {
                            Log.d("Response: ", response.toString());
                        }
                    });

                    // Add request to the queue
                    queue.add(jsObjRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Volley Code Ende
                //This makes sure that the server has enough time to respond and that the loading animation can be shown for a necessary time
                try {
                    for (int i = 0; i < 30; i++) {
                        if (responsebool) {
                            break;
                        }
                        Thread.sleep(100);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (verified) {
                    db.handleUserInput(encryptedEmail, encryptedPassword);
                    //if the sever does not respond in the expected time, the app checks the user data with the local database
                } else if (!responsebool) {
                    verified = db.verifyPassword(encryptedEmail, encryptedPassword);
                }
            }
            //no internetConnection exists -> use local database to verify e-mail and password
            else {
                verified = db.verifyPassword(encryptedEmail, encryptedPassword);
                // This makes sure that the loading animation is shown for a certain time
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return verified;
        }

        /**
         * Gets Triggered after doInBackground is finished
         *
         * @param success true if authentication is successful
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false) has to be called here already, otherwise the requestFocus-method in else-path doesn't always work, because it collides with the running progressBar
            showProgress(false);
            if (success) {
                editor.putString(getString(R.string.e_mail_address_preferences_key), mEmail);
                editor.apply();

                finish();
                this.addEmailToAutocompleteList();
                if (keepMeLoggedInChecked) {
                    editor.putBoolean("LoginState", true);
                    editor.apply();
                }
                login();
            } else {
                textInputLayoutPassword.setError(getString(R.string.error_incorrect_password));
                textInputLayoutPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        /**
         * Adds the new verified e-mail address to the List of used e-mails (history)
         */
        protected void addEmailToAutocompleteList() {

            emailAutocompleteList.add(mEmail);
            editor.putStringSet("emailAutocompleteList", emailAutocompleteList);
            editor.apply();
        }
    }
}

