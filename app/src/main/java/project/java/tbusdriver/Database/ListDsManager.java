package project.java.tbusdriver.Database;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import project.java.tbusdriver.Entities.MyLocation;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.Entities.Route;

import static project.java.tbusdriver.Const.AvailableRidesListName;
import static project.java.tbusdriver.Const.HistoricRidesListName;
import static project.java.tbusdriver.Const.MyRideListName;
import static project.java.tbusdriver.Const.RegionsListName;

/**
 * Created by אור איטח on 27/06/2017.
 */
/////////////////////////////////////////////////////
///////////// the app DB ////////////////////////////
////////////////////////////////////////////////////
public class ListDsManager {

    private static ArrayList<Ride> AvailableRides;
    private static ArrayList<Ride> MyRide;
    private static ArrayList<Ride> HistoricRides;
    private static ArrayList<Region> Regions;
    private Context context;
    private DateFormat formatter ;

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

    public static void setRegions(ArrayList<Region> Regions) {
        Regions = Regions;
    }

    public void updateHistoricRides(String input)
    {
        try
        {
            HistoricRides.clear();
            StringHistoricalRidesToJson(input);
        }
        catch (Exception e)
        {

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
                    Date now = new Date();
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                    try {
                        travel_time = format.format(now);
                    }
                    catch (Exception e)
                    {

                    }
                    id=c.getInt("id");
                    if (!c.isNull("json_route")) {
                        route=JsonRouteToRoute(c.getJSONObject("json_route"));
                    }
                    if(travel_time.equalsIgnoreCase("null"))
                        travel_time="--:--";
                    Ride ride=new Ride(route,travel_time,id);
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
                        travel_time="--:--";
                    Ride ride=new Ride(route,travel_time,id);
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
    private void StringHistoricalRidesToJson(String input)
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
                        travel_time="--:--";
                    Ride ride=new Ride(route,travel_time,id);
                    HistoricRides.add(ride);
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
        ArrayList<MyLocation> temp=new ArrayList<>();
        Route result=new Route(temp);
        //JSONObject point;
        if(jsonRoute!=null)
        {
            try {
                for (int i = 0; i < jsonRoute.length(); i++) {
                    JSONObject point = jsonRoute.getJSONObject(String.valueOf(i));
                    String id = String.valueOf(i);
                    Location geom=new Location("");
                    geom.setLatitude(point.getJSONObject("geom").getDouble("lat"));
                    geom.setLongitude(point.getJSONObject("geom").getDouble("lng"));
                    //Double distance=
                    // tmp hash map for single contact
                    MyLocation myLocation=new MyLocation(0,geom,5);
                    myLocation.setDistance(point.getDouble("distance"));
                    myLocation.setLocationId(i);
                    myLocation.setMyLocation(geom);
                    String test = point.getString("name");
                    if (jsonRoute.toString().contains("username")){
                        myLocation.setUsername(point.getString("username"));
                    }
                    if (jsonRoute.toString().contains("phone")){
                        myLocation.setPhone(point.getString("phone"));
                    }
                    if (test != null && test.contains("-d")) {
                        myLocation.setPickUp(true);
                        myLocation.setPassenger(test.replace("-d",""));
                    }
                    else {
                        myLocation.setPickUp(false);
                        myLocation.setPassenger(test);
                    }
                    if (jsonRoute.toString().contains("junction_name"))
                    {
                        myLocation.setDestinationAddress(point.getString("junction_name"));
                    }
                    else
                    {
                        myLocation.setDestinationAddress("לא ידוע");
                    }
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

    public static int convertRideIdToIndex(String list,int rideId)
    {
        switch (list)
        {
            case AvailableRidesListName:
                for (int i=0; i<AvailableRides.size();i++)
                {
                    if(AvailableRides.get(i).getRideId()==rideId)
                        return i;
                }
                break;
            case MyRideListName:
                for (int i=0; i<MyRide.size();i++)
                {
                    if(MyRide.get(i).getRideId()==rideId)
                        return i;
                }
                break;
            case HistoricRidesListName:
                for (int i=0; i<HistoricRides.size();i++)
                {
                    if(HistoricRides.get(i).getRideId()==rideId)
                        return i;
                }
                break;
            case RegionsListName:
                for (int i = 0; i <Regions.size() ; i++)
                {
                    if (Regions.get(i).getRegionID()==rideId)
                        return  i;
                }
            default:
                return -1;
        }
        return 0;
    }

}
