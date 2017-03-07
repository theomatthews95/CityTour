package com.example.admin123.citytour;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.admin123.citytour.Fragments.Currency.CurrencyExchangeFragment;
import com.example.admin123.citytour.Fragments.HomepageFragment;
import com.example.admin123.citytour.Fragments.GmapFragment;
import com.example.admin123.citytour.Fragments.PostcardFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SeeSightsFragment;
import com.google.android.gms.location.LocationListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomepageFragment.OnFragmentInteractionListener, SeeSightsFragment.OnFragmentInteractionListener, PostcardFragment.OnFragmentInteractionListener, CurrencyExchangeFragment.OnFragmentInteractionListener, GmapFragment.OnFragmentInteractionListener{

    ListView listView;
    ArrayAdapter<String> listAdapter;
    String fragmentArray[] = {"Homepage", "See the Sights", "Favourite", "Itinerary", "Postcard", "Currency Exchange"};
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_draw);

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fragmentArray);
        listView.setAdapter(listAdapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        Fragment fragment = new HomepageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Fragment fragment;
                switch (position){
                    case 0:
                        fragment = new HomepageFragment();
                        break;
                    case 1:
                        fragment = new SeeSightsFragment();
                        break;
                    case 2:
                        fragment = new HomepageFragment();
                        break;
                    case 3:
                        fragment = new HomepageFragment();
                        break;
                    case 4:
                        fragment = new PostcardFragment();
                        break;
                    case 5:
                        fragment = new CurrencyExchangeFragment();
                        break;
                    default:
                        fragment = new HomepageFragment();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();
                drawerLayout.closeDrawers();
            }

        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
