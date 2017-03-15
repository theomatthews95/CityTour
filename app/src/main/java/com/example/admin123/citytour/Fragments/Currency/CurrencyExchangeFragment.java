package com.example.admin123.citytour.Fragments.Currency;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.citytour.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.key;
import static android.R.attr.value;
import static com.example.admin123.citytour.R.drawable.flag_button_uk;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrencyExchangeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrencyExchangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrencyExchangeFragment extends Fragment implements ChooseCurrencyDialog.OnSetConvertFromListener, ChooseCurrencyDialog.OnSetConvertToListener {

    private OnFragmentInteractionListener mListener;
    CurrencyExchange currency_exchange = new CurrencyExchange();
    String convertFrom = "GBP";
    String convertTo = "USD";
    Button convertFromButton;
    Button convertToButton;
    TextView convertFromTextView;
    TextView convertToTextView;
    HashMap<String, TreeMap<String, Integer>> currenciesHashMap = new HashMap<String, TreeMap<String, Integer>>();

    public CurrencyExchangeFragment() {
        TreeMap<String, Integer> dollarTreeMap = new TreeMap<String, Integer>();
        dollarTreeMap.put("$ US Dollar", R.drawable.flag_button_us);

        TreeMap<String, Integer> poundTreeMap = new TreeMap<String, Integer>();
        poundTreeMap.put("£ Pound Sterling",R.drawable.flag_button_uk);

        TreeMap<String, Integer> yuanTreeMap = new TreeMap<String, Integer>();
        yuanTreeMap.put("¥ Chinese Yuan",R.drawable.flag_button_china);

        TreeMap<String, Integer> euroTreeMap = new TreeMap<String, Integer>();
        euroTreeMap.put("€ Euros",R.drawable.flag_button_euro);

        currenciesHashMap.put("USD", dollarTreeMap);
        currenciesHashMap.put("GBP", poundTreeMap);
        currenciesHashMap.put("CNY", yuanTreeMap);
        currenciesHashMap.put("EUR", euroTreeMap);
    }


    // TODO: Rename and change types and number of parameters
    public static CurrencyExchangeFragment newInstance(String param1, String param2) {
        CurrencyExchangeFragment fragment = new CurrencyExchangeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_currency_exchange, container, false);
        Button convertCurrencyButton = (Button)v.findViewById(R.id.calculateExchange);
        final EditText TopET = (EditText) v.findViewById(R.id.top_currency_value);
        TopET.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        TopET.setInputType(0x00002002);
        final TextView BottomET = (TextView) v.findViewById(R.id.bottom_currency_value);


        convertCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String conversionAmountString = TopET.getText().toString();

                //check if a conversion amount has been entered and the EditText != empty
                if (!conversionAmountString.matches("")){
                    Double conversionAmount = Double.parseDouble(conversionAmountString);
                    String response = currency_exchange.calculateExchange(convertFrom, convertTo, conversionAmount);

                    BottomET.setText(response);

                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }else{
                    Toast toast = Toast.makeText(getContext(), "Please enter in an amount to convert", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        convertFromButton = (Button) v.findViewById(R.id.convertFrom);
        convertFromTextView = (TextView) v.findViewById(R.id.convertFromTextView);
        convertFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialog(true);
            }
        });
        convertToButton = (Button) v.findViewById(R.id.convertTo);
        convertToTextView = (TextView) v.findViewById(R.id.convertToTextView);
        convertToButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                showDialog(false);
            }
        });

        Button swapCurrenciesButton = (Button) v.findViewById(R.id.swapCurrencies);
        swapCurrenciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCurrencies(convertFrom, convertTo);
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showDialog(boolean isConvertFrom){
        ChooseCurrencyDialog chooseCurrencyDialog = ChooseCurrencyDialog.newInstance("Choose Currency Dialog", isConvertFrom);
        chooseCurrencyDialog.setTargetFragment(this, 0);
        chooseCurrencyDialog.show(getActivity().getSupportFragmentManager(), "fragmentDialog");
    }

    public void setConvertFrom(String convertFrom){
        this.convertFrom = convertFrom;
        TreeMap<String, Integer> flagsTreeMap = currenciesHashMap.get(convertFrom);
        Integer flag = flagsTreeMap.firstEntry().getValue();
        String title = flagsTreeMap.firstEntry().getKey();
        convertFromButton.setBackground(getResources().getDrawable(flag));
        convertFromTextView.setText(title);
    }

    public void setConvertTo(String convertTo){
        this.convertTo = convertTo;
        TreeMap<String, Integer> flagsTreeMap = currenciesHashMap.get(convertTo);
        Integer flag = flagsTreeMap.firstEntry().getValue();
        String title = flagsTreeMap.firstEntry().getKey();
        convertToButton.setBackground(getResources().getDrawable(flag));
        convertToTextView.setText(title);
    }

    public void switchCurrencies(String currentConvertFrom, String currentConvertTo){
        setConvertTo(currentConvertFrom);
        setConvertFrom(currentConvertTo);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Limit digits user can put into EditText for currency conversion
    private class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}
