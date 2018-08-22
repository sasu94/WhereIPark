package unical.whereipark;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import unical.whereipark.utility.User;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUser();
//        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//            @Override
//            public void run() {
//                initUser();
//            }
//        }, SPLASH_TIME_OUT);


    }

    public void initUser() {
        SharedPreferences pref = getSharedPreferences(User.STORE_USER_INFO, MODE_PRIVATE);
        String email = pref.getString(User.STORE_EMAIL, null);
        String userName = pref.getString(User.STORE_USER_NAME, null);
        if (email != null || userName != null) {
                User.init(email, userName, User.APP_ACCOUNT, null);
                startActivity(new Intent(this, Main2Activity.class));

//            (new LoginAsyncTask(email,password,this)).execute();
        } else {
//            GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestProfile()
//                    .requestEmail()
//                    .build();
//
//            // Build a GoogleSignInClient with the options specified by gso.
//            GoogleSignInClient gsiClient= GoogleSignIn.getClient(this,gso);
//            gsiClient.signOut();




            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if (account != null) {
//                (new CheckSocialLoginAsyncTask(User.GOOGLE_ACCOUNT,account.getId(),account.getEmail(),account.getDisplayName(),this)).execute();
                User.init(account.getEmail(), account.getDisplayName(), User.GOOGLE_ACCOUNT, null);
                User.getInstance().setSocialID(account.getId());
                startActivity(new Intent(this, Main2Activity.class));

            } else {
//                AccessToken accessToken=AccessToken.getCurrentAccessToken();
//                if ( accessToken!= null) {
//                    Profile profile = Profile.getCurrentProfile();
//                    final String name=profile.getName();
//                    // App code
//                    GraphRequest request = GraphRequest.newMeRequest(
//                            accessToken,
//                            new GraphRequest.GraphJSONObjectCallback() {
//                                @Override
//                                public void onCompleted(JSONObject object, GraphResponse response) {
//                                    Log.v("LoginActivity", response.toString());
//
//                                    // Application code
//                                    try {
//                                        String id=object.getString("id");
//                                        String email = object.getString("email");
////                                        (new CheckSocialLoginAsyncTask(User.FB_ACCOUNT,id,email,name,SplashActivity.this)).execute();
//
//                                        User.init(email, name, User.FB_ACCOUNT, null);
//                                        User.getInstance().setSocialID(id);
//                                        startActivity(new Intent(SplashActivity.this, Main2Activity.class));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                    Bundle parameters = new Bundle();
//                    parameters.putString("fields", "id,email");
//                    request.setParameters(parameters);
//                    request.executeAsync();
//                } else
                    startActivity(new Intent(this, LoginActivity.class));
            }
        }
        finish();
    }
}
