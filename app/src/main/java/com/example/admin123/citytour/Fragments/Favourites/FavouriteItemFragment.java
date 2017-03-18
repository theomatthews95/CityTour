package com.example.admin123.citytour.Fragments.Favourites;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.Fragments.PostcardFragment;
import com.example.admin123.citytour.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 13/03/2017.
 */

public class FavouriteItemFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String placeReference;
    private Boolean isFavourited=false;
    private FavouritesDBHelper favouritesDB;
    private Double locationLat;
    private Double locationLong;
    private String locationTitle;
    private Drawable locationPhoto;
    static DbBitmapUtility bitmapUtility = new DbBitmapUtility();
    byte [] placeImage;

    // TODO: Rename and change types and number of parameters
    public static FavouriteItemFragment newInstance() {
        FavouriteItemFragment fragment = new FavouriteItemFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favourite_item, container, false);
        setHasOptionsMenu(true);
        //Reference used to query google places for more location information
        placeReference = getArguments().getString("placeReference");
        String photoReference = getArguments().getString("photoReference");
        isFavourited = getArguments().getBoolean("isFavourited");

        //the bytearray image sent from the favouritelist
        placeImage = getArguments().getByteArray("placeImage");
        //Create database to store favourite locations
        favouritesDB = new FavouritesDBHelper(getActivity());

        //Get longitude and latitude of location
        locationLat = getArguments().getDouble("lat");
        locationLong = getArguments().getDouble("long");
        locationTitle = getArguments().getString("title");
        locationPhoto = getLocationPhoto(photoReference);

        viewPager = (ViewPager) v.findViewById(R.id.favouritesViewPager);
        viewPager.setAdapter(new FavouritesAdapter(getChildFragmentManager(), getActivity()));

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

        });

        favouritesDB.close();

        return v;
    }


    private class FavouritesAdapter extends FragmentPagerAdapter {
        private String fragments [] = {"Information", "Notes"};

        public FavouritesAdapter(FragmentManager supportFragmentManager, FragmentActivity activity) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if (placeImage == null) {
                        placeImage = bitmapUtility.getBytes(bitmapUtility.drawableToBitmap(locationPhoto));
                    }
                    Fragment fragment = new FragmentFavouriteInfo();
                    Bundle bundle = new Bundle();
                    bundle.putString("placeReference",placeReference);
                    bundle.putByteArray("placeImage", placeImage);
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    Fragment favEdits =  new FragmentFavouriteEdits();
                    Bundle bundle1 = new Bundle();
                    bundle1.putDouble("lat", locationLat);
                    bundle1.putDouble("long", locationLong);
                    bundle1.putString("locationTitle", locationTitle);
                    bundle1.putString("placeReference", placeReference);
                    //Boolean to tell app if user can save notes by saying if it is in the DB
                    bundle1.putBoolean("isFavourited", isFavourited);
                    favEdits.setArguments(bundle1);
                    return favEdits;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
                return fragments[position];
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favourite_menu_items, menu);
        MenuItem item = menu.findItem(R.id.favourite_menu_button);
        item.expandActionView();
        changeIcon(item);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favourite_menu_button:
                isFavourited=!isFavourited;
                changeIcon(item);
                return true;
            case R.id.create_postcard:
                Fragment fragment = new PostcardFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void changeIcon(MenuItem item) {
        // Toggle location icon
        if (isFavourited == true) {
            item.setIcon(R.drawable.ic_like_true);
            InsertFavouriteDatabase();
        } else {
            item.setIcon(R.drawable.ic_like_false);
            deleteLocation();
        }
    }

    private void InsertFavouriteDatabase(){

        byte[] placeImage;
        if(locationPhoto==null) {
             Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
             placeImage = bitmapUtility.getBytes(bm);
        }else{
            //Convert drawable to bitmap, then bitmap to byte array
            placeImage = bitmapUtility.getBytes(bitmapUtility.drawableToBitmap(locationPhoto));
        }

        boolean isInserted = favouritesDB.insertData(locationTitle, locationLat, locationLong, placeReference, "DEFAULT TEXT", placeImage);

        if (isInserted == true) {
            Log.i("DB_Helper", "Inserted " + locationTitle + " " + locationLat + ", " + locationLong + ", " + placeReference + " into database.");
            Toast.makeText(getActivity(), "Favourited", Toast.LENGTH_SHORT).show();
            //getDb();
        }else {
            Log.i("DB_Helper", "Location not inserted into database. Most likely a duplicate.");
        }
    }

    public void getFavouritesDB(){
        Cursor res = favouritesDB.getAllData();
    }

    public void deleteLocation(){
        Integer isDeleted = favouritesDB.deleteValue(locationTitle);
        if (isDeleted == 1){
            Log.i("DB_Helper", locationTitle +" has been deleted from the database");
            Toast.makeText(getActivity(), "Unfavourited", Toast.LENGTH_SHORT).show();
        }else{
            Log.i("DB_Helper", "Failed to delete "+locationTitle);
        }
    }

    private void getDb(){
        Cursor res = favouritesDB.getAllData();
        StringBuffer dbContents = new StringBuffer();
        while (res.moveToNext()){
            dbContents.append("Name :"+res.getString(0));
            dbContents.append(", Lat :"+res.getDouble(1));
            dbContents.append(", Long :"+res.getDouble(2));
            dbContents.append(", 3 :"+res.getDouble(3));
            dbContents.append(", 4 :"+res.getString(4));
            dbContents.append(", 5 :"+res.getString(5));
            dbContents.append(", Bitmap :"+res.getBlob(6) + "\n");
        }
        Log.i("DB_Helper", dbContents.toString());
    }

    private void setLocationPhotoDB(Drawable locationPhoto){

        //Convert drawable to bitmap, then bitmap to byte array
        byte[] locationPhotobitmap = bitmapUtility.getBytes(bitmapUtility.drawableToBitmap(locationPhoto));

        Integer result = favouritesDB.addLocationPhoto(locationTitle, locationPhotobitmap);
        if(result!=1){
            Log.i(TAG, "Failed to insert photo");
        }else{
            Log.i(TAG, "Inserted photo");
        }

    }

    private Drawable getLocationPhoto(String photoReference){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        URL url = null;
        String placesKey = getResources().getString(R.string.google_maps_key);

        try {
            url = new URL("https://maps.googleapis.com/maps/api/place/photo?maxheight=4000&photoreference="+photoReference+"&key="+placesKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream content = null;
        try {
            content = (InputStream)url.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable locationPhoto = Drawable.createFromStream(content , "src");

        return locationPhoto;
    }
}




