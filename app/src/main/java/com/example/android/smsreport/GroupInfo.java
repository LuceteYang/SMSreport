package com.example.android.smsreport;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by User on 2016-09-09.
 */
public class GroupInfo {
    String id;
    String title;
    int count;
    List<ContactData> members = new ArrayList<ContactData>();

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setMembers(List<ContactData> members){
    this.members = members;
    }
    public List showMember(){
        return this.members;
    }
}
