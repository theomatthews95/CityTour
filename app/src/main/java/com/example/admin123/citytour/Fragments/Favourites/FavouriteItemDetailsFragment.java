package com.example.admin123.citytour.Fragments.Favourites;

import android.database.Cursor;
import android.os.Bundle;
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

import com.example.admin123.citytour.Fragments.PostcardFragment;
import com.example.admin123.citytour.R;

/**
 * Created by theom on 13/03/2017.
 */

public class FavouriteItemDetailsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String placeReference;
    private Boolean isFavourited=false;
    private FavouritesDBHelper favouritesDB;
    private Double locationLat;
    private Double locationLong;
    private String locationTitle;

    // TODO: Rename and change types and number of parameters
    public static FavouriteItemDetailsFragment newInstance() {
        FavouriteItemDetailsFragment fragment = new FavouriteItemDetailsFragment();
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

        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        setHasOptionsMenu(true);
        placeReference = getArguments().getString("placeReference");
        //Create database to store recent searches
        favouritesDB = new FavouritesDBHelper(getActivity());

        //Get longitude and latitude of location
        locationLat = getArguments().getDouble("lat");
        locationLong = getArguments().getDouble("long");
        locationTitle = getArguments().getString("title").replaceAll("\\s+","_").toLowerCase();

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
                    Fragment fragment = new FragmentFavouriteInfo();
                    Bundle bundle = new Bundle();
                    bundle.putString("placeReference",placeReference);
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    Fragment favEdits =  new FragmentFavouriteEdits();
                    Bundle bundle1 = new Bundle();
                    bundle1.putDouble("lat", locationLat);
                    bundle1.putDouble("long", locationLong);
                    bundle1.putString("locationTitle", locationTitle);
                    bundle1.putString("reference", placeReference);
                    bundle1.putBoolean("isFavourited", isFavourited);
                    favEdits.setTargetFragment(getParentFragment(), 0);
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
        boolean isInserted = favouritesDB.insertData(locationTitle, locationLat, locationLong, placeReference, "DEFAULT TEXT");

        if (isInserted == true) {
            Log.i("DB_Helper", "Inserted " + locationTitle + " " + locationLat + ", " + locationLong + ", " + placeReference + " into database.");
            Toast.makeText(getActivity(), "Favourited", Toast.LENGTH_SHORT).show();
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
}




