package com.example.admin123.citytour.Fragments.Favourites;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlacesUtility;
import com.example.admin123.citytour.Fragments.SeeSights.Places.PlaceDetail;
import com.example.admin123.citytour.Fragments.SeeSights.Places.PlacesList;
import com.example.admin123.citytour.Fragments.SeeSights.SeeSightsFragment;
import com.example.admin123.citytour.R;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


/**
 * Created by theom on 13/03/2017.
 */
public class FragmentFavouriteInfo extends Fragment {
    private String placeReference;
    private GooglePlace place;
    private final static String TAG = "FavouriteFragment";
    private FavouritesDBHelper favouritesDB;
    private String placeTitle = "";
    private Bitmap locationPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_details, container, false);

        placeReference = getArguments().getString("placeReference");
        byte [] placeImage = getArguments().getByteArray("placeImage");
        DbBitmapUtility bitmapUtility = new DbBitmapUtility();
        locationPhoto = bitmapUtility.getImage(placeImage);

        favouritesDB = new FavouritesDBHelper(getActivity());

        String placesKey = getResources().getString(R.string.google_maps_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(getActivity(), "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();
        } else {
            PlacesDetailReadFeed process = new PlacesDetailReadFeed();
            String placeDetailRequest = "https://maps.googleapis.com/maps/api/place/details/json?" +
                    "key=" + placesKey + "&reference=" + placeReference;

            process.execute(new String[] {placeDetailRequest});
        }

        favouritesDB.close();
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
            ImageView iv = (ImageView) getView().findViewById(R.id.favourite_image);
            iv.setImageBitmap(locationPhoto);
        }
    }

    private void fillInLayout(GooglePlace place) {
        // title element has name and types
        TextView title = (TextView)getView().findViewById(R.id.name);
        placeTitle = place.getName();
        title.setText(placeTitle);

        Log.i("PLACES EXAMPLE", "Setting title to: " + title.getText());
        //address
        TextView address = (TextView) getView().findViewById(R.id.address_text_view);
        address.setText(place.getFormatted_address() + " " + place.getFormatted_phone_number());
        Log.i("PLACES EXAMPLE", "Setting address to: " + address.getText());
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


    private void getDb(){
        Cursor res = favouritesDB.getAllData();
        StringBuffer dbContents = new StringBuffer();
        while (res.moveToNext()){
            dbContents.append("Name :"+res.getString(0));
            dbContents.append(", Lat :"+res.getDouble(1));
            dbContents.append(", Long :"+res.getDouble(2));
            dbContents.append(", 5 :"+res.getBlob(6) + "\n");
        }
        Log.i("DB_Helper", dbContents.toString());
    }

    public interface OnSetLocationPhotoRef {
        public void setLocationPhotoRef(Drawable locationPhoto);
    }
}
