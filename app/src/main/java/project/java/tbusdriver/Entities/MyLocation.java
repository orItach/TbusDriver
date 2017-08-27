package project.java.tbusdriver.Entities;

import android.location.Location;

/**
 * Created by אור איטח on 26/06/2017.
 */

public class MyLocation {

    private int locationId;
    private Location myLocation;
    private double distance;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public MyLocation(int locationId, Location myLocation, double distance) {
        this.locationId = locationId;
        this.myLocation = myLocation;
        this.distance = distance;
    }



}
