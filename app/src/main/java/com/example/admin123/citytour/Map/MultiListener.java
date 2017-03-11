package com.example.admin123.citytour.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

/**
 * Created by Theo on 10/03/2017.
 */

public class MultiListener implements GoogleMap.OnCameraIdleListener {
    ArrayList<GoogleMap.OnCameraIdleListener> mListeners = new ArrayList<GoogleMap.OnCameraIdleListener>();

    public MultiListener(){

    }

    @Override
    public void onCameraIdle() {
        for (GoogleMap.OnCameraIdleListener listener : mListeners){
            listener.onCameraIdle();
         }
    }

    public void addClusterManagerIdle(ClusterManager clusterManager){
        mListeners.add(clusterManager);
    }

    public void addHaloIdle(GoogleMap.OnCameraIdleListener listener){
        mListeners.add(listener);
    }
}
