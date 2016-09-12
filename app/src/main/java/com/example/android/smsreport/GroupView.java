package com.example.android.smsreport;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by User on 2016-09-09.
 */
public class GroupView extends LinearLayout {
    TextView GroupName;
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
        this.GroupName = (TextView)findViewById(R.id.tv_groupname);
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        if(groupInfo.getId().equals("all")){
        }else{
            this.GroupName.setText(groupInfo.getTitle()+"("+groupInfo.getCount()+")");
        }
    }
}
