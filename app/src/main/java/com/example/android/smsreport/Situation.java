package com.example.android.smsreport;

import io.realm.RealmObject;

/**
 * Created by User on 2016-09-08.
 */
public class Situation extends RealmObject {
    private int code;
    private String situation;
    private String situationmgs;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getSituationmgs() {
        return situationmgs;
    }

    public void setSituationmgs(String situationmgs) {
        this.situationmgs = situationmgs;
    }
}
