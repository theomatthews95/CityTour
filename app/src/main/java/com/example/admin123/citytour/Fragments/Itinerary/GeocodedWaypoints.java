package com.example.admin123.citytour.Fragments.Itinerary;

import java.io.Serializable;
import java.util.List;

/**
 * Created by theom on 19/03/2017.
 */

public class GeocodedWaypoints implements Serializable {
    String place_id;
    String geocoder_status;
    List<String> types;

    public String getId() {
        return place_id;
    }

    public String getGeocoder_status(){return geocoder_status;}

    public List<String> getTypes(){return types;}
}
