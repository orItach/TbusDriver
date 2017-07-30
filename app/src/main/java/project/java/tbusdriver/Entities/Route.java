package project.java.tbusdriver.Entities;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by אור איטח on 04/06/2017.
 */

public class Route
{
    private int numberOfLocation;
    private ArrayList<MyLocation> Locations;

    public Route(int numberOfLocation, ArrayList<MyLocation> locations) {
        this.numberOfLocation = numberOfLocation;
        Locations = locations;
    }

    public int getNumberOfLocation() {
        return numberOfLocation;
    }

    public void setNumberOfLocation(int numberOfLocation) {
        this.numberOfLocation = numberOfLocation;
    }

    public ArrayList<MyLocation> getLocations() {
        return Locations;
    }

    public void setLocations(ArrayList<MyLocation> locations) {
        Locations = locations;
    }
}
