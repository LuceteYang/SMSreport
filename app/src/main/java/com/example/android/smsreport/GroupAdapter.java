package com.example.android.smsreport;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 2016-09-09.
 */
public class GroupAdapter extends BasicAdapter<GroupInfo> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupView view;

        if (convertView == null) {
            view = new GroupView(parent.getContext());
        } else {
            view = (GroupView) convertView;
        }
        view.setGroupInfo(items.get(position));
        return view;
    }
}
