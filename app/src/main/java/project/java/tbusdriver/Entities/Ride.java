package project.java.tbusdriver.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
//import com.google.auto.value.AutoValue;
//import com.google.gson.Gson;

/**
 * Created by אור איטח on 27/06/2017.
 */
//Parcelable
public class Ride implements Serializable,Parcelable {

    private static final long serialVersionUID = 0L;

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

    public Ride(Parcel in){
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
}
