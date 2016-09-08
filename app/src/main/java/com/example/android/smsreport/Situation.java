package com.example.android.smsreport;

import io.realm.RealmObject;

/**
 * Created by User on 2016-09-08.
 */
public class Situation extends RealmObject {
    private String situation="상황을 선택해 주세요.";
    private String situationmgs="내용 입력";

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
