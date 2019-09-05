package project.java.tbusdriver.Entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
//import com.google.auto.value.AutoValue;
//import com.google.gson.Gson;

/**
 * Created by אור איטח on 27/06/2017.
 */
//////////////////////////////////////////////////////
//////////////////// Ride //////////////////////////
////////////////////////////////////////////////////
public class Ride implements Serializable, Parcelable, Comparable {

    private static final long serialVersionUID = 0L;

    // the id of ride
    private int rideId;

    private int groupId;
    // the route
    private Route route;
    // the travelTime- how much the all ride took
    private String travelTime;

    private Date actualTime;

    private Date preferredTime;

    private int maxPrice;

    private int taxiPrice;

    public Ride(Route route, String travelTime, int rideId) {
        this.route = route;
        this.travelTime = travelTime;
        this.rideId = rideId;
    }

    public Ride(Route route, String travelTime, int rideId, Date actualTime, Date preferredTime, int maxPrice) {
        this.route = route;
        this.travelTime = travelTime;
        this.rideId = rideId;
        this.actualTime = actualTime;
        this.preferredTime = preferredTime;
        this.maxPrice = maxPrice;
    }

    public Ride(Route route, String travelTime, int rideId,int groupId, Date actualTime, Date preferredTime, int maxPrice) {
        this.route = route;
        this.travelTime = travelTime;
        this.rideId = rideId;
        this.groupId = groupId;
        this.actualTime = actualTime;
        this.preferredTime = preferredTime;
        this.maxPrice = maxPrice;
    }
    public Ride(Route route, String travelTime, int rideId,int groupId, Date actualTime, Date preferredTime, int maxPrice, int taxiPrice) {
        this.route = route;
        this.travelTime = travelTime;
        this.rideId = rideId;
        this.groupId = groupId;
        this.actualTime = actualTime;
        this.preferredTime = preferredTime;
        this.maxPrice = maxPrice;
        this.taxiPrice = taxiPrice;
    }

    @Override
    public  boolean equals(Object maybeEquals) {
        if (maybeEquals != null && maybeEquals instanceof Ride){
            return this.groupId == ((Ride) maybeEquals).groupId;
        }
        return false;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getTravelTime() {
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

    public Date getActualTime() {
        return actualTime;
    }

    public void setActualTime(Date actualTime) {
        this.actualTime = actualTime;
    }

    public Date getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(Date preferredTime) {
        this.preferredTime = preferredTime;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getTaxiPrice() {
        return taxiPrice;
    }

    public void setTaxiPrice(int taxiPrice) {
        this.taxiPrice = taxiPrice;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel in) {
            return new Ride(in);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };

    public Ride(Parcel in) {
        //Gson gson = new Gson();
        //String jsonData = in.readString();
        //this.rideId = gson.fromJson(jsonData,int.class);
        //jsonData = in.readString();
        //this.route = gson.fromJson(jsonData,Route.class);
        //jsonData = in.readString();
        //this.travelTime = gson.fromJson(jsonData,String.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Gson gson = new Gson();
        //String json = gson.toJson(rideId);
        //dest.writeString(json);
        //json = gson.toJson(route);
        //dest.writeString(json);
        //json = gson.toJson(travelTime);
        //dest.writeString(json);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.rideId - ((Ride)(o)).rideId;
    }
}
