package com.example.android.smsreport;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by User on 2016-09-09.
 */

public class GroupView extends LinearLayout {
    TextView GroupName;
    boolean isChecked = false;

    public GroupView(Context context) {
        super(context);
        init();
    }

    public GroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_group, this);
        this.GroupName = (TextView) findViewById(R.id.tv_groupname);
    }

    public void setGroupInfo(GroupInfo groupinfo) {
        isChecked = groupinfo.isChecked();
        if (groupinfo.getId().equals("all")) {
            GroupName.setText(groupinfo.getTitle());
        } else {
            GroupName.setText(groupinfo.getTitle() + "(" + groupinfo.getCount() + ")");
        }
        if (isChecked) {
            GroupName.setBackgroundColor(0xFF00FF00);
        }
    }

    public void setChecked() {
        isChecked = !isChecked;
        if (isChecked) {
            GroupName.setBackgroundColor(0xFF00FF00);
        } else {
            GroupName.setBackgroundColor(Color.WHITE);
        }
    }
}

