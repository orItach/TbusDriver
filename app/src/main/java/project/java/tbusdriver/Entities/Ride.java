package project.java.tbusdriver.Entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by אור איטח on 27/06/2017.
 */

public class Ride {
    ArrayList<MyLocation> myLocations;
    Date startTime;
    Date estimatedFinishTime;
    int numberOfPassenger;

    public Ride(ArrayList<MyLocation> myLocations, Date startTime, Date estimatedFinishTime, int numberOfPassenger) {
        this.myLocations = myLocations;
        this.startTime = startTime;
        this.estimatedFinishTime = estimatedFinishTime;
        this.numberOfPassenger = numberOfPassenger;
    }


    public ArrayList<MyLocation> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(ArrayList<MyLocation> myLocations) {
        this.myLocations = myLocations;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEstimatedFinishTime() {
        return estimatedFinishTime;
    }

    public void setEstimatedFinishTime(Date estimatedFinishTime) {
        this.estimatedFinishTime = estimatedFinishTime;
    }

    public int getNumberOfPassenger() {
        return numberOfPassenger;
    }

    public void setNumberOfPassenger(int numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }
}
