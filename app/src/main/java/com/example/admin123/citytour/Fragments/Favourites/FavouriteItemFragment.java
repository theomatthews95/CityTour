package com.example.admin123.citytour.Fragments.Favourites;

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
import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;
import com.example.admin123.citytour.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 13/03/2017.
 */

public class FavouriteItemFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String placeReference;
    private Boolean isFavourited=false;
    private Boolean isVisited=false;
    private FavouritesDBHelper favouritesDB;
    private Double locationLat;
    private Double locationLong;
    private String locationTitle;
    private Drawable locationPhoto;
    static DbBitmapUtility bitmapUtility = new DbBitmapUtility();
    byte [] placeImage;
    private ArrayList<GooglePlace> places;

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

        //Display menu items
        setHasOptionsMenu(true);

        //Create database to store favourite locations
        favouritesDB = new FavouritesDBHelper(getActivity());

        //Get longitude and latitude of location
        locationLat = getArguments().getDouble("lat");
        locationLong = getArguments().getDouble("long");
        locationTitle = getArguments().getString("title");
        places = (ArrayList<GooglePlace>) getArguments().getSerializable("resultsFromMap");

        String launchedFrom = getArguments().getString("launchedFrom");

        if (launchedFrom.equals("Map_fragment")){
            String photoReference = getArguments().getString("photoReference");
            Log.i(TAG, "phootoreference is "+photoReference);
            locationPhoto = getLocationPhoto(photoReference);
            placeImage = bitmapUtility.getBytes(bitmapUtility.drawableToBitmap(locationPhoto));
            isFavourited = favouritesDB.isLocationFavourite(locationTitle);
        } else if (launchedFrom.equals("Favourite_list")){
            //the bytearray image sent from the favouritelist
            placeImage = getArguments().getByteArray("placeImage");
            isFavourited = getArguments().getBoolean("isFavourited");
        }

        if (isFavourited == true){
            isVisited = favouritesDB.isLocationVisited(locationTitle);
        }

        //Reference used to query google places for more location information
        placeReference = getArguments().getString("placeReference");

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
                    /*if (placeImage == null) {
                        placeImage = bitmapUtility.getBytes(bitmapUtility.drawableToBitmap(locationPhoto));
                    }*/
                    Fragment fragment = new FragmentFavouriteInfo();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("places", places);
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
        inflater.inflate(R.menu.favourite_item_menu, menu);
        MenuItem favouriteMenu = menu.findItem(R.id.favourite_menu_button);
        MenuItem visitedMenu = menu.findItem(R.id.visited);

        favouriteMenu.expandActionView();
        visitedMenu.expandActionView();

        changeIcon(favouriteMenu);
        changeIcon(visitedMenu);
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

                Bundle bundle = new Bundle();
                bundle.putByteArray("photo",placeImage);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();
                return true;
            case R.id.visited:
                isVisited=!isVisited;
                changeIcon(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void changeIcon(MenuItem item) {
        // Toggle location icon
        switch (item.getItemId()){
            case R.id.favourite_menu_button:
                if (isFavourited == true) {
                    item.setIcon(R.drawable.ic_like_true);
                    InsertFavouriteDatabase();
                } else {
                    item.setIcon(R.drawable.ic_like_false);
                    deleteLocation();
                }
                return;
            case R.id.visited:
                if (isVisited == true){
                    item.setIcon(R.drawable.ic_remove_circle_outline_black_24px);
                    updateIsVisited();
                } else {
                    item.setIcon(R.drawable.ic_done_black_24px);
                    updateIsVisited();
                }
                return;
            default:
        }



    }

    private void InsertFavouriteDatabase(){

        if(placeImage==null) {
             Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.birmingham_new_street);
             placeImage = bitmapUtility.getBytes(bm);
        }

        boolean isInserted = favouritesDB.insertData(locationTitle, locationLat, locationLong, placeReference, "DEFAULT TEXT", placeImage, isVisited, "");

        if (isInserted == true) {
            Log.i("DB_Helper", "Inserted " + locationTitle + " " + locationLat + ", " + locationLong + ", " + placeReference + ", "+ isVisited + " into database.");
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

    private Integer updateIsVisited(){
        Integer updateIsVisitedResults = favouritesDB.updateIsVisited(locationTitle, isVisited);
        Log.i("DB_Helper", "The database was updated with the text: "+isVisited+ " Result: "+updateIsVisitedResults);
        if (isVisited == false){
            Toast.makeText(getActivity(), "Not visited", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Visited", Toast.LENGTH_SHORT).show();
        }

        return updateIsVisitedResults;
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
            url = new URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&maxheight=400&photoreference="+photoReference+"&key="+placesKey);
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




