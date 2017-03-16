package com.example.admin123.citytour.Fragments.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin123.citytour.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouritesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favourites_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.favouritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FavouriteListAdapter(getData());
        recyclerView.setAdapter(adapter);
        return v;
    }

    public static List<FavouriteListItem> getData(){
        List<FavouriteListItem> data = new ArrayList<>();
        int [] icons = {R.drawable.ic_map_pin_aquarium, R.drawable.ic_map_pin_bar, R.drawable.ic_map_pin_cafe, R.drawable.ic_map_pin_food};
        String[] titles={"China", "US", "UK", "Europe"};
        for(int i=0; i<titles.length && i<icons.length; i++){
            FavouriteListItem current=new FavouriteListItem(icons[i], titles[i]);
            current.iconId=icons[i];
            current.title=titles[i];
            data.add(current);
        }
        return data;
    }
}
