package com.example.admin123.citytour.Fragments.SeeSights;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.Fragments.SeeSights.Places.PlacesList;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.RecentLocationsDBHelper;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaDialogFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaItem;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeDialogFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeItem;
import com.example.admin123.citytour.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationListener;
import android.location.LocationListener;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

public class SeeSightsFragment extends Fragment implements View.OnClickListener,
        SearchTypeDialogFragment.OnSetSearchLocationTypeFromListener,
        SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener,
        GoogleApiClient.OnConnectionFailedListener {


    private OnFragmentInteractionListener mListener;
    private String searchLocationType;
    private TextView whatTypeTextView;
    private TextView whatAreaTextView;
    private double searchLat;
    private double searchLong;
    private String searchRadius;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double userLocationLat;
    private double userLocationLong;

    boolean toastGPSShown = false;

    // TODO: Rename and change types and number of parameters
    public static SeeSightsFragment newInstance() {
        SeeSightsFragment fragment = new SeeSightsFragment();
        return fragment;
    }

    public SeeSightsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_see_sights, container, false);

        //TextView that is changed to display user's search area
        whatAreaTextView = (TextView) v.findViewById(R.id.whatAreaTextView);

        // Configure what area is search
        Button mWhatArea = (Button) v.findViewById(R.id.what_area_button);
        mWhatArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (toastGPSShown == false) {
                    Toast.makeText(getContext(), "GPS is currently not functioning", Toast.LENGTH_SHORT).show();
                }
                showLocationAreaDialog();
            }
        });

        setUpLocation();
        //getUserLocation();


        // Configure what type of location is being searched for
        Button mWhatType = (Button) v.findViewById(R.id.what_type_button);
        mWhatType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showLocationTypeDialog();
            }
        });

        //TextView that is changed to display what the user has selected from What Type dialog
        whatTypeTextView = (TextView) v.findViewById(R.id.what_type_search_editText);

        Button mSearch = (Button) v.findViewById(R.id.search_map);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String locationType = searchLocationType;
                if (locationType != null)
                    bundle.putString("locationType", locationType);
                else {
                    Toast.makeText(getContext(), "You've not entered a location type", Toast.LENGTH_SHORT).show();
                    bundle.putString("locationType", "");
                }
                if (searchLong == 0.0 && searchLat == 0.0){
                    Log.i(TAG, "Entered");
                    searchLat = userLocationLat;
                    searchLong = userLocationLong;
                    searchRadius = "2000";
                }
                bundle.putDouble("searchAreaLong", searchLong);
                bundle.putDouble("searchAreaLat", searchLat);
                bundle.putString("searchRadius",searchRadius);
                Fragment fragment = new PlacesList();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.relativeLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    //Display the dialog to allow user to input what area they would like to search within
    public void showLocationAreaDialog(){
        SearchAreaDialogFragment dialog = SearchAreaDialogFragment.newInstance("Search area");
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "fragmentDialog");
    }

    //Set the search location area parameters
    public void setSearchLocationArea(SearchAreaItem searchAreaItem) {
        searchRadius = searchAreaItem.getRadius().toString();
        String searchAreaName = searchAreaItem.getName();

        if (searchAreaName == null || searchAreaName.equals("My Location")){
            searchLat = userLocationLat;
            searchLong = userLocationLong;
        }
        else {
            searchLat = searchAreaItem.getSearchCoordinates().latitude;
            searchLong = searchAreaItem.getSearchCoordinates().longitude;
        }

        Log.i(TAG, "searchAreaName = "+searchAreaName+", Coordinates: "+searchLat + ", "+searchLong);
        //change the text view above the dialog launch button to show the user their selection
        if (searchAreaName == null) {
            whatAreaTextView.setText("My Location");
            whatAreaTextView.setTextSize(35);
        } else {
            whatAreaTextView.setText(searchAreaName);
            whatAreaTextView.setTextSize(40);
        }

    }

    //Display dialog to allow user to input what type of location they would like to search
    public void showLocationTypeDialog(){
        SearchTypeDialogFragment dialog = SearchTypeDialogFragment.newInstance("Point of interest type");
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "fragmentDialog");
    }


    //Set the search location type
    public void setSearchLocationType(ArrayList<SearchTypeItem> searchLocationTypeArray){
        String searchLocationType="";
        String visibleSearchLocationType="";

        //Iterate over the returned array list from the dialogfragment to create a query string for places API
        for(int i = 0; i<searchLocationTypeArray.size(); i++){
            if(i == 0){
                searchLocationType = searchLocationTypeArray.get(i).getSearchValue();
                visibleSearchLocationType = searchLocationTypeArray.get(i).getName();
            }else {
                searchLocationType = searchLocationType + "|" + searchLocationTypeArray.get(i).getSearchValue();
                visibleSearchLocationType = visibleSearchLocationType + ", " + searchLocationTypeArray.get(i).getName();
            }
        }

        //change the text view above the dialog launch button to show the user their selection
        if(visibleSearchLocationType.equals(""))
        {
            whatTypeTextView.setText("What type?");
            whatTypeTextView.setTextSize(50);
        }else{
            whatTypeTextView.setText(visibleSearchLocationType);
            whatTypeTextView.setTextSize(22);
        }
        this.searchLocationType = searchLocationType;
        System.out.println(searchLocationType);
    }

    private void setUpLocation(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i(TAG, "Your location is "+location.getLatitude()+", "+location.getLongitude());
                /*while (toastGPSShown == false) {
                    Toast.makeText(getContext(), "GPS is functioning", Toast.LENGTH_SHORT).show();
                    toastGPSShown = true;
                }*/
                userLocationLat = location.getLatitude();
                userLocationLong = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            } else {
                getUserLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getUserLocation();
                return;
        }
    }

    private void getUserLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        locationManager.requestLocationUpdates("gps", 500, 0, locationListener);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
