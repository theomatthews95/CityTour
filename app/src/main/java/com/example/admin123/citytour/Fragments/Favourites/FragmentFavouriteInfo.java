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
import java.util.List;

/**
 * Created by theom on 13/03/2017.
 */
public class FragmentFavouriteInfo extends Fragment {
    private String placeReference;
    private GooglePlace place;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_details, container, false);

        placeReference = getArguments().getString("placeReference");
        Log.i("Favs","Place reference "+placeReference);

        String placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(getActivity(), "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();
        } else {
            PlacesDetailReadFeed process = new PlacesDetailReadFeed();
            String placeDetailRequest = "https://maps.googleapis.com/maps/api/place/details/json?" +
                    "key=" + placesKey + "&reference=" + placeReference;

            process.execute(new String[] {placeDetailRequest});
        }

        return v;
    }
    private class PlacesDetailReadFeed extends AsyncTask<String, Void, PlaceDetail> {

        @Override
        protected PlaceDetail doInBackground(String... urls) {
            try {
                //dialog.setMessage("Fetching Places Data");
                String referer = null;
                //dialog.setMessage("Fetching Places Data");
                if (urls.length == 1) {
                    referer = null;
                } else {
                    referer = urls[1];
                }
                String input = GooglePlacesUtility.readGooglePlaces(urls[0], referer);
                Gson gson = new Gson();
                PlaceDetail place = gson.fromJson(input, PlaceDetail.class);
                Log.i("PLACES EXAMPLE", "Place found is " + place.toString());
                return place;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES EXAMPLE", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(PlaceDetail placeDetail) {
            place = placeDetail.getResult();
            fillInLayout(place);
        }
    }

    private void fillInLayout(GooglePlace place) {
        // title element has name and types
        TextView title = (TextView)getView().findViewById(R.id.name);
        title.setText(place.getName());
        Log.i("PLACES EXAMPLE", "Setting title to: " + title.getText());
        //address
        TextView address = (TextView) getView().findViewById(R.id.address);
        address.setText(place.getFormatted_address() + " " + place.getFormatted_phone_number());
        Log.i("PLACES EXAMPLE", "Setting address to: " + address.getText());
        //vicinity
        /*TextView vicinity = (TextView) getView().findViewById(R.id.vicinity);
        vicinity.setText(place.getVicinity());
        Log.i("PLACES EXAMPLE", "Setting vicinity to: " + vicinity.getText());*/
        //rating
        TextView reviews = (TextView) getView().findViewById(R.id.reviews);
        Log.i("PLACES", "INfo"+place.getIcon());
        List<GooglePlace.Review> reviewsData = place.getReviews();
        if (reviewsData != null) {
            StringBuffer sb = new StringBuffer();
            for (GooglePlace.Review r : reviewsData) {
                sb.append(r.getAuthor_name());
                sb.append(" says \"");
                sb.append(r.getText());
                sb.append("\" and rated it ");
                sb.append(r.getRating());
                sb.append("\n\n");
            }
            reviews.setText(sb.toString());
        } else {
            reviews.setText("There have not been any reviews!");
        }
    }
}
