package edu.arizona.biosemantics.conflictsolver;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesManager {

    private static SharedPreferencesManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME  = "mysharedpref12";
    private static final String KEY_USERNAME      = "username";
    private static final String KEY_USER_EMAIL    = "email";
    private static final String KEY_USER_ID       = "expertId";


    private SharedPreferencesManager(Context context) {

        mCtx = context;
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {

        if (mInstance == null) {

            mInstance = new SharedPreferencesManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String email){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences.getString(KEY_USERNAME, null) != null){

            return true;
        }
        return false;
    }

    public boolean logout(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true; 
    }

    public int getExpertId(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, 0);

    }
    public String getUsername(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);

    }

    public String getUserEmail(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }
}
