package com.example.android.smsreport;

/**
 * Created by User on 2016-09-05.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dongja94 on 2016-04-28.
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        Context context = MyApplication.getContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public String getEmail() {
        return mPrefs.getString(KEY_EMAIL, "");
    }

    public void setEmail(String email) {
        mEditor.putString(KEY_EMAIL, email);
        mEditor.commit();
    }

    public String getPassword() {
        return mPrefs.getString(KEY_PASSWORD, "");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();
    }
}
