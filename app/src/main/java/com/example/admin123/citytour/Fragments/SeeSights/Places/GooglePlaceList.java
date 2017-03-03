package com.example.admin123.citytour.Fragments.SeeSights.Places;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 03/03/2017.
 */

public class GooglePlaceList {

    private String status;
    private String next_page_token;
    private ArrayList<GooglePlace> results;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<GooglePlace> getResults() {
        return results;
    }

    public void setResults(ArrayList<GooglePlace> results) {
        this.results = results;
    }

    public ArrayList<String> getPlaceNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (GooglePlace place : results) {
            result.add(place.toString());
        }
        return result;
    }
}
