package com.example.admin123.citytour.Fragments.SeeSights;

/**
 * Created by theom on 01/03/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin123.citytour.R;

import java.util.ArrayList;

public class SearchTypeListAdapter extends ArrayAdapter{
        SearchTypeItem[] searchTypeItems = null;
        Context context;
        ArrayList<SearchTypeItem> checkedSearchItems = new ArrayList<SearchTypeItem>();
        View theView;

        public SearchTypeListAdapter(Context context, SearchTypeItem[] resource) {
                super(context, R.layout.location_type_item,resource);
                // TODO Auto-generated constructor stub
                this.context = context;
                this.searchTypeItems = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                theView=convertView;
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                convertView = inflater.inflate(R.layout.location_type_item, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.textView1);
                CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
                name.setText(searchTypeItems[position].getName());
                if(searchTypeItems[position].getValue() == 1) {
                    cb.setChecked(true);
                }else {
                    cb.setChecked(false);
                }
                return convertView;
        }

        @Override
        public SearchTypeItem getItem(int position){
                return searchTypeItems[position];
        }

        public SearchTypeItem[] getArray(){
            return searchTypeItems;
        }

        //return the type locations that are
        public ArrayList<SearchTypeItem> getCheckedItems(){
                ArrayList<SearchTypeItem> checkedSearchItems = new ArrayList<SearchTypeItem>();
                CheckBox cb = (CheckBox) theView.findViewById(R.id.checkBox1);
                for (int i=0;i<searchTypeItems.length;i++){
                        if(cb.isChecked() == true){
                                checkedSearchItems.add(searchTypeItems[i]);
                        }
                }

                return checkedSearchItems;
        }
}