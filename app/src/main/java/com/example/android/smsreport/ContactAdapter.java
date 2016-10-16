package com.example.android.smsreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-09-12.
 */
public class ContactAdapter  extends BaseAdapter {
    protected List<ContactData> items = new ArrayList<>();
    List<Boolean> alCheck;
    boolean type;   //true Main, false:Group
    public void add( ContactData t){
        items.add(t);
        notifyDataSetChanged();
    }

    public ContactAdapter(List<Boolean> alCheck,boolean type) {
        this.alCheck = alCheck;
        this.type = type;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ContactData getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactView view;

        if (convertView == null) {
            view = new ContactView(parent.getContext());
        } else {
            view = (ContactView) convertView;
        }
        view.setContactInfo(items.get(position));
        if(type){
            view.setBackgroundResource(alCheck.get(position) ? R.color.clicked : R.color.white);
        }
        return view;
    }
    public void addlist(List list){
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void removelist(List list){
        items.removeAll(list);
        notifyDataSetChanged();
    }
}
