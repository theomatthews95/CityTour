package com.example.admin123.citytour.Fragments;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.admin123.citytour.R.id.drawerLayout;
import static com.google.android.gms.wearable.DataMap.TAG;


public class GmapFragment extends Fragment implements OnMapReadyCallback{
    MapView mMapView;
    private GoogleMap mMap;

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
        ArrayList<LatLng> markers = new ArrayList<>();

        if (numberOfPlaces != 0) {
            for (int i = 0; i < places.size(); i++) {
                String title = places.get(i).getName();
                double lat = places.get(i).getGeometry().getLocation().getLat();
                double lng = places.get(i).getGeometry().getLocation().getLng();
                LatLng marker = new LatLng(lat, lng);
                markers.add(marker);
                mMap.addMarker(new MarkerOptions().position(marker)
                        .title(title)
                        .icon(getBitmapDescriptor(R.drawable.ic_map_pin))
                );
            }
        }else {
            LatLng marker = new LatLng(placesSearchLat, placesSearchLong);
            Log.i(TAG, "MAPS "+placesSearchLat + ", " +placesSearchLong);
            markers.add(marker);
            mMap.addMarker(new MarkerOptions().position(marker).title("Search Location"));
            Toast.makeText(getContext(), "No places found. Try a larger radius.", Toast.LENGTH_SHORT).show();
        }

        //Create boundary around markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng marker1 : markers) {
            builder.include(marker1);
        }


        int padding = 15; // offset from edges of the map in pixels

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        //Move the camera to location
        mMap.moveCamera(cu);




       //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.4174885,-3.7035306)));
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = getResources().getDrawable(R.drawable.ic_map_pin);
        int h = ((int) convertDpToPixel(70));
        int w = ((int) convertDpToPixel(70));
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
