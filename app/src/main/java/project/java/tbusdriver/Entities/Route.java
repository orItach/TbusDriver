package project.java.tbusdriver.Entities;

import java.util.ArrayList;

/**
 * Created by אור איטח on 04/06/2017.
 */

public class Route
{
    private ArrayList<MyLocation> Locations;

    public Route( ArrayList<MyLocation> locations) {
        Locations = locations;
    }

    public ArrayList<MyLocation> getLocations() {
        return Locations;
    }

    public void setLocations(ArrayList<MyLocation> locations) {
        Locations = locations;
    }

}
