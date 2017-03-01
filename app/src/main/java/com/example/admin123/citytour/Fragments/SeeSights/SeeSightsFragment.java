package com.example.admin123.citytour.Fragments.SeeSights;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin123.citytour.Fragments.GmapFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeDialogFragment;
import com.example.admin123.citytour.Fragments.SeeSights.SearchType.SearchTypeItem;
import com.example.admin123.citytour.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeeSightsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeeSightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeSightsFragment extends Fragment implements View.OnClickListener, SearchTypeDialogFragment.OnSetSearchLocationTypeFromListener {


    private OnFragmentInteractionListener mListener;
    private String searchLocationType;

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
                SearchAreaDialogFragment dialog = SearchAreaDialogFragment.newInstance("Search area");
                dialog.show(getFragmentManager(), "fragmentDialog");
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

        Button mSearch = (Button) v.findViewById(R.id.search_map);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GmapFragment();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    //Set the search location type
    public void setSearchLocationType(ArrayList<SearchTypeItem> searchLocationTypeArray){
        String searchLocationType="";
        for(int i = 0; i<searchLocationTypeArray.size(); i++){
            searchLocationType = searchLocationType+"|"+searchLocationTypeArray.get(i).getSearchValue();
        }
        this.searchLocationType = searchLocationType;
        System.out.println(searchLocationType);
    }

    public void showLocationTypeDialog(){
        SearchTypeDialogFragment dialog = SearchTypeDialogFragment.newInstance("Point of interest type");
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "fragmentDialog");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
