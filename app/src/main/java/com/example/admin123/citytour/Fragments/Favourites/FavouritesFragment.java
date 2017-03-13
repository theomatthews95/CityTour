package com.example.admin123.citytour.Fragments.Favourites;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;
import com.example.admin123.citytour.R;
import com.google.gson.Gson;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 13/03/2017.
 */

public class FavouritesFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    GooglePlace place;

    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
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
        place = (GooglePlace) getArguments().getSerializable("place");

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

        return v;
    }

    private class FavouritesAdapter extends FragmentPagerAdapter {
        private String fragments [] = {"Info", "Notes"};

        public FavouritesAdapter(FragmentManager supportFragmentManager, FragmentActivity activity) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment fragment = new FragmentFavouriteInfo();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("place",place);
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    return new FragmentFavouriteEdits();
                default:

                    System.out.println("Hi3");
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
}




