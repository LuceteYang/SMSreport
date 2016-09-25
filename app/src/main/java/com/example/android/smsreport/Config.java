package com.example.android.smsreport;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.RealmResults;

/**
 * Created by User on 2016-09-18.
 */
public class Config {
    public static boolean checkGroupArray(RealmResults<ChoiceGroupid> list, String a){
        for(ChoiceGroupid str: list) {
            if(str.getCode().equals(a))
                return true;
        }
        return false;
    }

    public static boolean checkContactArray(RealmResults<ChoiceContactid> list, String a){
        for(ChoiceContactid str: list) {
            if(str.getId().equals(a))
                return true;
        }
        return false;
    }
}
