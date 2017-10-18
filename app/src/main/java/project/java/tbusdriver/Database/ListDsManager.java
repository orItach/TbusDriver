package project.java.tbusdriver.Database;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import project.java.tbusdriver.Entities.MyLocation;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.Entities.Route;

/**
 * Created by אור איטח on 27/06/2017.
 */

public class ListDsManager {
    private static ArrayList<Ride> AvailableRides;
    private static ArrayList<Ride> MyRide;
    private static ArrayList<Ride> HistoricRides;
    private static ArrayList<Region> Regions;
    Context context;
    DateFormat formatter ;

    public ListDsManager(Context context)
    {
        this.context=context;
        AvailableRides=new ArrayList<Ride>();
        MyRide=new ArrayList<Ride>();
        HistoricRides=new ArrayList<Ride>();
        Regions=new ArrayList<Region>();
        formatter = new SimpleDateFormat("HH:mm");
        //new UpdateDataTask().execute(0);//update agency
        //new UpdateDataTask().execute(1);//update trips
        //new UpdateDataTask().execute(2);
    }

    public static ArrayList<Ride> getAvailableRides() {
        return AvailableRides;
    }

    public static void setAvailableRides(ArrayList<Ride> availableRides) {
        AvailableRides = availableRides;
    }

    public static ArrayList<Ride> getMyRide() {
        return MyRide;
    }

    public static void setMyRide(ArrayList<Ride> myRide) {
        MyRide = myRide;
    }


    public static ArrayList<Ride> getHistoricRide() {
        return HistoricRides;
    }

    public static void setHistoricRide(ArrayList<Ride> historicRide) {
        HistoricRides = historicRide;
    }
    public static ArrayList<Region> getRegions() {
        return Regions;
    }

    public static void setRegions(ArrayList<Ride> Regions) {
        Regions = Regions;
    }

