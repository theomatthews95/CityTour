package com.example.admin123.citytour.Map;

import android.content.Context;

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

    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getIcon());
    }


    @Override
    public void onCameraIdle() {

    }
}
