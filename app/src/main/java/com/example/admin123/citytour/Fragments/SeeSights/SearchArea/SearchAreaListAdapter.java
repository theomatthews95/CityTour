package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.admin123.citytour.R;

import java.util.ArrayList;

/**
 * Created by theom on 04/03/2017.
 */

public class SearchAreaListAdapter extends BaseAdapter{
    ArrayList<SearchAreaItem> searchAreaItems = new ArrayList<SearchAreaItem>();

    public SearchAreaListAdapter(ArrayList<SearchAreaItem> resource) {
        searchAreaItems = resource;
    }

    @Override
    public int getCount() {
        return searchAreaItems.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_adapter_item, parent, false);
        } else {
            result = convertView;
        }

        ((TextView) result.findViewById(android.R.id.text1)).setText(getItem(position).getName());

        return result;
    }

    @Override
    public SearchAreaItem getItem(int position){
        return searchAreaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
