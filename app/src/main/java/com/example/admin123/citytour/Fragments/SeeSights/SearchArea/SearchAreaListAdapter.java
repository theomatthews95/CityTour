package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.admin123.citytour.R;

import java.util.ArrayList;

/**
 * Created by theom on 04/03/2017.
 */

public class SearchAreaListAdapter {
    /*ArrayList<SearchAreaItem> searchAreaItems = new ArrayList<SearchAreaItem>();
    Context context;

    public SearchAreaListAdapter(Context context, ArrayList<SearchAreaItem> resource) {
        super(context, R.layout.location_area_item,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.searchAreaItems = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.location_area_item, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        //Listener to check for when user makes a change to the checkbox
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //method used to change the state of the checkbox
                setChecked(position);
            }
        });

        name.setText(searchAreaItems.get(position).getName());
        if(searchAreaItems.get(position).isChecked() == true) {
            cb.setChecked(true);
        }else {
            cb.setChecked(false);
        }


        return convertView;
    }

   *//* @Override
    public SearchAreaItem getItem(int position){
        return searchAreaItems.get(position);
    }
*//*
    public ArrayList<SearchAreaItem> getArray(){
        return searchAreaItems;
    }

    //return the area locations that are
    public ArrayList<SearchAreaItem> getCheckedItems(){
        ArrayList<SearchAreaItem> checkedSearchItems = new ArrayList<SearchAreaItem>();
        for (int i=0;i<searchAreaItems.size();i++){
            if (searchAreaItems.get(i).isChecked() == true) {
                checkedSearchItems.add(searchAreaItems.get(i));
            }
        }
        return checkedSearchItems;
    }

    //Method used to change the state of the checkbox
    public void setChecked(Integer position){
        searchAreaItems.get(position).setChecked(!searchAreaItems.get(position).isChecked());
        System.out.println(searchAreaItems.get(position).getName()+" "+searchAreaItems.get(position).isChecked());
    }*/
}