    public void updateHistoricRides(Cursor matrixAgency)
    {
        try{
            if (matrixAgency==null)
                throw new Exception("The Agencies are empty");
            HistoricRides.clear();
            for (matrixAgency.moveToFirst(); !matrixAgency.isAfterLast(); matrixAgency.moveToNext()) {
                //HistoricRides.add(new Ride(matrixAgency.getLong(matrixAgency.getColumnIndex("ID")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("name")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("country")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("city")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("street")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("phoneNum")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("email")),
                //        matrixAgency.getString(matrixAgency.getColumnIndex("link")),
                //        "https://d30y9cdsu7xlg0.cloudfront.net/png/17241-200.png"));
                }
        }
        catch (Exception e)
            {
            //Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void updateAvailableRides(String input) {
        try
        {
            AvailableRides.clear();
            StringAvailableRidesToJson(input);
        }
        catch (Exception e)
        {

        }
    }
    public void updateMyRides(String input)
    {
        try
        {
            MyRide.clear();
            StringMyRidesToJson(input);
        }
        catch (Exception e)
        {

        }
    }

    private void StringMyRidesToJson(String input)
    {
        if (input != null) {
            try {
                JSONObject result = new JSONObject(input);
                // Getting JSON Array node
                JSONArray rides = result.getJSONArray("rides");
                int id;
                String travel_time;
                Route route=null;
                // looping through All Contacts
                for (int i = 0; i < rides.length(); i++) {
                    JSONObject c = rides.getJSONObject(i);
                    travel_time = c.getString("travel_time");
                    id=c.getInt("id");
                    if (!c.isNull("json_route")) {
                        route=JsonRouteToRoute(c.getJSONObject("json_route"));
                    }
                    if(travel_time.equalsIgnoreCase("null"))
                        travel_time="15:00:00";
                    Ride ride=new Ride(route,Time.valueOf(travel_time),id);
                    MyRide.add(ride);
                    //ride.setTravelTime(c.getString("travel_time"));
                }
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
        else
        {

        }
    }

    private void StringAvailableRidesToJson(String input)
    {
        if (input != null) {
            try {
                JSONObject result = new JSONObject(input);
                // Getting JSON Array node
                JSONArray rides = result.getJSONArray("rides");
                int id;
                String travel_time;
                Route route=null;
                // looping through All Contacts
                for (int i = 0; i < rides.length(); i++) {
                    JSONObject c = rides.getJSONObject(i);
                    travel_time = c.getString("travel_time");
                    id=c.getInt("id");
                    if (!c.isNull("json_route")) {
                        route=JsonRouteToRoute(c.getJSONObject("json_route"));
                    }
                    if(travel_time.equalsIgnoreCase("null"))
                        travel_time="15:00:00";
                    Ride ride=new Ride(route,Time.valueOf(travel_time),id);
                    AvailableRides.add(ride);
                    //ride.setTravelTime(c.getString("travel_time"));
                }
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
        else
        {

        }
    }

    @Nullable
    private Route JsonRouteToRoute(JSONObject jsonRoute)
    {
        Location geom=new Location("");
        MyLocation myLocation=new MyLocation(0,geom,5);
        ArrayList<MyLocation> temp=new ArrayList<>();
        Route result=new Route(temp);
        if(jsonRoute!=null)
        {
            try {
                for (int i = 0; i < jsonRoute.length(); i++) {
                    JSONObject point = jsonRoute.getJSONObject(String.valueOf(i));
                    String id = String.valueOf(i);
                    geom.setLatitude(point.getJSONObject("geom").getDouble("lat"));
                    geom.setLongitude(point.getJSONObject("geom").getDouble("lng"));
                    //Double distance=
                    // tmp hash map for single contact
                    myLocation.setDistance(point.getDouble("distance"));
                    myLocation.setLocationId(i);
                    myLocation.setMyLocation(geom);
                    // adding contact to contact list
                    result.getLocations().add(myLocation);
                }
            }
            catch (final JSONException e) {

            }
        }
        else
        {
            return null;
        }
        return result;
    }

    public class UpdateDataTask extends AsyncTask<Integer, String, String> {
        protected String doInBackground(Integer... type) {
            if(type[0]==0)
            {
                //Cursor matrixAgency=context.getContentResolver().query(uriAgency, new String[]{}, "", new String[]{}, "");
                //if(HistoricRides==null)
                //    publishProgress("The Agencies are empty");
                //updateAvailableRides(matrixAgency);
            }
            else if(type[0]==1)
            {
                //Cursor matrixTrip = context.getContentResolver().query(uriTrip, new String[]{}, "", new String[]{}, "");
                //if(matrixTrip==null)
                //    publishProgress("The Trips are empty");
                //updateTrip(matrixTrip);
            }
            else if(type[0]==2)
            {
                ///// TODO: 27/06/2017  implement

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            android.app.AlertDialog.Builder myAlert=new android.app.AlertDialog.Builder(context);
            myAlert.setMessage(values[0]).create();
            myAlert.setTitle("Error with load Data");
            //myAlert.setIcon(R.drawable.error);
            myAlert.show();
            //Toast.makeText(context, values[0], Toast.LENGTH_LONG).show();
        }
    }

    public int convertRideIdToIndex(String list,int rideId)
    {
        switch (list)
        {
            case "AvailableRides":
                for (int i=0; i<AvailableRides.size();i++)
                {
                    if(AvailableRides.get(i).getRideId()==rideId)
                        return i;
                }
                break;
            case "MyRide":
                for (int i=0; i<MyRide.size();i++)
                {
                    if(MyRide.get(i).getRideId()==rideId)
                        return i;
                }
                break;
            case "HistoricRides":
                for (int i=0; i<HistoricRides.size();i++)
                {
                    if(HistoricRides.get(i).getRideId()==rideId)
                        return i;
                }
                break;
            default:
                return -1;
        }
        return 0;
    }





}
