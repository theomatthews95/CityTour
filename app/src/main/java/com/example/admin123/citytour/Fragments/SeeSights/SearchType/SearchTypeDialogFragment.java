package com.example.admin123.citytour.Fragments.SeeSights.SearchType;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.admin123.citytour.Fragments.Currency.ChooseCurrencyDialog;
import com.example.admin123.citytour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 01/03/2017.
 */

public class SearchTypeDialogFragment extends DialogFragment{
    private ListView listView;
    private SearchTypeListAdapter listAdapter;
    private ArrayList<SearchTypeItem> searchTypeItems = new ArrayList<SearchTypeItem>();

    public interface OnSetSearchLocationTypeFromListener {
        public void setSearchLocationType(ArrayList<SearchTypeItem> searchLocationTypeArray);
    }

    public SearchTypeDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SearchTypeDialogFragment newInstance(String title) {
        SearchTypeDialogFragment dialog = new SearchTypeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Declare the view
        View v = getActivity().getLayoutInflater().inflate(R.layout.search_type_dialog, null);

        //Create the ListView with the Location Type items
        ListView listView = createLocationTypeListView(v);
        //Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);

        Button doneButton = (Button) v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                OnSetSearchLocationTypeFromListener callback = (OnSetSearchLocationTypeFromListener) getTargetFragment();
                callback.setSearchLocationType(listAdapter.getCheckedItems());
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public ListView createLocationTypeListView(View v){
        ListView lv;
        lv = (ListView) v.findViewById(R.id.listView1);
        searchTypeItems.add(new SearchTypeItem("Museum", "museum", 0));
        searchTypeItems.add(new SearchTypeItem("Restaurant", "restaurant", 1));
        searchTypeItems.add(new SearchTypeItem("Cafe", "cafe", 1));
        searchTypeItems.add(new SearchTypeItem("Library", "library", 0));
        searchTypeItems.add(new SearchTypeItem("Aquarium", "aquarium", 1));
        searchTypeItems.add(new SearchTypeItem("Shopping", "shopping_mall", 1));
        searchTypeItems.add(new SearchTypeItem("Hotels", "lodging", 0));
        listAdapter = new SearchTypeListAdapter(getActivity(), searchTypeItems);
        lv.setAdapter(listAdapter);
        return lv;
    }
}
