package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by theom on 04/03/2017.
 */
public class SearchAreaItem{
    LatLng searchCoordinates;
    Integer searchRadius;

    SearchAreaItem(LatLng searchCoordinates, int searchRadius){
        this.searchCoordinates = searchCoordinates;
        this.searchRadius = searchRadius;
    }
    public LatLng getSearchCoordinates(){
        return this.searchCoordinates;
    }
    public Integer getRadius(){ return this.searchRadius; }

}