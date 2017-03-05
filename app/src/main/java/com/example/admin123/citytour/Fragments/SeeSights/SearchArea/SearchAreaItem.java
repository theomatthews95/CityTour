package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by theom on 04/03/2017.
 */
public class SearchAreaItem{
    LatLng searchCoordinates;
    Integer searchRadius;
    String searchAreaName;

    SearchAreaItem(LatLng searchCoordinates, int searchRadius, String searchAreaName){
        this.searchCoordinates = searchCoordinates;
        this.searchRadius = searchRadius;
        this.searchAreaName= searchAreaName;
    }

    public LatLng getSearchCoordinates(){
        return this.searchCoordinates;
    }
    public Integer getRadius(){ return this.searchRadius; }
    public String getName(){ return this.searchAreaName;}

}