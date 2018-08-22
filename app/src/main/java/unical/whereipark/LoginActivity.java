package unical.whereipark;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import unical.whereipark.utility.MessagesHendler;
import unical.whereipark.utility.RequestHendler;
import unical.whereipark.utility.User;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    //////////////////GOOGLE LOGIN//////////////////////////////
    GoogleSignInClient gsiClient;
    SignInButton loginB;
    //////////////////FACEBOOK LOGIN///////////////////////////
//    LoginButton loginButtonFB;
//    private CallbackManager callbackManager;

    private  Button register;
    int SIGN_IN_ACTIVITY = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mayRequestContacts();
        //////////////////////////GOOGLE LOGIN /////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        gsiClient = GoogleSignIn.getClient(this, gso);
        loginB = (SignInButton) findViewById(R.id.sign_in_button);
        setGoogleButtonText(loginB,"Google");
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("non null","ok1");
                startActivityForResult(gsiClient.getSignInIntent(), SIGN_IN_ACTIVITY);
            }
        });
        //////////////////////////////////////////////////////////

        ////////////////////////////FACEBOOK LOGIN////////////////////////////
//        loginButtonFB=(LoginButton)findViewById(R.id.login_button);
//        setFacebookButtonText(loginButtonFB,"Facebook");
//        callbackManager = CallbackManager.Factory.create();
//        loginButtonFB.setReadPermissions(getReadPermissions());
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        // Callback registration
//        loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override public void onSuccess(LoginResult loginResult) {
//
////                AccessToken accessToken = loginResult.getAccessToken();
//                //TODO delete code repetition in splash activity
//                Profile profile = Profile.getCurrentProfile();
//                String name=profile.getName();
//                // App code
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("LoginActivity", response.toString());
//
//                                // Application code
//                                try {
//                                    String id=object.getString("id");
//                                    String email = object.getString("email");
//                                    Log.e("DAFB",id+"-"+email+"-"+name);
//                                    (new CheckSocialLoginAsyncTask(User.FB_ACCOUNT,id,email,name,LoginActivity.this)).execute();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,email");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override public void onCancel() {
//                Toast.makeText(getApplicationContext(), "Login cancelled!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override public void onError(FacebookException exception) {
//                Toast.makeText(getApplicationContext(), "Error: "+exception.getLocalizedMessage()+"!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //////////////////////////////////////////////////////////////////////////////

        register=(Button)findViewById(R.id.register_button);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        // TODO populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    ///////////////////////////////////GOOGLE LOGIN///////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_ACTIVITY) {
            Toast.makeText(this, "Signin activity competed", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.e("non null","ok2");
            handleSignedInAccountResult(task);
        }
//        else{
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//
//        }
    }

    private void handleSignedInAccountResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.e("non null","ok3");
            if (account != null) {
                Log.e("non null","ok");
                (new CheckSocialLoginAsyncTask(User.GOOGLE_ACCOUNT,account.getId(),account.getEmail(),account.getDisplayName(),this)).execute();
//account.getPhotoUrl();
            }
//            updateUI();
        } catch (ApiException e) {
            Log.w("SIGNIN", "getSignInAccount failed with code:" + e.getClass());
        }

    }
    //////////////////////////////////////////////////////////////

    private List<String> getReadPermissions(){
        List<String> readPermissions = new ArrayList<>();
        readPermissions.add("email");
        return readPermissions;
    }

    private void openFacebookInfo(){
        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateUI() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String name = account.getDisplayName();
            String email = account.getEmail();
            String id = account.getId();
            Toast.makeText(this, "name:" + name + "\nemail:" + email + "\nid:" + id, Toast.LENGTH_SHORT).show();

//            statusTV.setText(id+"\n"+name+"\n"+email);
            loginB.setEnabled(false);
//            logoutB.setEnabled(true);
        } else {
//            statusTV.setText("Anonymus");
            loginB.setEnabled(true);
//            logoutB.setEnabled(false);
        }

    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO Read the contact and Populate the email camp
            }
        }
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
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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

    private boolean isEmailValid(String email) {
        return Pattern.matches("^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$", email);

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    private void setGoogleButtonText(SignInButton signInButton, String buttonText) {
        // Search all the views inside SignInButton for TextView
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            // if the view is instance of TextView then change the text SignInButton
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
    private void setFacebookButtonText(LoginButton signInButton, String buttonText) {
        signInButton.setText(buttonText);
//        // Search all the views inside SignInButton for TextView
//        for (int i = 0; i < signInButton.getChildCount(); i++) {
//            View v = signInButton.getChildAt(i);
//
//            // if the view is instance of TextView then change the text SignInButton
//            if (v instanceof TextView) {
//                TextView tv = (TextView) v;
//                tv.setText(buttonText);
//                return;
//            }
//        }
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        //TODO fare get user name dal db;
        private String message;
        HashMap<String, String> credenziali;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            credenziali = new HashMap<>();
            credenziali.put("email", mEmail);
            credenziali.put("password", mPassword);
        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject jsonObject = new JSONObject(credenziali);
            String result = RequestHendler.getInstance().makeRequest(RequestHendler.LOGIN_REQUEST, jsonObject);
            message = result;
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            mAuthTask = null;
            showProgress(false);

            if (!result.equals(MessagesHendler.ERROR_INCORRECT_LOGIN)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String userName = jsonObject.getString("userName");
                    int isSeller = jsonObject.getInt("isSeller");
                    User.init(mEmail, userName, User.APP_ACCOUNT, null);
                    message = "login ok";
                    Log.e("login", "ok");

                    startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                    CheckBox ch = (CheckBox) findViewById(R.id.ch_rememberme);
                    if (ch.isChecked())
                        getSharedPreferences(User.STORE_USER_INFO, MODE_PRIVATE)
                                .edit()
                                .putString(User.STORE_EMAIL, mEmail)
                                .putString(User.STORE_USER_NAME, userName)
                                .commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("login", "!ok");
                mPasswordView.setError(message);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

