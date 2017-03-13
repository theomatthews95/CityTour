package com.example.admin123.citytour.Fragments.SeeSights.Places;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin123.citytour.Map.GmapFragment;
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import java.net.URLEncoder;

/**
 * Created by theom on 03/03/2017.
 */

public class PlacesList extends Fragment{

    private Double searchAreaLong;
    private Double searchAreaLat;
    private String placesKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(getActivity(), "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();
        } else {
            //Get users inputted data from SeeSightsFragment to create Google Places API Request
            String type = URLEncoder.encode(getArguments().getString("locationType"));
            searchAreaLong = getArguments().getDouble("searchAreaLong");
            searchAreaLat = getArguments().getDouble("searchAreaLat");
            if(searchAreaLat == 0.0 && searchAreaLong == 0.0){
                searchAreaLong = -1.89028791;
                searchAreaLat = 52.48549062;
                Toast.makeText(getContext(), "Couldn't establish user location so searching Birmingham", Toast.LENGTH_LONG).show();
            }

            String searchRadius = getArguments().getString("searchRadius");

            //Insert retrieved data from SeeSightsFragment into places API request
            String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    searchAreaLat + "," + searchAreaLong + "&type="+type+"&radius="+searchRadius+"&key=" + placesKey;
            PlacesReadFeed process = new PlacesReadFeed();

            //Execute API request
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
            bundle.putInt("numberOfPlaces", places.getResults().size());
            //User's search location
            bundle.putDouble("searchAreaLong", searchAreaLong);
            bundle.putDouble("searchAreaLat", searchAreaLat);
            Fragment fragment = new GmapFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.relativeLayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            //return places.getPlaceNames().get(1);
        }
    }

}
