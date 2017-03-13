package com.example.admin123.citytour.Fragments.Favourites;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlacesUtility;
import com.example.admin123.citytour.Fragments.SeeSights.Places.PlaceDetail;
import com.example.admin123.citytour.Fragments.SeeSights.Places.PlacesList;
import com.example.admin123.citytour.Fragments.SeeSights.SeeSightsFragment;
import com.example.admin123.citytour.R;
import com.google.gson.Gson;

import java.net.URLEncoder;

/**
 * Created by theom on 13/03/2017.
 */
public class FragmentFavouriteInfo extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_details, container, false);

        GooglePlace place = (GooglePlace) getArguments().getSerializable("place");
        TextView placeTitle = (TextView) v.findViewById(R.id.name);
        placeTitle.setText(place.getName());

        TextView placeAddress = (TextView) v.findViewById(R.id.address);
        placeAddress.setText(place.getFormatted_address());

        String placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(getActivity(), "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();
        } else {
            //Get users inputted data from SeeSightsFragment to create Google Places API Request
           /* String type = URLEncoder.encode(getArguments().getString("locationType"));


            Double searchAreaLong = -1.89028791;
            Double searchAreaLat = 52.48549062;

            //Insert retrieved data from SeeSightsFragment into places API request
            String placesRequest = "https://maps.googleapis.com/maps/api/place/details/json?" +
                    "key=" + placesKey + "&reference=" + place.getReference();*/
            /*PlacesList.PlacesReadFeed process = new PlacesList.PlacesReadFeed();

            //Execute API request
            process.execute(new String[] {placesRequest});*/
        }



        return v;
    }
}
