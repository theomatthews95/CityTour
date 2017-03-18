package com.example.admin123.citytour.Fragments.Favourites;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class FavouritesListFragment extends Fragment implements FavouriteListAdapter.MyViewHolder.OnItemClickListener{

    private RecyclerView recyclerView;
    private FavouriteListAdapter adapter;
    private FavouritesDBHelper favouritesDB;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favourites_list, container, false);

        //Create database to store favourite locations
        favouritesDB = new FavouritesDBHelper(getActivity());



        recyclerView = (RecyclerView) v.findViewById(R.id.favouritesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FavouriteListAdapter(getDB(), this);

        recyclerView.setAdapter(adapter);
        getDB();
        favouritesDB.close();
        return v;
    }

    private List<FavouriteListItem> getDB(){
        List<FavouriteListItem> data = new ArrayList<>();
        Cursor res = favouritesDB.getAllData();
        StringBuffer dbContents = new StringBuffer();

        if (res.getCount() == 0){
            String titleText="There are no favourites to display";
            FavouriteListItem current=new FavouriteListItem(titleText, "", 0.0, 0.0, null);
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

                FavouriteListItem current = new FavouriteListItem(titleText, reference, latitude, longitude, locationPhoto);

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
        /*if (actionMode == null) {

           actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);*/

        return true;
    }
  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favourite_menu_items, menu);
        MenuItem item = menu.findItem(R.id.favourite_menu_button);
        item.expandActionView();
        super.onCreateOptionsMenu(menu, inflater);
    }*/

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
            mode.getMenuInflater().inflate (R.menu.favourite_menu_items, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.favourite_menu_button:
                    // TODO: actually remove items
                    Log.d(TAG, "menu_remove");
                    mode.finish();
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
