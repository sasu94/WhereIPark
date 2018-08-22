package unical.whereipark.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Elena Mastria on 21/08/2018.
 */
public class User {

    //SHARED PREFERENCE
    public static final String STORE_USER_INFO = "userInfo";//RICONOSCE LA SHARED PREFERENCE
    public static final String STORE_EMAIL = "email";
    public static final String STORE_USER_NAME = "username";
    //TYPE ACCOUNT
    public static final String APP_ACCOUNT = "app_account";
    public static final String GOOGLE_ACCOUNT = "google";
    public static final String FB_ACCOUNT = "facebook";

    private static String emailUser;
    private static String accountType;
    private static String userName;
    private static User user;
    private static Image imageUser;
    private static boolean isAlredyInitialized = false;
    private static String socialID;
    public static void init(String emailUser, String userName, String accountType, Image imageUser) {
        if (!User.isAlredyInitialized) {
            User.emailUser = emailUser;
            User.userName = userName;
            User.imageUser = imageUser;
            User.accountType = accountType;
            User.isAlredyInitialized = true;
        } else throw new RuntimeException("User already initialized");}

    public static User getInstance() {
        if (user == null) {
            return user = new User();
        } else return user;
    }

    private User() {
    }




    public String getEmailUser() {
        return emailUser;
    }

    public String getUserName() {
        return userName;
    }

    public Image getImageUser() {
        return imageUser;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setSocialID(String socialID) {
        if (!accountType.equals(User.GOOGLE_ACCOUNT)&&!accountType.equals(User.FB_ACCOUNT)) {
            throw new RuntimeException("User is not logged with social");
        }
        User.socialID = socialID;
    }

    public static void logout(Context c) {
        SharedPreferences pref = c.getSharedPreferences(User.STORE_USER_INFO, MODE_PRIVATE);
        String email = pref.getString(User.STORE_EMAIL, null);
        String userName = pref.getString(User.STORE_USER_NAME, null);
        if (email != null || userName != null) {
            pref.edit().remove(STORE_EMAIL).remove(STORE_USER_NAME).commit();
        }else{
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(c);
            if(account!=null){
                GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestProfile()
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                GoogleSignInClient gsiClient= GoogleSignIn.getClient(c,gso);
                gsiClient.signOut();

            }
            else if(AccessToken.getCurrentAccessToken()!=null){
                LoginManager.getInstance().logOut();
            }

        }
        clearUser();
    }
    private  static void clearUser(){
        User.isAlredyInitialized=false;
        User.user=null;
        User.socialID =null;
    }
}
