package com.example.admin123.citytour.Fragments.SeeSights;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin123.citytour.Fragments.SeeSights.Places.PlacesList;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaDialogFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaItem;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeDialogFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeItem;
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

import static com.google.android.gms.wearable.DataMap.TAG;

public class SeeSightsFragment extends Fragment implements View.OnClickListener,
        SearchTypeDialogFragment.OnSetSearchLocationTypeFromListener,
        SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener,
        GoogleApiClient.OnConnectionFailedListener{


    private OnFragmentInteractionListener mListener;
    private String searchLocationType;
    private TextView whatTypeTextView;
    private double searchLat;
    private double searchLong;
    private String searchRadius;


    // TODO: Rename and change types and number of parameters
    public static SeeSightsFragment newInstance() {
        SeeSightsFragment fragment = new SeeSightsFragment();
        return fragment;
    }

    public SeeSightsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_see_sights, container, false);

        // Configure what area is search
        Button mWhatArea = (Button) v.findViewById(R.id.what_area_button);
        mWhatArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showLocationAreaDialog();
            }
        });

        // Configure what type of location is being searched for
        Button mWhatType = (Button) v.findViewById(R.id.what_type_button);
        mWhatType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showLocationTypeDialog();
            }
        });

        whatTypeTextView = (TextView) v.findViewById(R.id.what_type_search_editText);

        Button mSearch = (Button) v.findViewById(R.id.search_map);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String locationType = searchLocationType;
                bundle.putString("locationType", locationType);
                bundle.putString("searchRadius",searchRadius);
                bundle.putDouble("searchAreaLong", searchLong);
                bundle.putDouble("searchAreaLat", searchLat);
                Fragment fragment = new PlacesList();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.relativeLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    public void showLocationTypeDialog(){
        SearchTypeDialogFragment dialog = SearchTypeDialogFragment.newInstance("Point of interest type");
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "fragmentDialog");
    }

    public void showLocationAreaDialog(){
        SearchAreaDialogFragment dialog = SearchAreaDialogFragment.newInstance("Search area");
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "fragmentDialog");
    }
    public void setSearchLocationArea(SearchAreaItem searchAreaItem){
        searchRadius = searchAreaItem.getRadius().toString();
        searchLat = searchAreaItem.getSearchCoordinates().longitude;
        searchLong = searchAreaItem.getSearchCoordinates().latitude;

    }

    //Set the search location type
    public void setSearchLocationType(ArrayList<SearchTypeItem> searchLocationTypeArray){
        String searchLocationType="";
        String visibleSearchLocationType="";

        //Iterate over the returned array list from the dialogfragmet to create a query string for places API
        for(int i = 0; i<searchLocationTypeArray.size(); i++){
            if(i == 0){
                searchLocationType = searchLocationTypeArray.get(i).getSearchValue();
                visibleSearchLocationType = searchLocationTypeArray.get(i).getName();
            }else {
                searchLocationType = searchLocationType + "|" + searchLocationTypeArray.get(i).getSearchValue();
                visibleSearchLocationType = visibleSearchLocationType + ", " + searchLocationTypeArray.get(i).getName();
            }
        }

        //change the text view under the dialog launch button to show the user their selection
        whatTypeTextView.setText(visibleSearchLocationType);
        this.searchLocationType = searchLocationType;
        System.out.println(searchLocationType);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
