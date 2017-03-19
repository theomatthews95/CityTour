package com.example.admin123.citytour.Fragments.Itinerary;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.Fragments.Favourites.FavouriteListAdapter;
import com.example.admin123.citytour.Fragments.Favourites.FavouriteListItem;
import com.example.admin123.citytour.Fragments.Favourites.FavouritesDBHelper;
import com.example.admin123.citytour.Fragments.Favourites.FavouritesListFragment;
import com.example.admin123.citytour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 18/03/2017.
 */

public class ItineraryFragment extends Fragment implements FavouriteListAdapter.MyViewHolder.OnItemClickListener{

    private RecyclerView recyclerView;
    private FavouriteListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_confirm_itinerary, container, false);
        final ArrayList<FavouriteListItem> itineraryList = (ArrayList<FavouriteListItem>) getArguments().getSerializable("itineraryList");

        recyclerView = (RecyclerView) v.findViewById(R.id.favouritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button confirmItinerary = (Button) v.findViewById(R.id.confirm_itinerary);
        confirmItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("itineraryList", itineraryList);
                Fragment fragment = new GoogleDirections();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.relativeLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        adapter = new FavouriteListAdapter(itineraryList, this);
        recyclerView.setAdapter(adapter);
        return v;
    }


    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }
}
