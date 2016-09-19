package com.example.android.smsreport;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
        //이건 미스테리 에러
        if(position==0){
            view.setChecked();
            view.setChecked();
        }
        return view;
    }

    public ArrayList<String> returnChoiceGroup(){
        ArrayList<String> mArrayList = new ArrayList();
        for(int i=0;i<items.size();i++){
            if(items.get(i).isChecked()){
//                Log.i(items.get(i).getTitle(),String.valueOf(items.get(i).isChecked()));
                mArrayList.add(items.get(i).getId());
            }
        }
        return mArrayList;
    }



}
