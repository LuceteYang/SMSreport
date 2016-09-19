package com.example.android.smsreport;

import io.realm.RealmObject;

/**
 * Created by User on 2016-09-18.
 */
public class ChoiceGroupid extends RealmObject {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
