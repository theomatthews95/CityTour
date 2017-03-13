/*
package com.example.admin123.citytour.Fragments.SeeSights.Places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.R;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.List;

*/
/**
 * Created by theom on 13/03/2017.
 *//*


public class PlaceDetailActivity extends AppCompatActivity {
    private GooglePlace place;
    private Context context;
    private String placesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        this.context = getApplicationContext();

        Log.i("PLACE EXAMPLE", "Place Detail Activity");
        place = (GooglePlace) this.getIntent().getExtras().getSerializable("PLACE");
        Log.i("PLACE EXAMPLE", "got place " + place.toString());
        //this.setHasOptionsMenu(true);
        setTitle(place.getName());
        placesKey = getResources().getString(R.string.google_maps_key);
        String placesKey = getResources().getString(R.string.placesKey);;
        String placesRequest = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "key=" + placesKey + "&reference=" + place.getReference();
        PlacesDetailReadFeed detailTask = new PlacesDetailReadFeed();
        detailTask.execute(new String[]{placesRequest});
    }

    @Override
    public void onResume () {
        super.onResume();

    }

    private class PlacesDetailReadFeed extends AsyncTask<String, Void, PlaceDetail> {
        private final ProgressDialog dialog = new ProgressDialog(PlaceDetailActivity.this);

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
            this.dialog.setMessage("Getting nearby places...");
            this.dialog.show();

        }

        @Override
        protected void onPostExecute(PlaceDetail placeDetail) {
            place = placeDetail.getResult();
            fillInLayout(place);
            this.dialog.dismiss();
        }
    }

    private void fillInLayout(GooglePlace place) {
        // title element has name and types
        TextView title = (TextView)findViewById(R.id.name);
        title.setText(place.getName());
        Log.i("PLACES EXAMPLE", "Setting title to: " + title.getText());
        //address
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(place.getFormatted_address() + " " + place.getFormatted_phone_number());
        Log.i("PLACES EXAMPLE", "Setting address to: " + address.getText());
        //vicinity
        TextView vicinity = (TextView) findViewById(R.id.vicinity);
        vicinity.setText(place.getVicinity());
        Log.i("PLACES EXAMPLE", "Setting vicinity to: " + vicinity.getText());
        //rating
        TextView reviews = (TextView) findViewById(R.id.reviews);

        List<GooglePlace.Review> reviewsData = place.getReviews();
        if (reviewsData != null) {
            StringBuffer sb = new StringBuffer();
            for (GooglePlace.Review r : reviewsData) {
                sb.append(r.getAuthor_name());
                sb.append(" says \"");
                sb.append(r.getText());
                sb.append("\" and rated it ");
                sb.append(r.getRating());
                sb.append("\n");
            }
            reviews.setText("Reviews:\n" + sb.toString());
        } else {
            reviews.setText("There have not been any reviews!");
        }
        Log.i("PLACES EXAMPLE", "Setting rating to: " + reviews.getText());
    }

}
*/
