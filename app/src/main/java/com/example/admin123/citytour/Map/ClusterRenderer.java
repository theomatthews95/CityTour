package com.example.admin123.citytour.Map;

import android.content.Context;

import com.example.admin123.citytour.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/** Created by theom on 10/03/2017 */



public class ClusterRenderer extends DefaultClusterRenderer<MyItem> implements GoogleMap.OnCameraIdleListener {

    private final Context mContext;
    private BitmapDescriptor customPin;


    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager, BitmapDescriptor customPin) {
        super(context, map, clusterManager);
        this.customPin = customPin;
        mContext = context;
    }

    @Override protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = customPin;

        markerOptions.icon(markerDescriptor).snippet(item.getTitle());

    }


    @Override
    public void onCameraIdle() {

    }
}
