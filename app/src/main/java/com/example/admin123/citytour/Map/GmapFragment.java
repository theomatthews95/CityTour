package com.example.admin123.citytour.Map;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.*;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;
import com.example.admin123.citytour.MainActivity;
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.admin123.citytour.R.id.drawerLayout;
import static com.google.android.gms.wearable.DataMap.TAG;


public class GmapFragment extends Fragment implements OnMapReadyCallback{
    MapView mMapView;
    private GoogleMap mMap;
    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<LatLng> markers = new ArrayList<>();
    private ArrayList<Circle> drawnCircles = new ArrayList<Circle>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<GooglePlace> places =  (ArrayList<GooglePlace>) getArguments().getSerializable("googlePlaceList");
        Integer numberOfPlaces = (Integer) getArguments().getInt("numberOfPlaces");
        Double placesSearchLat = (Double) getArguments().getDouble("searchAreaLat");
        Double placesSearchLong = (Double) getArguments().getDouble("searchAreaLong");
        HashMap<String, Integer> placePins = MainActivity.returnPlacePins().PlacePins();


        setUpClusterer();


        if (numberOfPlaces != 0) {
            //If the place API request returned values
            //Iterate over the ArrayList containing all the places

            for (int i = 0; i < places.size(); i++) {

                //Get the name of the place
                String title = places.get(i).getName();

                //Get the longitude and latitude of the place
                double lat = places.get(i).getGeometry().getLocation().getLat();
                double lng = places.get(i).getGeometry().getLocation().getLng();

                //Create a LatLng item using the place's lat and long
                LatLng marker = new LatLng(lat, lng);

                //Add the marker to the arraylist of all the latlng markers
                markers.add(marker);

                //Get the place type for choosing an icon
                String placeTypes = places.get(i).getTypes().get(0);

                //Search the icon hashmap for a matching custom pin
                Integer pin = placePins.get(placeTypes);

                //If there is no pin in the hashmap, assign a default pin
                if(pin == null) {
                    pin = R.drawable.ic_map_pin;
                }


                // Add cluster items (markers) to the cluster manager.
                MyItem mapItem = new MyItem(lat, lng, title);
                mClusterManager.addItem(mapItem);

                BitmapDescriptor customPin = getBitmapDescriptor(pin);

                AddCustomPins(customPin);

                DrawHalos();
            }
        }else {
            //If there are no search items to display
            LatLng marker = new LatLng(placesSearchLat, placesSearchLong);
            markers.add(marker);

            // Add cluster items (markers) to the cluster manager.
            MyItem mapItem = new MyItem(placesSearchLat, placesSearchLat, "Search Location");
            mClusterManager.addItem(mapItem);
            //mMap.addMarker(new MarkerOptions().position(marker).title("Search Location"));
            Toast.makeText(getContext(), "No places found. Try a larger radius.", Toast.LENGTH_SHORT).show();
        }



        //Create boundary around markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //For each marker, add it to the bounding area
        for (LatLng marker1 : markers) {
            builder.include(marker1);
        }

        int padding = 15; // offset from edges of the map in pixels

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        //Move the camera to location
        mMap.moveCamera(cu);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(getActivity(), mMap);


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

    }

    private void AddCustomPins(BitmapDescriptor customPin){
        final ClusterRenderer renderer = new ClusterRenderer(getActivity(), mMap, mClusterManager, customPin);
        mClusterManager.setRenderer(renderer);
    }


    public BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = getResources().getDrawable(id);
        int h = ((int) convertDpToPixel(75));
        int w = ((int) convertDpToPixel(85));
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private void DrawHalos(){
        /*mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.e(TAG,"Listener 1");
            }
        });*/

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
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
        });

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
