package com.example.admin123.citytour.Fragments.SeeSights.SearchType;

/**
 * Created by theom on 01/03/2017.
 */

public class SearchTypeItem {
    String name;
    boolean isChecked; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */
    //The value accepted by google places API
    String searchValue;

    SearchTypeItem(String name, String searchValue, int value){
        this.name = name;
        this.isChecked = isChecked;
        this.searchValue = searchValue;
    }
    public String getName(){
        return this.name;
    }
    public boolean isChecked(){
        return this.isChecked;
    }
    public String getSearchValue(){
        return this.searchValue;
    }
    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }

}