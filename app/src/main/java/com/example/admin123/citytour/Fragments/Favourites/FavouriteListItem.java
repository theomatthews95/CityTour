package com.example.admin123.citytour.Fragments.Favourites;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouriteListItem implements Serializable
{
    String title;
    String reference;
    Double latitude;
    Double longitude;
    byte[] locationPhoto;
    String distanceToNext;
    String timeToNext;
    Boolean isVisited;

    public FavouriteListItem(String title, String reference, Double latitude, Double longitude, byte[] locationPhoto, String distanceToNext, String timeToNext, Boolean isVisited){
        this.title=title;
        this.reference=reference;
        this.longitude=longitude;
        this.latitude=latitude;
        this.locationPhoto=locationPhoto;
        this.distanceToNext=distanceToNext;
        this.timeToNext=timeToNext;
        this.isVisited=isVisited;
    }

    public Double getLat(){
        return latitude;
    }

    public Double getLong(){
        return longitude;
    }

    public String getTitle(){return title;}

    public FavouriteListItem updateVisited(boolean isVisited){
        FavouriteListItem item = new FavouriteListItem(title, reference, latitude, longitude, locationPhoto, distanceToNext, timeToNext, isVisited);
        return item;
    }
}
