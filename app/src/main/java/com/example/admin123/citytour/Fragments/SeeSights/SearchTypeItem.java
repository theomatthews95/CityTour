package com.example.admin123.citytour.Fragments.SeeSights;

/**
 * Created by theom on 01/03/2017.
 */

public class SearchTypeItem {
    String name;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */
    //The value accepted by google places API
    String searchValue;

    SearchTypeItem(String name, String searchValue, int value){
        this.name = name;
        this.value = value;
        this.searchValue = searchValue;
    }
    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }
    public String getSearchValue(){
        return this.searchValue;
    }

}