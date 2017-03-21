package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin123.citytour.Fragments.Currency.ChooseCurrencyDialog;
import com.example.admin123.citytour.Fragments.Currency.ListHashmapAdapter;
import com.example.admin123.citytour.Fragments.SeeSights.Places.PlacesList;
import com.example.admin123.citytour.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by theom on 10/02/2017.
 */

public class SearchAreaDialogFragment extends DialogFragment
        implements DialogInterface.OnDismissListener {

    private SeekBar seekbarDistance;
    private TextView textViewDistance;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private LatLng searchCoordinates;
    private double searchLong;
    private double searchLat;
    private Integer searchRadius;
    private String searchAreaName;
    private RecentLocationsDBHelper locationsDB;
    private ArrayList<SearchAreaItem> recentLocationsArrayList;
    private static final String TAG = "SearchAreaDialog";
    SearchAreaListAdapter listAdapter;
    ListView listView;

    //Callback method to return data to SeeSightsFragment
    public interface OnSetSearchLocationAreaFromListener {
        public void setSearchLocationArea(SearchAreaItem searchLocationArea);
    }

    public SearchAreaDialogFragment() {

    }

    public static SearchAreaDialogFragment newInstance(String title) {
        SearchAreaDialogFragment dialog = new SearchAreaDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Declare the view
        View v = getActivity().getLayoutInflater().inflate(R.layout.search_area_dialog, null);

        recentLocationsArrayList = new ArrayList<SearchAreaItem>();

        //Create database to store recent searches
        locationsDB = new RecentLocationsDBHelper(getActivity());

        //Create the Google Places AutoComplete Widget
        autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //Listener for Google Places AutoComplete widget
        autoCompleteProcess();

        updateMostRecentLocations();

        listView = (ListView) v.findViewById(R.id.recentLocationsList);
        listAdapter = new SearchAreaListAdapter(recentLocationsArrayList);
        //listView.setEmptyView( v.findViewById( R.id.empty_list_view ) );
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch (position){
                    default:
                        searchCoordinates = listAdapter.getItem(position).getSearchCoordinates();
                        searchAreaName = listAdapter.getItem(position).getName();
                        autocompleteFragment.setText(searchAreaName);
                        break;
                }}
        });

        TextView myLocation = (TextView) v.findViewById(R.id.myLocation);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autocompleteFragment.setText("My Location");
            }
        });


        //Seekbar for sliding input of radius
        seekbarDistance = (SeekBar) v.findViewById(R.id.seekbarDistance);
        //Listener for the Seekbar sliding input of radius
        seekBarProcess(v);

        //Button to submit the user inputs from the search area dialog
        Button doneButton = (Button) v.findViewById(R.id.doneButton);

        //Listener for button to submit user input from dialog
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(searchCoordinates == null){
                    searchCoordinates = new LatLng(0.0,0.0);
                }
                if(searchRadius == null){
                    searchRadius = 2000;
                }

                Log.i(TAG,"The Search item on DONE of Area dialog: Coordinates "+searchCoordinates+" Name "+searchAreaName);
                //Create SearchAreaItem object to store data input from user in dialog fragment
                SearchAreaItem searchAreaItem = new SearchAreaItem(searchCoordinates, searchRadius, searchAreaName);
                SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener callback = (SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener) getTargetFragment();

                //Callback submit for user's inputted data
                callback.setSearchLocationArea(searchAreaItem);

                InsertRecentDatabase(searchAreaName);
                locationsDB.close();
                dismiss();
            }
        });

        //Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);

        return builder.create();
    }

    private void InsertRecentDatabase(String searchAreaName){
        if (searchAreaName == null || searchAreaName.equals("My Location")){
            Log.i(TAG, "Not gonna insert my location or null biatch");
        }else {
            boolean isInserted = locationsDB.insertData(searchAreaName, searchLat, searchLong);
            if (isInserted == true)
                Log.i(TAG, "Inserted " + searchAreaName + " into database.");
            else
                Log.i(TAG, "Location not inserted into database. Most likely a duplicate.");

            updateMostRecentLocations();
        }
    }


    public void updateMostRecentLocations(){
        Cursor res = locationsDB.getRecent5();
        StringBuffer recent5 = new StringBuffer();

        if (res.getCount() == 0){
            return;
        }
        recent5.append("Recent 5 Start"+"\n");
        while (res.moveToNext()){
            LatLng coordinates = new LatLng(res.getDouble(1), res.getDouble(2));
            String locationName = res.getString(0);
            SearchAreaItem item = new SearchAreaItem(coordinates, 0, locationName);
            recentLocationsArrayList.add(item);

            recent5.append("Name :"+res.getString(0));
            recent5.append(", Lat :"+res.getDouble(1));
            recent5.append(", Long :"+res.getDouble(2));
            recent5.append(", Timestamp :"+res.getString(3) + "\n");
        }

        recent5.append("Recent 5 End"+"\n");
        //Log.i(TAG, recent5.toString());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void cleanUp(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        cleanUp();
    }

    public void autoCompleteProcess(){
        //Listener for Google Places AutoComplete widget
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());//get place details here
                searchCoordinates = place.getLatLng();
                searchAreaName = place.getName().toString();
                searchLong = place.getLatLng().longitude;
                searchLat = place.getLatLng().latitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void seekBarProcess(View v){
        seekbarDistance.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value=progress;
                        searchRadius=progress;
                        textViewDistance.setText("Search radius: " + progress + " meters");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        searchRadius=progress_value;
                        textViewDistance.setText("Search radius: " +progress_value + " meters");
                    }
                }
        );

        //TextView to display user input on slider
        textViewDistance = (TextView) v.findViewById(R.id.textViewDistance);
        textViewDistance.setText("Search radius: " + seekbarDistance.getProgress() + " meters");
    }

}
