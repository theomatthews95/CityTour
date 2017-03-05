package com.example.admin123.citytour.Fragments.SeeSights.Places;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin123.citytour.Fragments.GmapFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaItem;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeDialogFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SeeSightsFragment;
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.net.URLEncoder;

/**
 * Created by theom on 03/03/2017.
 */

public class PlacesList extends Fragment{
    private String placesKey;
    /* Location is Aston University */
    private double latitude = 52.485867;
    private double longitude = -1.890161;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(getActivity(), "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();

        } else {
            String type = URLEncoder.encode(getArguments().getString("locationType"));
            Double searchAreaLong = getArguments().getDouble("searchAreaLong");
            Double searchAreaLat = getArguments().getDouble("searchAreaLat");
            String searchRadius = getArguments().getString("searchRadius");
            String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    searchAreaLong + "," + searchAreaLat + "&type="+type+"&radius="+searchRadius+"&key=" + placesKey;
            PlacesReadFeed process = new PlacesReadFeed();
            process.execute(new String[] {placesRequest});
        }

        return inflater.inflate(R.layout.fragment_place_list, container, false);
    }



    private class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
        @Override
        protected GooglePlaceList doInBackground(String... urls) {
            try {
                String referer = null;
                //dialog.setMessage("Fetching Places Data");
                if (urls.length == 1) {
                    referer = null;
                } else {
                    referer = urls[1];
                }
                String input = GooglePlacesUtility.readGooglePlaces(urls[0], referer);
                Gson gson = new Gson();
                GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
                Log.i("PLACES_EXAMPLE", "Number of places found is " + places.getResults().size());

                return places;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES_EXAMPLE", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(GooglePlaceList places) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("googlePlaceList", places.getResults());
            Fragment fragment = new GmapFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.relativeLayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            //return places.getPlaceNames().get(1);
        }
    }

}
