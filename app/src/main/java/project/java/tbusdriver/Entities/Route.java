package project.java.tbusdriver.Entities;

import android.os.Parcel;
import android.os.Parcelable;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by אור איטח on 04/06/2017.
 */
//////////////////////////////////////////////////////
//////////////////// Route //////////////////////////
////////////////////////////////////////////////////
public class Route implements Serializable, Parcelable {
    // list of location
    private ArrayList<MyLocation> Locations;

    public Route(ArrayList<MyLocation> locations) {
        Locations = locations;
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public ArrayList<MyLocation> getLocations() {
        return Locations;
    }

    public void setLocations(ArrayList<MyLocation> locations) {
        Locations = locations;
    }

    public Route(Parcel in) {
        //Gson gson = new Gson();
        //String jsonData = in.readString();
        //this.Locations = gson.fromJson(jsonData, new TypeToken<ArrayList<MyLocation>>(){}.getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Gson gson = new Gson();
        //String json = gson.toJson(Locations);
        //dest.writeString(json);
    }

}
