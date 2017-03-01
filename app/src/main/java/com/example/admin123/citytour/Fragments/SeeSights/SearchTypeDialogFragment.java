package com.example.admin123.citytour.Fragments.SeeSights;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin123.citytour.R;

import java.util.ArrayList;

/**
 * Created by theom on 01/03/2017.
 */

public class SearchTypeDialogFragment extends DialogFragment{
    private ListView listView;
    private SearchTypeListAdapter listAdapter;

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
        final View v = getActivity().getLayoutInflater().inflate(R.layout.search_type_dialog, null);

        //Create the ListView with the Location Type items
        createLocationTypeListView(v);

        //Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);

        Button doneButton = (Button) v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getCheckedItems(v);
            }
        });

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void createLocationTypeListView(View v){
        ListView lv;
        SearchTypeItem[] searchTypeItems;
        lv = (ListView) v.findViewById(R.id.listView1);
        searchTypeItems = new SearchTypeItem[6];
        searchTypeItems[0] = new SearchTypeItem("Museum", "museum", 0);
        searchTypeItems[1] = new SearchTypeItem("Restaurant", "restaurant", 1);
        searchTypeItems[2] = new SearchTypeItem("Cafe", "cafe", 1);
        searchTypeItems[3] = new SearchTypeItem("Library", "library", 0);
        searchTypeItems[4] = new SearchTypeItem("Aquarium", "aquarium", 1);
        searchTypeItems[5] = new SearchTypeItem("Shopping", "shopping_mall", 1);
        listAdapter = new SearchTypeListAdapter(getActivity(), searchTypeItems);
        lv.setAdapter(listAdapter);
        listView = lv;
    }

    public void getCheckedItems(View v){
        OnSetSearchLocationTypeFromListener callback = (OnSetSearchLocationTypeFromListener) getTargetFragment();
        callback.setSearchLocationType(listAdapter.getCheckedItems());
        System.out.println("HI");
    }

   /* public ArrayList<SearchTypeItem> getCheckedItems(){
        ArrayList<SearchTypeItem> checkedSearchItems = new ArrayList<SearchTypeItem>();
        int total = 0 ;
        int mListLength = listAdapter.getArray().length;
        for (int i = 0; i < mListLength ; i++) {
            CheckBox c = (CheckBox) listView.getChildAt(i);
            if (c.isChecked()) {
                checkedSearchItems.add(listView.getChildAt(i));
            }
        }
        return checkedSearchItems;
    }*/

}
