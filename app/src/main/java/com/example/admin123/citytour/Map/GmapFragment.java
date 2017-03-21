package com.example.admin123.citytour.Map;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.*;

import com.example.admin123.citytour.Fragments.Currency.CurrencyExchangeFragment;
import com.example.admin123.citytour.Fragments.Favourites.FavouriteItemFragment;
import com.example.admin123.citytour.Fragments.Favourites.FavouriteListItem;
import com.example.admin123.citytour.Fragments.Favourites.FavouritesListFragment;
import com.example.admin123.citytour.Fragments.HomepageFragment;
import com.example.admin123.citytour.Fragments.PostcardFragment;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;
import com.example.admin123.citytour.Fragments.SeeSights.SeeSightsFragment;
import com.example.admin123.citytour.MainActivity;
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;



public class GmapFragment extends Fragment implements OnMapReadyCallback, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>{
    private GoogleMap mMap;
    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<LatLng> markers = new ArrayList<>();
    private ArrayList<String> placeReferences = new ArrayList<>();
    private ArrayList<String> placePhotoReferences = new ArrayList<>();
    private ArrayList<Circle> drawnCircles = new ArrayList<Circle>();
    private ClusterRenderer renderer;
    private ArrayList<GooglePlace> places;
    private static final String TAG = "MapFragment";
    MultiListener ml = new MultiListener();

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
        ViewGroup.LayoutParams params = fragment.getView().getLayoutParams();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        places =  (ArrayList<GooglePlace>) getArguments().getSerializable("googlePlaceList");

        Integer numberOfPlaces;
        if(places!= null) {
            numberOfPlaces = places.size();
        }else {
            numberOfPlaces = (Integer) getArguments().getInt("numberOfPlaces");
        }

        Double searchAreaLat = (Double) getArguments().getDouble("searchAreaLat");
        Double searchAreaLong = (Double) getArguments().getDouble("searchAreaLong");
        HashMap<String, Integer> placePins = MainActivity.returnPlacePins().PlacePins();

        ArrayList<FavouriteListItem> itineraryList = (ArrayList<FavouriteListItem>) getArguments().getSerializable("itineraryList");
        String polyline = (String) getArguments().getString("polyline");


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

                List<GooglePlace.Photos> photoRef = places.get(i).getPhotos();
                if(photoRef != null) {
                    placePhotoReferences.add(photoRef.get(0).getPhoto_reference());

                    Log.i(TAG, photoRef.get(0).getPhoto_reference());
                }
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

                BitmapDescriptor customPin = getBitmapDescriptor(pin);
                Log.i(TAG, customPin.toString());


                // Add cluster item (markers) to the cluster manager.
                final MyItem mapItem = new MyItem(lat, lng, title, customPin, i);

                // Click listener for when marker's title is clicked, launch location details fragment

                placeReferences.add(places.get(i).getReference());
                mClusterManager.setOnClusterItemInfoWindowClickListener(this);


                mMap.setOnInfoWindowClickListener(mClusterManager);

                mClusterManager.addItem(mapItem);

