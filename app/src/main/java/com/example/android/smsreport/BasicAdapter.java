package com.example.android.smsreport;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-09-09.
 */
public abstract class BasicAdapter<T> extends BaseAdapter {
    protected List<T> items = new ArrayList<>();

    public void add( T t){
        items.add(t);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}