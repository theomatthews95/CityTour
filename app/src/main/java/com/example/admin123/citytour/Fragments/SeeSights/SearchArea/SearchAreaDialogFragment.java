package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import com.example.admin123.citytour.Fragments.Currency.ListHashmapAdapter;
import com.example.admin123.citytour.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by theom on 10/02/2017.
 */

public class SearchAreaDialogFragment extends DialogFragment {

    private ListView listView;
    private SearchAreaListAdapter listAdapter;
    private ArrayList<SearchAreaItem> searchAreaItems = new ArrayList<SearchAreaItem>();
    private SeekBar seekbarDistance;
    private TextView textViewDistance;

    public interface OnSetSearchLocationAreaFromListener {
        public void setSearchLocationArea(ArrayList<SearchAreaItem> searchLocationAreaArray);
    }

    public SearchAreaDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
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


        seekbarDistance = (SeekBar) v.findViewById(R.id.seekbarDistance);
        textViewDistance = (TextView) v.findViewById(R.id.textViewDistance);
        textViewDistance.setText("Search radius: " + seekbarDistance.getProgress() + " miles");

        seekbarDistance.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value=progress;
                        textViewDistance.setText("Search radius: " + progress + " miles");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textViewDistance.setText("Search radius: " +progress_value + " miles");
                    }
                }
        );
        //Create the ListView with the Location Area items
       // ListView listView = createLocationAreaListView(v);
        //Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);

        Button doneButton = (Button) v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener callback = (SearchAreaDialogFragment.OnSetSearchLocationAreaFromListener) getTargetFragment();
                callback.setSearchLocationArea(listAdapter.getCheckedItems());*/
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }


   /* public ListView createLocationAreaListView(View v){
        ListView lv;
        lv = (ListView) v.findViewById(R.id.listViewArea);
        searchAreaItems.add(new SearchAreaItem("Museum", "museum", 0));
        searchAreaItems.add(new SearchAreaItem("Restaurant", "restaurant", 1));
        searchAreaItems.add(new SearchAreaItem("Cafe", "cafe", 1));
        searchAreaItems.add(new SearchAreaItem("Library", "library", 0));
        searchAreaItems.add(new SearchAreaItem("Aquarium", "aquarium", 1));
        searchAreaItems.add(new SearchAreaItem("Shopping", "shopping_mall", 1));
        listAdapter = new SearchAreaListAdapter(getActivity(), searchAreaItems);
        lv.setAdapter(listAdapter);
        return lv;
    }*/
}