                DrawHalos();
            }
        }else {
            if (itineraryList!=null){
                for(FavouriteListItem item : itineraryList){
                    LatLng marker = new LatLng(item.getLat(), item.getLong());
                    markers.add(marker);
                    MyItem mapItem = new MyItem(item.getLat(), item.getLong(),item.getTitle(), getBitmapDescriptor(R.drawable.ic_heart_pin), 0);
                    mClusterManager.addItem(mapItem);
                }

            }else {
                //If there are no search items to display
                LatLng marker = new LatLng(searchAreaLat, searchAreaLong);
                markers.add(marker);
                // Add cluster items (markers) to the cluster manager.
                MyItem mapItem = new MyItem(searchAreaLat, searchAreaLong, "Search Location", getBitmapDescriptor(R.drawable.ic_map_pin), 0);
                mClusterManager.addItem(mapItem);
                //mMap.addMarker(new MarkerOptions().position(marker).title("Search Location"));
                Toast.makeText(getContext(), "No places found. Try a larger radius.", Toast.LENGTH_SHORT).show();
            }
        }

        //Create cluster renderer
        renderer = new ClusterRenderer(getActivity(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        //Create boundary around markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //For each marker, add it to the bounding area
        for (LatLng marker1 : markers) {
            builder.include(marker1);
        }

        int padding = 15; // offset from edges of the map in pixels

        LatLngBounds bounds = builder.build();
        CameraUpdate cu;

        if (markers.size() == 1){
            //cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            LatLng latLng = new LatLng(markers.get(0).latitude, markers.get(0).longitude);
            cu = CameraUpdateFactory.newLatLngZoom(latLng , 14.0f);
        }else {
            cu = CameraUpdateFactory.newLatLngBounds(bounds , padding);
        }

        if(polyline!=null) {
            List<LatLng> points = (PolyUtil.decode(polyline));
            for (int i = 0; i < points.size() - 1; i++) {
                LatLng src = points.get(i);
                LatLng dest = points.get(i + 1);

                // mMap is the Map Object
                Polyline line = mMap.addPolyline(
                        new PolylineOptions().add(
                                new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude)
                        ).width(7).color(Color.BLUE).geodesic(true)
                );
            }
        }

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
        ml.addClusterManagerIdle(mClusterManager);
        //mMap.setOnCameraIdleListener(ml);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

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
        ml.addHaloIdle(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                //Log.i(TAG, "Bounds "+bounds);
                for (Circle circle : drawnCircles){
                    circle.remove();
                }
                for (LatLng marker : markers){
                    boolean isViewable = bounds.contains(marker);

                    Double radius;

                    double southMiddleLong = mMap.getCameraPosition().target.longitude;
                    double southMiddleLat = bounds.southwest.latitude;
                    LatLng southHalfWay = new LatLng(southMiddleLat, southMiddleLong);

                    double northMiddleLong = mMap.getCameraPosition().target.longitude;
                    double northMiddleLat = bounds.northeast.latitude;
                    LatLng northHalfway = new LatLng(northMiddleLat, northMiddleLong);


                    double westMiddleLong = bounds.southwest.longitude;
                    double westMiddleLat = bounds.southwest.latitude + (mMap.getCameraPosition().target.latitude - bounds.southwest.latitude);
                    LatLng westHalfway = new LatLng(westMiddleLat, westMiddleLong);


                    double eastMiddleLong = bounds.northeast.longitude;
                    double eastMiddleLat = bounds.southwest.latitude + (mMap.getCameraPosition().target.latitude - bounds.southwest.latitude);
                    LatLng eastHalfway = new LatLng(eastMiddleLat, eastMiddleLong);

                    if (isViewable == false) {
                        if (marker.latitude < bounds.southwest.latitude) {
                            if (marker.longitude < bounds.southwest.longitude) {
                                // Log.i(TAG, "South West of display");
                                radius = SphericalUtil.computeDistanceBetween(bounds.southwest, marker)*1.4;
                                //radius = ;
                            }else if (marker.longitude > bounds.northeast.longitude) {
                                //Log.i(TAG, "South East of display");
                                LatLng southEast = new LatLng(bounds.southwest.latitude, bounds.northeast.longitude);
                                radius = SphericalUtil.computeDistanceBetween(southEast, marker) * 1.4;
                                //radius = southWest;
                            }else{
                                //Log.i(TAG, "South of display");
                                radius = SphericalUtil.computeDistanceBetween(southHalfWay, marker)*1.4;
                            }
                        }else if (marker.latitude > bounds.northeast.latitude){
                            if (marker.longitude > bounds.northeast.longitude){
                                // Log.i(TAG, "North East of display");
                                radius = SphericalUtil.computeDistanceBetween(bounds.northeast, marker);
                            }else if (marker.longitude < bounds.southwest.longitude){
                                //Log.i(TAG, "North West of display");
                                LatLng northWest = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);
                                radius = SphericalUtil.computeDistanceBetween(northWest, marker)*1.4;
                            }else{
                                //Log.i(TAG, "North of display");
                                radius = SphericalUtil.computeDistanceBetween(northHalfway, marker)*1.4;
                            }
                        }else if (marker.longitude < bounds.southwest.longitude){
                            //Log.i(TAG, "West of display");
                            radius = SphericalUtil.computeDistanceBetween(westHalfway, marker);
                            radius = radius*1.2;
                        }else{
                            //Log.i(TAG, "East of display");
                            radius = SphericalUtil.computeDistanceBetween(eastHalfway, marker);
                            radius = radius*1.2;
                        }

                        boolean drawCircle = true;
                        if (radius >= SphericalUtil.computeDistanceBetween(westHalfway, eastHalfway)/2 || radius >= 2000 || radius >= SphericalUtil.computeDistanceBetween(bounds.northeast, mMap.getCameraPosition().target)*0.5){
                            //Log.i(TAG, "Circle too big");
                            drawCircle = false;
                        }

                        if (drawCircle == true) {
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
        });
        mMap.setOnCameraIdleListener(ml);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
         /*for (LatLng marker : markers){
             marker.remove();
         }*/

        Fragment f = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commit();
        }

        super.onDestroyView();
    }


    @Override
    public void onPause() {
        super.onPause();
        final FragmentManager fragManager = this.getFragmentManager();
        final Fragment fragment = fragManager.findFragmentById(R.id.map);
        if(fragment!=null){
            fragManager.beginTransaction().remove(fragment).commit();
            System.out.println("Pause");
        }
    }


    /*  @Override
      public void onResume() {
          SupportMapFragment f = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
          if (f == null) {
              getFragmentManager().beginTransaction().replace(R.id.relativeLayout, f).commit();
              System.out.println("Trying to destroy "+f);
          }
          super.onResume();
      }*/
    @Override
    public void onClusterItemInfoWindowClick(MyItem mapItem) {

        Bundle bundle = new Bundle();
        if (mapItem.getPlaceArrayPosition() >= placePhotoReferences.size()) {
            bundle.putString("photoReference", placePhotoReferences.get(placePhotoReferences.size() - 1));
        }else{
            bundle.putString("photoReference", placePhotoReferences.get(mapItem.getPlaceArrayPosition()));
        }

        bundle.putString("placeReference", placeReferences.get(mapItem.getPlaceArrayPosition()));
        bundle.putSerializable("resultsFromMap", places);
        bundle.putString("title", mapItem.getTitle());
        bundle.putDouble("lat", mapItem.getPosition().latitude);
        bundle.putDouble("long", mapItem.getPosition().longitude);
        bundle.putString("launchedFrom", "Map_fragment");
        Fragment fragment = new FavouriteItemFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.relativeLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}