package com.example.admin123.citytour.Fragments.Favourites;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin123.citytour.DbBitmapUtility;
import com.example.admin123.citytour.Fragments.Itinerary.GoogleDirections;
import com.example.admin123.citytour.Map.GmapFragment;
import com.example.admin123.citytour.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouritesListFragment extends Fragment implements FavouriteListAdapter.MyViewHolder.OnItemClickListener{

    private RecyclerView recyclerView;
    private FavouriteListAdapter adapter;
    private FavouritesDBHelper favouritesDB;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favourites_list, container, false);

        //Create database to store favourite locations
        favouritesDB = new FavouritesDBHelper(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.favouritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button showOnMap = (Button) v.findViewById(R.id.show_on_map);
        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("itineraryList", getDB());
                bundle.putDouble("searchAreaLat", getDB().get(0).getLat());
                bundle.putDouble("searchAreaLong", getDB().get(0).getLong());

                Fragment fragment = new GmapFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.relativeLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        adapter = new FavouriteListAdapter(getDB(), this, false);

        recyclerView.setAdapter(adapter);
        getDB();
        favouritesDB.close();
        return v;
    }

    private ArrayList<FavouriteListItem> getDB(){
        ArrayList<FavouriteListItem> data = new ArrayList<>();
        Cursor res = favouritesDB.getAllData();
        StringBuffer dbContents = new StringBuffer();

        if (res.getCount() == 0){
            Drawable drawable = getActivity().getDrawable(R.drawable.no_image_available);
            DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
            byte[] image = dbBitmapUtility.getBytes(dbBitmapUtility.drawableToBitmap(drawable));
            String titleText="There are no favourites to display";
            FavouriteListItem current=new FavouriteListItem(titleText, "", 0.0, 0.0, image, "", "", false);
            data.add(current);
        }else {
            while (res.moveToNext()) {
                //titles.add(res.getString(0));
                String titleText = res.getString(0);
                Double latitude = res.getDouble(1);
                Double longitude = res.getDouble(2);
                String reference = res.getString(4);
                byte[] placeImage = res.getBlob(6);
                DbBitmapUtility bitmapUtility = new DbBitmapUtility();
                Bitmap locationPhoto = bitmapUtility.getImage(placeImage);

                Boolean isVisited = favouritesDB.isLocationVisited(titleText);

                FavouriteListItem current = new FavouriteListItem(titleText, reference, latitude, longitude, placeImage, "", "", isVisited);

                data.add(current);
            }
            Log.i("Favouriites_list", dbContents.toString());
        }
        return data;
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode =((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);

        return true;
    }


    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate (R.menu.menu_itinerary, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_itinerary:
                    ArrayList<FavouriteListItem> itineraryList = adapter.getItemsDetails(adapter.getSelectedItems());

                    if (itineraryList.size() >= 2) {
                        mode.finish();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("itineraryList", itineraryList);

                        Fragment fragment = new GoogleDirections();
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.relativeLayout, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }else{
                        Toast.makeText(getContext(),"You must select a minimum of 2 items for an itinerary.", Toast.LENGTH_SHORT).show();
                    }

                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
        }
    }

}
