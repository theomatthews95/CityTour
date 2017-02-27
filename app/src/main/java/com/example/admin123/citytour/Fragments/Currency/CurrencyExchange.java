package com.example.admin123.citytour.Fragments.Currency;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.admin123.citytour.R.id.progressBar;

/**
 * Created by theom on 20/02/2017.
 */

public class CurrencyExchange extends AsyncTask<Void, Void, String> {

    private String convertFrom;
    private String conversionCurrency;

    protected String doInBackground(Void... urls) {
        // Do some validation here
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        //Parse the JSON response into a string
        try {
            //Fetch exchange rate data from fixer.io
            URL url = new URL("http://api.fixer.io/latest?base="+convertFrom);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                //read data into a string
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                String response = stringBuilder.toString();
                //return JSON data as string for calculateExchange() to calculate a conversion value given user params
                return response;
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    //method to parse JSON calculate the numeric exchange value
    protected String calculateExchange(String userBaseCurrency, String userConversionCurrency, Integer conversionAmount){
        Float conversionCurrencyRate = new Float(0);
        String exchangedValue = "Failed to convert";
        try {
            convertFrom = userBaseCurrency;
            conversionCurrency = userConversionCurrency;
            String response = doInBackground();
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            conversionCurrencyRate = BigDecimal.valueOf(object.getJSONObject("rates").getDouble(conversionCurrency)).floatValue();
            exchangedValue = String.format("%.2f", conversionCurrencyRate*conversionAmount);
        } catch (JSONException e) {
            // JSON Parsing error
            e.printStackTrace();
            System.out.println("Failed to retrieve exchange rate data");
        }

        return exchangedValue;
    }
}
