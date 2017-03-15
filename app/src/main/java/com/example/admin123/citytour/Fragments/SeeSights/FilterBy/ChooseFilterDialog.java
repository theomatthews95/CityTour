package com.example.admin123.citytour.Fragments.SeeSights.FilterBy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.admin123.citytour.Fragments.Currency.ListHashmapAdapter;
import com.example.admin123.citytour.R;

import java.util.HashMap;

/**
 * Created by theom on 14/03/2017.
 */

public class ChooseFilterDialog extends DialogFragment{
    private EditText mEditText;
    HashMap<String, String> filtersHashmap = new HashMap<String, String>();
    ListHashmapAdapter listAdapter;
    ListView listView;

    //Interfaces to send data back to SeeSightsFragment
    public interface OnSetFiltersListener {
        public void setFilters(String filterBy, String filterTitle);
    }

    public ChooseFilterDialog(){
        filtersHashmap.put("Cheap", "maxprice=1");
        filtersHashmap.put("Expensive", "minprice=3");
        filtersHashmap.put("Reviews", "prominence");
        filtersHashmap.put("Open now", "opennow");
    }

    public static ChooseFilterDialog newInstance(String title){
        ChooseFilterDialog dialog = new ChooseFilterDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_dialog, null);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_list_dialog, null);
        listView = (ListView) v.findViewById(R.id.dialogListView);
        listAdapter = new ListHashmapAdapter(filtersHashmap);
        listView.setAdapter(listAdapter);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch (position){
                    default:
                        OnSetFiltersListener callback = (OnSetFiltersListener) getTargetFragment();
                        callback.setFilters(listAdapter.getItem(position).getValue(), listAdapter.getItem(position).getKey());
                        break;
                }
                //Code to close the dialog after selection
                dismiss();
            }

        });
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = "Choose a filter";
        //getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

}
