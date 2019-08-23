package project.java.tbusdriver.Entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

//import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by אור איטח on 26/06/2017.
 */
//////////////////////////////////////////////////////
//////////////////// MyLocation //////////////////////
////////////////////////////////////////////////////
public class MyLocation implements Serializable, Parcelable {

    private int locationId;
    private Location myLocation;
    private double distance;
    // flag mean the station is down or up
    private boolean isPickUp;

    //private Passenger passenger;
    // TODO check why we have passenger and username
    private String passenger;
    private String username;
    private String phone;
    private String destinationAddress;

    public static final Creator<MyLocation> CREATOR = new Creator<MyLocation>() {
        @Override
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        @Override
        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };

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

    public boolean isPickUp() {
        return isPickUp;
    }

    public void setPickUp(boolean pickUp) {
        isPickUp = pickUp;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public MyLocation(int locationId, Location myLocation, double distance) {
        this.locationId = locationId;
        this.myLocation = myLocation;
        this.distance = distance;
    }

    public MyLocation(int locationId, Location myLocation, double distance, boolean isPickUp, String passenger, String username, String phone) {
        this.locationId = locationId;
        this.myLocation = myLocation;
        this.distance = distance;
        this.isPickUp = isPickUp;
        this.passenger = passenger;
        this.username = username;
        this.phone = phone;
    }

    public MyLocation(Parcel in) {
        //Gson gson = new Gson();
        //String jsonData = in.readString();
        //this.locationId = gson.fromJson(jsonData,int.class);
        //jsonData = in.readString();
        //this.myLocation = gson.fromJson(jsonData,Location.class);
        //jsonData = in.readString();
        //this.distance = gson.fromJson(jsonData,double.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Gson gson = new Gson();
        //String json = gson.toJson(locationId);
        //dest.writeString(json);
        //json = gson.toJson(myLocation);
        //dest.writeString(json);
        //json = gson.toJson(distance);
        //dest.writeString(json);
    }


}
