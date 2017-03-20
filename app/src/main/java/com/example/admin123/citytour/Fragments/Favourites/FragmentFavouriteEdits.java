package com.example.admin123.citytour.Fragments.Favourites;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin123.citytour.Fragments.Currency.ChooseCurrencyDialog;
import com.example.admin123.citytour.Fragments.SeeSights.SearchArea.SearchAreaItem;
import com.example.admin123.citytour.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by theom on 13/03/2017.
 */
public class FragmentFavouriteEdits extends Fragment {

    private FavouritesDBHelper favouritesDB;
    private EditText userNotesEditText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_edits,container,false);

        favouritesDB = new FavouritesDBHelper(getActivity());
        final String locationTitle  = getArguments().getString("locationTitle");
        final Double locationLat = getArguments().getDouble("lat");
        final Double locationLong = getArguments().getDouble("long");
        final String placeReference = getArguments().getString("placeReference");
        userNotesEditText = (EditText) v.findViewById(R.id.user_notes);
        getNotes(locationTitle);
        //getNotes(locationTitle);

        Button saveNotesButton = (Button) v.findViewById(R.id.save_notes_button);

        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //Toast.makeText(getContext(), "Saved notes: "+userNotesEditText.getText(), Toast.LENGTH_SHORT).show();
                /*OnSetUserNotesListener callback = (OnSetUserNotesListener) getTargetFragment();
                callback.setUserNotes(userNotesEditText.getText().toString());*/

                Integer isUpdate = updateDB(locationTitle, userNotesEditText.getText().toString());
                if (isUpdate == 1){
                    Toast.makeText(getContext(), "Saved notes", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "You must favourite the item before you can save notes", Toast.LENGTH_SHORT).show();
                }
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        favouritesDB.close();

        return v;
    }

    private void getNotes(String locationTitle){
        Cursor res = favouritesDB.getLocationData(locationTitle);
        StringBuffer dbContents = new StringBuffer();

        if (res.getCount() == 0){
            return;
        }
        while (res.moveToNext()){
            dbContents.append(res.getString(5));
        }
        String userNotes = dbContents.toString();
        if (!userNotes.equals("DEFAULT TEXT")){
            userNotesEditText.setText(userNotes);
        }
    }

    private void getDB(){
        Cursor res = favouritesDB.getAllData();
        StringBuffer dbContents = new StringBuffer();

        if (res.getCount() == 0){
            return;
        }
        while (res.moveToNext()){
            dbContents.append("Name :"+res.getString(0));
            dbContents.append(", Lat :"+res.getDouble(1));
            dbContents.append(", Long :"+res.getDouble(2));
            dbContents.append(", 3 :"+res.getDouble(3));
            dbContents.append(", 4 :"+res.getDouble(4));
            dbContents.append(", User notes :"+res.getString(5) + "\n");
        }
       /* String userNotes = res.getString(5);
        if (!userNotes.equals("DEFAULT TEXT")){
            userNotesEditText.setText(userNotes);
        }*/
        Log.i("DB_Helper", dbContents.toString());
    }

    private Integer updateDB(String locationName, String userNotes){
        Integer updateNotesResults = favouritesDB.updateUserNotes(locationName, userNotes);
        Log.i("DB_Helper", "The database was updated with the text: "+userNotes+ " Result: "+updateNotesResults);
        return updateNotesResults;
    }

}
