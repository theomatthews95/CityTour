package com.example.admin123.citytour.Fragments.Itinerary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 19/03/2017.
 */

public class ItineraryList {

    private ArrayList<Routes> routesList;
    private String text;
    private ArrayList<ItineraryList> results;

    public String getText() {
        return text;
    }

    public static class Routes{
        ArrayList<Legs> legList;
        OverviewPolyline overview_polyline;
        ArrayList<Integer> waypoint_order;

        public ArrayList<Legs> getLegList(){return legList;}
        public OverviewPolyline getOverview_polyline(){return overview_polyline;}
        public ArrayList<Integer> getWaypoint_order(){return waypoint_order;}

        public static class Legs{
            String duration;
            String distance;
            public String getDuration(){return duration;}
            public String getDistance(){return distance;}
        }

        public static class OverviewPolyline{
            String points;
            public String getPoints(){return points;}
        }

    }

    public ArrayList<Routes> getRoutesList(){
        return routesList;
    }

    public ArrayList<ItineraryList> getResults() {
        return results;
    }

    public void setResults(ArrayList<ItineraryList> results) {
        this.results = results;
    }
}
