package project.java.tbusdriver.Entities;

/**
 * Created by אור איטח on 27/06/2017.
 */

public class Ride {

    private int rideId;
    private Route route;
    private String travelTime;

    public Ride(Route route, String  travelTime, int rideId) {
        this.route = route;
        this.travelTime = travelTime;
        this.rideId = rideId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String  getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }
}
