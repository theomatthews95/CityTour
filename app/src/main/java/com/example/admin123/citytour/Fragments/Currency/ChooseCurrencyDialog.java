package com.example.admin123.citytour.Fragments.Currency;

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
import android.widget.EditText;
import android.widget.ListView;

import com.example.admin123.citytour.R;

import java.util.HashMap;

/**
 * Created by theom on 10/02/2017.
 */

public class ChooseCurrencyDialog extends DialogFragment {

    private EditText mEditText;
    HashMap<String, String> currenciesCodeMap = new HashMap<String, String>();
    ListHashmapAdapter listAdapter;
    ListView listView;

    //Interfaces to send data back to CurrencyExchangeFragment
    public interface OnSetConvertFromListener {
        public void setConvertFrom(String conversion);
    }
    public interface OnSetConvertToListener {
        public void setConvertTo(String conversion);
    }

    public ChooseCurrencyDialog() {
        currenciesCodeMap.put("$ US Dollar", "USD");
        currenciesCodeMap.put("£ Pound Sterling", "GBP");
        currenciesCodeMap.put("¥ Chinese Yuan", "CNY");
        currenciesCodeMap.put("€ Euros", "EUR");
    }

    public static ChooseCurrencyDialog newInstance(String title, boolean isConvertFrom) {
        ChooseCurrencyDialog dialog = new ChooseCurrencyDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("isConvertFrom", isConvertFrom);
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
        listAdapter = new ListHashmapAdapter(currenciesCodeMap);
        listView.setAdapter(listAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setView(v);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id){
               switch (position){
                   default:
                       boolean isConvertFrom = getArguments().getBoolean("isConvertFrom");
                       System.out.println(listAdapter.getItem(position).getValue());
                       if (isConvertFrom == true) {
                           OnSetConvertFromListener callback = (OnSetConvertFromListener) getTargetFragment();
                           callback.setConvertFrom(listAdapter.getItem(position).getValue());
                       }else{
                           OnSetConvertToListener callback = (OnSetConvertToListener) getTargetFragment();
                           callback.setConvertTo(listAdapter.getItem(position).getValue());
                       }
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
        // Get field from view
        //mEditText = (EditText) view.findViewById(R.id.currency);
        // Fetch arguments from bundle and set title
        String title = "Choose a currency";
        //getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }


}