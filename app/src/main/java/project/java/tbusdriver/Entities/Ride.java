package project.java.tbusdriver.Entities;

import java.sql.Time;

/**
 * Created by אור איטח on 27/06/2017.
 */

public class Ride {

    Route route;
    Time travelTime;
    int rideId;

    public Ride(Route route, Time travelTime, int rideId) {
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

    public Time getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Time travelTime) {
        this.travelTime = travelTime;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }
}
