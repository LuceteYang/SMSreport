package com.example.android.smsreport;

/**
 * Created by User on 2016-09-09.
 */
public class ContactData {
    String groupname;
    String phonenum;
    String name;
    String id;
    boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getPhonenum() {
        return phonenum;
    }
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}
