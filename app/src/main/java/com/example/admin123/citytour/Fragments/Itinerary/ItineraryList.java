package com.example.admin123.citytour.Fragments.Itinerary;

import com.example.admin123.citytour.Fragments.SeeSights.Places.GooglePlace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 19/03/2017.
 */

public class ItineraryList implements Serializable{

    private String status;

    private ArrayList<GeocodedWaypoints> geocoded_waypoints;
    private ArrayList<Routes> routes;

    public ArrayList<GeocodedWaypoints> getGeocodedWaypoints() {
        return geocoded_waypoints;
    }

    public ArrayList<Routes> getRoutes(){ return routes;}
}
