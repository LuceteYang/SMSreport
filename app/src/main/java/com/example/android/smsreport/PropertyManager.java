package com.example.android.smsreport;

/**
 * Created by User on 2016-09-05.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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

    public static final String SITUATION = "0";
    public static final String[] SITUATIONMSG = new String[] {"내용 입력","내용 입력","내용 입력","내용 입력"};

    public String getSituation() {
        return mPrefs.getString(SITUATION,"0");
    }

    public void setSituation(String code) {
        mEditor.putString(SITUATION, code);
        mEditor.commit();
    }

    public String getSituationmsg(int index){
        Log.i("getSituationmsg########",String.valueOf(index));
        return mPrefs.getString(SITUATIONMSG[index],"내용 입력");
    }

    public void setSituationmsg(int index, String text) {
        Log.i("msgSituationmsg########",String.valueOf(index)+text);
        mEditor.putString(SITUATIONMSG[index], text);
        mEditor.commit();
    }


}
