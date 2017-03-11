/*
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

*/
/**
 * Created by Theo on 10/03/2017.
 *//*


public class MyClusterManager extends ClusterManager{


    public MyClusterManager(Context context, GoogleMap map) {
        super(context, map);
    }

    public MyClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
    }

    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        Log.i(TAG, "Bounds "+bounds);
        for (Circle circle : drawnCircles){
            circle.remove();
        }
        for (LatLng marker : markers){
            boolean isViewable = bounds.contains(marker);
            System.out.println(marker.latitude + ", " + marker.longitude + ": " + isViewable);
            Double radius = SphericalUtil.computeDistanceBetween(marker, mMap.getCameraPosition().target);
            if (isViewable == false) {
                // Instantiates a new CircleOptions object and defines the center and radius
                CircleOptions circleOptions = new CircleOptions()
                        .center(marker)
                        .radius(radius); // In meters

                // Get back the mutable Circle
                Circle circle = mMap.addCircle(circleOptions);
                drawnCircles.add(circle);
            }
        }
    }
}
*/
