package com.example.admin123.citytour.Fragments.SeeSights.Places;

/**
 * Created by theom on 03/03/2017.
 */

public class PlaceDetail {
    //@Key
    private GooglePlace result;

    public GooglePlace getResult() {
        return result;
    }

    public void setResult(GooglePlace result) {
        this.result = result;
    }

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}
