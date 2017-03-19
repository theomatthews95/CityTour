package com.example.admin123.citytour.Fragments.Itinerary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin123.citytour.Fragments.Favourites.FavouriteListItem;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlaceList;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlacesUtility;
import com.example.admin123.citytour.Fragments.SeeSights.Places.PlacesList;
import com.example.admin123.citytour.Map.GmapFragment;
import com.example.admin123.citytour.R;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by theom on 18/03/2017.
 */

public class GoogleDirections extends Fragment {

    private String placesKey;
    private ItineraryList itineraryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(getActivity(), "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();
        } else {

            ArrayList<FavouriteListItem> itineraryList = (ArrayList<FavouriteListItem>) getArguments().getSerializable("itineraryList");
            String waypoints = "None_set";
            String destination = "Not_set";
            String origin = "Not_set";
            for (int i = 0; i<itineraryList.size(); i++){
                if (i == 0) {
                    origin = "origin="+itineraryList.get(i).getLat() +","+itineraryList.get(i).getLong();
                }else if (i == 1){
                    destination = "destination="+itineraryList.get(i).getLat() +","+itineraryList.get(i).getLong();
                }else if (i == 2){
                    waypoints = "waypoints=optimize:true|"+itineraryList.get(i).getLat() +","+itineraryList.get(i).getLong();
                }else{
                    waypoints = waypoints + "|" + itineraryList.get(i).getLat() +","+itineraryList.get(i).getLong();
                }
            }
            String params = origin+"&"+destination+"&"+waypoints;
            //Insert retrieved data from SeeSightsFragment into places API request
            String placesRequest = "https://maps.googleapis.com/maps/api/directions/json?" +
                    params+"&mode=walking&key=" + placesKey;
           // String placesRequest ="https://maps.googleapis.com/maps/api/directions/json?origin=52.486435,-1.888999&destination=52.480962,-1.895156&waypoints=optimize:true|52.483366,-1.897720|52.486069,-1.895087&mode=walking&key=" + placesKey;

            GoogleDirections.DirectionsReadFeed process = new GoogleDirections.DirectionsReadFeed();

            //Execute API request
            process.execute(new String[] {placesRequest});

        }

        return inflater.inflate(R.layout.fragment_place_list, container, false);
    }



    private class DirectionsReadFeed extends AsyncTask<String, Void, ItineraryList> {
        @Override
        protected ItineraryList doInBackground(String... urls) {
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
                ItineraryList itineraryList = gson.fromJson(input, ItineraryList.class);
                //Log.i("Places", "Number of places found is " + ItineraryList.getResults().size());

                return itineraryList;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Itinerary", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ItineraryList itineraryList) {
            reportBack(itineraryList);

            //return places.getPlaceNames().get(1);
        }
    }

    protected void reportBack(ItineraryList itineraryList) {
        if (this.itineraryList == null) {
            this.itineraryList = itineraryList;

        } else {
            this.itineraryList.getResults().addAll(itineraryList.getResults());
        }
        Log.i("TGS", "NOOOISE"+ itineraryList.getRoutesList());
        /*Bundle bundle = new Bundle();
        bundle.putSerializable("googlePlaceList", placeResults.getResults());
        bundle.putInt("numberOfPlaces", placeResults.getResults().size());
        //User's search location
        bundle.putDouble("searchAreaLong", searchAreaLong);
        bundle.putDouble("searchAreaLat", searchAreaLat);
        Fragment fragment = new GmapFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.relativeLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }
}
