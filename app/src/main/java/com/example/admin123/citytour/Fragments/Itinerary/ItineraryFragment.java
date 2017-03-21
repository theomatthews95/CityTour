package com.example.admin123.citytour.Fragments.Itinerary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.Fragments.Favourites.FavouriteListAdapter;
import com.example.admin123.citytour.Fragments.Favourites.FavouriteListItem;
import com.example.admin123.citytour.Fragments.Favourites.FavouritesDBHelper;
import com.example.admin123.citytour.Fragments.PostcardFragment;
import com.example.admin123.citytour.Map.GmapFragment;
import com.example.admin123.citytour.R;

import java.util.ArrayList;

/**
 * Created by theom on 18/03/2017.
 */

public class ItineraryFragment extends Fragment implements FavouriteListAdapter.MyViewHolder.OnItemClickListener{

    private RecyclerView recyclerView;
    private FavouriteListAdapter adapter;
    private String polyline;
    private FavouritesDBHelper favouritesDB;
    private ArrayList<FavouriteListItem> itineraryList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_itinerary_list, container, false);
        itineraryList = (ArrayList<FavouriteListItem>) getArguments().getSerializable("initialItinList");
        final ItineraryList googleDirectionResults = (ItineraryList) getArguments().getSerializable("googleDirectionResults");
        ArrayList<Integer> waypointOrder = googleDirectionResults.getRoutes().get(0).getWaypoint_order();
        polyline = getArguments().getString("polyline");

        //Create database to check if visited
        favouritesDB = new FavouritesDBHelper(getActivity());

        //Create a new list that holds the optimized order of the waypoints
        ArrayList<FavouriteListItem> newOrder = new ArrayList<FavouriteListItem>();
        newOrder.add(updateIsVisited(itineraryList.get(0)));
        for (int i = 0; i<waypointOrder.size(); i++){
            //Itinerary item position in array
            FavouriteListItem item = updateIsVisited(itineraryList.get(2+waypointOrder.get(i)));
            newOrder.add(item);
        }
        newOrder.add(updateIsVisited(itineraryList.get(1)));

        //Icon to display on the side of itinerary cards
        DbBitmapUtility bitmapUtility = new DbBitmapUtility();
        byte [] image = bitmapUtility.getBytes(bitmapUtility.drawableToBitmap(getResources().getDrawable(R.drawable.ic_itinerary)));

        ArrayList<FavouriteListItem> timingCards = new ArrayList<FavouriteListItem>();
        for(int j = 0; j<googleDirectionResults.getRoutes().get(0).getLegs().size(); j++){
            String distanceToNext = googleDirectionResults.getRoutes().get(0).getLegs().get(j).getDistance().getText();
            String timeToNext = googleDirectionResults.getRoutes().get(0).getLegs().get(j).getDuration().getText();

            FavouriteListItem fi = new FavouriteListItem("There are no favourites to display", "", itineraryList.get(0).getLat(), itineraryList.get(0).getLong(),image,distanceToNext, timeToNext, false);
            timingCards.add(fi);
        }

        ArrayList<FavouriteListItem> finalOrder = merge(newOrder, timingCards);

        recyclerView = (RecyclerView) v.findViewById(R.id.favouritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button confirmItinerary = (Button) v.findViewById(R.id.show_on_map);
        confirmItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("itineraryList", itineraryList);
                bundle.putString("polyline", polyline);
                bundle.putDouble("searchAreaLat", itineraryList.get(0).getLat());
                bundle.putDouble("searchAreaLong", itineraryList.get(0).getLong());

                Fragment fragment = new GmapFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.relativeLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        favouritesDB.close();

        adapter = new FavouriteListAdapter(finalOrder, this, true);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private static ArrayList merge(ArrayList a, ArrayList b) {
        int c1 = 0, c2 = 0;
        ArrayList<FavouriteListItem> res = new ArrayList<FavouriteListItem>();

        while(c1 < a.size() || c2 < b.size()) {
            if(c1 < a.size())
                res.add((FavouriteListItem) a.get(c1++));
            if(c2 < b.size())
                res.add((FavouriteListItem) b.get(c2++));
        }
        return res;
    }


    //Check if the item has been visited since creating the itinerary list
    private FavouriteListItem updateIsVisited(FavouriteListItem current){
        String titleText = current.getTitle();
        boolean isVisited = favouritesDB.isLocationVisited(titleText);
        FavouriteListItem item = current.updateVisited(isVisited);
        return item;
    }


    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }
}
