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

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by theom on 18/03/2017.
 */

public class GoogleDirections extends Fragment {

    private String placesKey;
    private ItineraryList googleDirectionResults;
    private  ArrayList<FavouriteListItem> initialItinList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Log.i("Google Directions", "API key issues");
        } else {

            initialItinList = (ArrayList<FavouriteListItem>) getArguments().getSerializable("itineraryList");
            String waypoints = "None_set";
            String destination = "Not_set";
            String origin = "Not_set";
            for (int i = 0; i<initialItinList.size(); i++){
                if (i == 0) {
                    origin = "origin="+initialItinList.get(i).getLat() +","+initialItinList.get(i).getLong();
                    Log.i(TAG, "i == 0 "+initialItinList.get(i).getTitle());
                }else if (i == 1){
                    destination = "destination="+initialItinList.get(i).getLat() +","+initialItinList.get(i).getLong();
                    Log.i(TAG, "i == 1 "+initialItinList.get(i).getTitle());
                }else if (i == 2){
                    waypoints = "waypoints=optimize:true|"+initialItinList.get(i).getLat() +","+initialItinList.get(i).getLong();
                    Log.i(TAG, "i == 2 "+initialItinList.get(i).getTitle());
                }else{
                    waypoints = waypoints + "|" + initialItinList.get(i).getLat() +","+initialItinList.get(i).getLong();
                    Log.i(TAG, "i == whateever "+initialItinList.get(i).getTitle());
                }
            }
            String params = origin+"&"+destination+"&"+waypoints;
            //Insert retrieved data from SeeSightsFragment into places API request
            String placesRequest = "https://maps.googleapis.com/maps/api/directions/json?" +
                    params+"&mode=walking&key=" + placesKey;

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
                ItineraryList googleDirectionResults = gson.fromJson(input, ItineraryList.class);

                Log.i("TGS", googleDirectionResults.toString());
                return googleDirectionResults;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Itinerary", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ItineraryList itineraryList) {
            reportBack(itineraryList);

        }
    }

    protected void reportBack(ItineraryList googleDirectionResults) {
        if (this.googleDirectionResults == null) {
            this.googleDirectionResults = googleDirectionResults;

        } else {
            //this.itineraryList.getStatus().addAll(itineraryList.getStatus());
        }

        if (googleDirectionResults.getRoutes().size() != 0) {
            Log.i("TGS", "Distance " + googleDirectionResults.getRoutes().get(0).getLegs().get(0).getDistance().getText());

            Bundle bundle = new Bundle();
            bundle.putSerializable("initialItinList", initialItinList);
            bundle.putSerializable("googleDirectionResults", googleDirectionResults);

            String polyline = googleDirectionResults.getRoutes().get(0).getOverview_polyline().getPoints();
            bundle.putString("polyline", polyline);
            Fragment fragment = new ItineraryFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.relativeLayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            Toast.makeText(getActivity(), "Couldn't create an itinerary. Route may be impossible.", Toast.LENGTH_SHORT).show();
        }
    }
}
