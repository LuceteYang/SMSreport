package com.example.android.smsreport;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by User on 2016-09-12.
 */
public class ContactView extends LinearLayout{
    TextView ContactName;
    TextView ContactNumber;
    TextView ContactGroup;

    public ContactView(Context context) {
        super(context);
        init();
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_contact, this);
        this.ContactName = (TextView)findViewById(R.id.tv_contact_name);
        this.ContactNumber = (TextView)findViewById(R.id.tv_contact_number);
        this.ContactGroup = (TextView)findViewById(R.id.tv_group_name);
    }

    public void setContactInfo(ContactData contactData) {
        this.ContactName.setText(contactData.getName());
        this.ContactNumber.setText(contactData.getPhonenum());
        this.ContactGroup.setText(contactData.getGroupname());
//        if (isChecked) {
//            ContactName.setBackgroundColor(0xFF00FF00);
//        }
    }

}
