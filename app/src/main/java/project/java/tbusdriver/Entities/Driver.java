package project.java.tbusdriver.Entities;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by אור איטח on 04/06/2017.
 */

public class Driver {
    private int driverNumber;
    private String name;
    private int numberOfPassenger;
    private Route route;
    private ArrayList[] passengers;
    private Location currentLocation;
    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public int getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(int driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfPassenger() {
        return numberOfPassenger;
    }

    public ArrayList[] getPassengers() {
        return passengers;
    }

    public Route getRoute() {
        return route;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfPassenger(int numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setPassengers(ArrayList[] passengers) {
        this.passengers = passengers;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    public Driver(int driverNumber_in, String name_in, int numberOfPassenger_in, Route route_in, ArrayList[] passengers, Location currentLocation_in, boolean available_in) {
        this.driverNumber = driverNumber_in;
        this.name = name_in;
        this.numberOfPassenger = numberOfPassenger_in;
        this.route = route_in;
        this.passengers = new ArrayList[numberOfPassenger_in];
        this.currentLocation = currentLocation_in;
        this.available = available_in;
        return;
    }


}
