package com.example.android.smsreport;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by User on 2016-09-12.
 */
public class ContactView extends LinearLayout {
    TextView ContactName;
    TextView ContactNumber;
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
    }

    public void setContactInfo(ContactData contactData) {
            this.ContactName.setText(contactData.getName());
            this.ContactNumber.setText(contactData.getPhonenum());
    }
}
