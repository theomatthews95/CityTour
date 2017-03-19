package com.example.admin123.citytour.Fragments.Itinerary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theom on 19/03/2017.
 */

public class Routes implements Serializable {
    String copyrights;
    ArrayList<Leg> legs;

    public String getCopyright(){return copyrights;}

    public static class Leg implements Serializable{
        private String end_address;
        private Distance distance;
        private Duration duration;
        public Distance getDistance(){return distance;}
        public Duration getDuration(){return duration;}

        public static class Distance implements Serializable{
            private String text;
            private String value;

            public String getText(){return text;}
            public String getValue(){return value;}

        }

        public static class Duration implements Serializable{
            private String text;
            private String value;

            public String getText(){return text;}
            public String getValue(){return value;}

        }

        public String getEnd_address(){return end_address;}

    }

    private OverviewPolyline overview_polyline;

    public OverviewPolyline getOverview_polyline(){return overview_polyline;}

    public static class OverviewPolyline implements Serializable{
        String points;
        public String getPoints(){return points;}
    }

    public ArrayList<Leg> getLegs(){return legs;}

    private ArrayList<Integer> waypoint_order;

    public ArrayList<Integer> getWaypoint_order(){return waypoint_order;}
}
