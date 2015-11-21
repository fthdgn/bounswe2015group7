package tr.edu.boun.cmpe.sculture;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static tr.edu.boun.cmpe.sculture.Constants.*;

public class BaseApplication extends Application {
    public static BaseApplication baseApplication;

    public RequestQueue mRequestQueue;

    private String TOKEN = null;
    private String USERNAME = "";
    private String EMAIL = "";

    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        autoLogin();
    }

    private void autoLogin() {
        if (TOKEN == null) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String email = settings.getString(PREF_EMAIL, "");
            String username = settings.getString(PREF_USERNAME, "");
            String token = settings.getString(PREF_ACCESS_TOKEN, "");
            if (email.equals("") || token.equals("") || username.equals(""))
                Toast.makeText(getApplicationContext(), getString(R.string.loginAdvice), Toast.LENGTH_LONG).show();
            else {
                setUserInfo(token, username, email);
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.userLogin) + USERNAME, Toast.LENGTH_SHORT).show();
        }
    }

    public void setUserInfo(String token, String username, String email) {
        this.TOKEN = token;
        this.USERNAME = username;
        this.EMAIL = email;
        this.isLoggedIn = true;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_EMAIL, EMAIL);
        editor.putString(PREF_ACCESS_TOKEN, TOKEN);
        editor.putString(PREF_USERNAME, USERNAME);
        editor.apply();

    }

    public boolean checkLogin() {
        return isLoggedIn;
    }

    public void logOut() {
        this.TOKEN = "";
        this.USERNAME = "";
        this.EMAIL = "";
        this.isLoggedIn = false;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_EMAIL, "");
        editor.putString(PREF_ACCESS_TOKEN, "");
        editor.putString(PREF_USERNAME, "");
        editor.apply();
    }


}
