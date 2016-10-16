package com.example.android.smsreport;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.internal.Group;

/**
 * Created by User on 2016-09-09.
 */
public class GroupAdapter extends BaseAdapter {

    protected List<GroupInfo> items = new ArrayList<>();
    List<Boolean> alCheck;
    public void add( GroupInfo t){
        items.add(t);
        notifyDataSetChanged();
    }

    public GroupAdapter(List<Boolean> alCheck) {
        this.alCheck = alCheck;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public GroupInfo getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupView view;

        if (convertView == null) {
            view = new GroupView(parent.getContext());
        } else {
            view = (GroupView) convertView;
        }
        view.setGroupInfo(items.get(position));
        view.setBackgroundResource(alCheck.get(position) ? R.color.clicked : R.color.white);
        return view;
    }

    public ArrayList<ChoiceGroupid> returnChoiceGroup(){
        ArrayList<ChoiceGroupid> mArrayList = new ArrayList();
        for(int i=0;i<items.size();i++){
            if(alCheck.get(i)){
                ChoiceGroupid a = new ChoiceGroupid();
                a.setCode(items.get(i).getId());
                a.setGroupname(items.get(i).getTitle());
//                Log.i(items.get(i).getTitle(),String.valueOf(items.get(i).isChecked()));
                mArrayList.add(a);
            }
        }
        return mArrayList;
    }



}
