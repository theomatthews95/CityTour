package com.example.admin123.citytour.Fragments.Favourites;

import android.graphics.Bitmap;
import android.util.Log;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouriteListItem
{
    int iconId;
    String title;
    String reference;
    Double latitude;
    Double longitude;
    Bitmap locationPhoto;

    FavouriteListItem(int iconId, String title, String reference, Double latitude, Double longitude, Bitmap locationPhoto){
        this.iconId=iconId;
        this.title=title;
        this.reference=reference;
        this.longitude=longitude;
        this.latitude=latitude;
        this.locationPhoto=locationPhoto;
    }
}
