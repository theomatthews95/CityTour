package com.example.admin123.citytour.Fragments.SeeSights.Places;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by theom on 03/03/2017.
 */

public class GooglePlacesUtility {  public static String readGooglePlaces(String uri, String referer) {
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
        URL url = new URL(uri);
        Log.i("AJB", url.toString());
        conn = (HttpURLConnection) url.openConnection();
        if (referer != null) {
            conn.setRequestProperty("Referer", referer);
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());

        // Load the results into a StringBuilder
        int read;
        char[] buff = new char[1024];
        while ((read = in.read(buff)) != -1) {
            jsonResults.append(buff, 0, read);
        }
        Log.i("AJB", "RESULTS: " + jsonResults);
    } catch (MalformedURLException e) {
        Log.i("Google Places Utility", "Error processing Places API URL");
        return null;
    } catch (IOException e) {
        Log.i("Google Places Utility", "Error connecting to Places API");
        return null;
    } finally {
        if (conn != null) {
            conn.disconnect();
        }
    }
    return jsonResults.toString();
        /*
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Referer", "http://aston.ac.uk/uk.ac.aston.cs3040.rssreaderwithmaps.model");
                Log.i(RssItemActivity.TAG, "GET Request: " + httpGet.getURI());
        try {
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.i(RssItemActivity.TAG, "Failed to download file " + uri);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
        */
}
}