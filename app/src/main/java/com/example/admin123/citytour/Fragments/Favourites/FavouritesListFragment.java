package com.example.admin123.citytour.Fragments.Favourites;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin123.citytour.DbBitmapUtility;
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
    private FavouritesDBHelper favouritesDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favourites_list, container, false);

        //Create database to store favourite locations
        favouritesDB = new FavouritesDBHelper(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.favouritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FavouriteListAdapter(getDB());

        recyclerView.setAdapter(adapter);
        getDB();
        favouritesDB.close();
        return v;
    }

 /*   public static List<FavouriteListItem> getData(){
        List<FavouriteListItem> data = new ArrayList<>();
        int [] icons = {R.drawable.ic_map_pin_aquarium, R.drawable.ic_map_pin_bar, R.drawable.ic_map_pin_cafe, R.drawable.ic_map_pin_food,R.drawable.ic_map_pin_aquarium, R.drawable.ic_map_pin_bar, R.drawable.ic_map_pin_cafe, R.drawable.ic_map_pin_food};
        String[] titles={"China", "US", "UK", "Europe","China", "US", "UK", "Europe"};
        for(int i=0; i<titles.length && i<icons.length; i++){
            FavouriteListItem current=new FavouriteListItem(icons[i], titles[i], "");
            current.iconId=icons[i];
            current.title=titles[i];
            data.add(current);
        }
        return data;
    }*/

    private List<FavouriteListItem> getDB(){
        List<FavouriteListItem> data = new ArrayList<>();
        Cursor res = favouritesDB.getAllData();
        StringBuffer dbContents = new StringBuffer();
        int [] icons = {R.drawable.ic_map_pin_aquarium, R.drawable.ic_map_pin_bar, R.drawable.ic_map_pin_cafe, R.drawable.ic_map_pin_food,R.drawable.ic_map_pin_aquarium, R.drawable.ic_map_pin_bar, R.drawable.ic_map_pin_cafe, R.drawable.ic_map_pin_food};
        //List<String> titles = new ArrayList<>();

        if (res.getCount() == 0){
            String titleText="There are no favourites to display";
            FavouriteListItem current=new FavouriteListItem(icons[0], titleText, "", 0.0, 0.0, null);
            current.title=titleText;
            current.iconId=icons[0];
            data.add(current);
        }
        int i = 0;

        while (res.moveToNext()){
            //titles.add(res.getString(0));
            String titleText=res.getString(0);
            Double latitude=res.getDouble(1);
            Double longitude=res.getDouble(2);
            String reference=res.getString(4);
            byte [] placeImage=res.getBlob(6);
            DbBitmapUtility bitmapUtility = new DbBitmapUtility();
            Bitmap locationPhoto = bitmapUtility.getImage(placeImage);

            FavouriteListItem current=new FavouriteListItem(icons[0], titleText, reference, latitude, longitude, locationPhoto);
            //current.title=titleText;
            current.iconId=icons[0];
            data.add(current);
            i++;
        }
        Log.i("Favouriites_list", dbContents.toString());

        return data;
    }
}
