package com.example.admin123.citytour.Map;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;

import static com.google.android.gms.wearable.DataMap.TAG;

//** Created by Theo on 10/03/2017.



public class MyClusterManager<MyItem> extends ClusterManager{


    public MyClusterManager(Context context, GoogleMap map) {
        super(context, map);
    }

    public MyClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
    }


}
