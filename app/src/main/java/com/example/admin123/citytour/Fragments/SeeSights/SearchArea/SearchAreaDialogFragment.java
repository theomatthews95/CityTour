package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.Fragments.Currency.ListHashmapAdapter;
import com.example.admin123.citytour.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 10/02/2017.
 */

public class SearchAreaDialogFragment extends DialogFragment implements DialogInterface.OnDismissListener {

    private ListView listView;
    private HashMap<String, String> searchAreaHashMap = new HashMap<String, String>();
    private SearchAreaListAdapter listAdapter;
    private SeekBar seekbarDistance;
    private TextView textViewDistance;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private LatLng searchCoordinates;
    private Integer searchRadius;

    public interface OnSetSearchLocationAreaFromListener {
        public void setSearchLocationArea(SearchAreaItem searchLocationArea);
    }

    public SearchAreaDialogFragment() {
        searchAreaHashMap.put("Birmingham", "birmingham");
        searchAreaHashMap.put("London", "london");
        searchAreaHashMap.put("Oxford", "oxford");
        searchAreaHashMap.put("Manchester", "manchester");
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

        autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setText("My Location");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());//get place details here
                searchCoordinates = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        seekbarDistance = (SeekBar) v.findViewById(R.id.seekbarDistance);
        textViewDistance = (TextView) v.findViewById(R.id.textViewDistance);
        textViewDistance.setText("Search radius: " + seekbarDistance.getProgress() + " meters");

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

        //Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);

        Button doneButton = (Button) v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(searchCoordinates == null){
                    searchCoordinates = new LatLng(51.7520209,-1.2577262999999999);
                }
                if(searchRadius == null){
                    searchRadius = 1;
                }
                SearchAreaItem searchAreaItem = new SearchAreaItem(searchCoordinates, searchRadius);
                SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener callback = (SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener) getTargetFragment();
                callback.setSearchLocationArea(searchAreaItem);

                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void cleanUp(){
        getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        cleanUp();
    }


}
