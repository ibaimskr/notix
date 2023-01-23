package com.example.notix.Network.User;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context applicationContext) {
        this.context = applicationContext;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean saveStringData(String key, String value) {
        SharedPreferences.Editor editor=pref.edit ();
        editor.putString (key, value);
        return editor.commit ();
    }

    public String getStringData(String key) {
        String value=pref.getString (key, "");
        return value;
    }
}
