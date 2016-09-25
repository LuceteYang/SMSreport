package com.example.android.smsreport;

import io.realm.RealmObject;

/**
 * Created by User on 2016-09-25.
 */
public class ChoiceContactid  extends RealmObject {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
