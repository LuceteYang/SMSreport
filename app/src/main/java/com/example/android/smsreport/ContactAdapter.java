package com.example.android.smsreport;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by User on 2016-09-12.
 */
public class ContactAdapter  extends BasicAdapter<ContactData> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactView view;

        if (convertView == null) {
            view = new ContactView(parent.getContext());
        } else {
            view = (ContactView) convertView;
        }
        view.setContactInfo(items.get(position));
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
