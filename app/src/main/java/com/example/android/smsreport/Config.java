package com.example.android.smsreport;

import java.util.AbstractList;
import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by User on 2016-09-18.
 */
public class Config {
    public static boolean checkArray(RealmResults<ChoiceGroupid> list, String a){
        for(ChoiceGroupid str: list) {
            if(str.getCode().equals(a))
                return true;
        }
        return false;
    }
}
