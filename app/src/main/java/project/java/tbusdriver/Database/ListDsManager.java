package project.java.tbusdriver.Database;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private DateFormat formatter;

    public ListDsManager(Context context) {
        this.context = context;
        AvailableRides = new ArrayList<Ride>();
        MyRide = new ArrayList<Ride>();
        HistoricRides = new ArrayList<Ride>();
        Regions = new ArrayList<Region>();
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

    public void updateHistoricRides(String input) {
        try {
            HistoricRides.clear();
            StringHistoricalRidesToJson(input);
        } catch (Exception e) {

        }
    }

    public void updateAvailableRides(String input) {
        try {
            AvailableRides.clear();
            StringAvailableRidesToJson(input);
        } catch (Exception e) {

        }
    }


    public void updateMyRides(String input) {
        try {
            MyRide.clear();
            StringMyRidesToJson(input);
        } catch (Exception e) {

        }
    }

    private void StringMyRidesToJson(String input) {
        if (input != null) {
            try {
                JSONObject result = new JSONObject(input);
                // Getting JSON Array node
                JSONArray rides = result.getJSONArray("rides");
                String travel_time;
                Route route = null;
                int maxPrice,id, groupId;
                Date preferredTime = new Date();
                Date actualTime = new Date();
                // looping through All Contacts
                for (int i = 0; i < rides.length(); i++) {
                    JSONObject c = rides.getJSONObject(i);
                    travel_time = c.getString("travel_time");
                    Date now = new Date();
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                    try {
                        travel_time = format.format(now);
                    } catch (Exception e) {

                    }
                    id = c.getInt("id");
                    groupId = c.getInt("group_id");
                    if (!c.isNull("json_route")) {
                        route = JsonRouteToRoute(c.getJSONObject("json_route"));
                    }
                    if (travel_time.equalsIgnoreCase("null"))
                        travel_time = "--:--";

                    maxPrice = c.getInt("max_price");
                    DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000", Locale.ENGLISH);
                    if (!(c.getString("preffered_time")).equalsIgnoreCase("null"))
                        preferredTime = timeFormat.parse (c.getString("preffered_time").replace("T"," "));
                    if (!(c.getString("actual_time")).equalsIgnoreCase("null"))
                        actualTime = timeFormat.parse (c.getString("actual_time").replace("T"," "));
                    if (preferredTime.after(new Date()))
                    {
                        Ride ride = new Ride(route, travel_time, id,groupId, actualTime, preferredTime, maxPrice);
                        if (!MyRide.contains(ride))
                            MyRide.add(ride);
                        else {

                        }
                    }
                    //ride.setTravelTime(c.getString("travel_time"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    private void StringAvailableRidesToJson(String input) {
        if (input != null) {
            try {
                //input = //"{" +
                        //"  \"status\": \"OK\"," +
                        //"  \"rides\": [" +
                        //"    {" +
                        //"      \"date_formated\": \"Invalid date\"," +
                        //"      \"preffered_time_formated\": \"Invalid date\"," +
                        //"      \"id\": 446," +
                        //"      \"status\": 0," +
                        //"      \"createdAt\": \"2018-07-26T12:54:38.696Z\"," +
                        //"      \"updatedAt\": \"2018-08-27T17:56:04.137Z\"," +
                        //"      \"taxi_id\": null," +
                        //"      \"deleted\": false," +
                        //"      \"notes\": null," +
                        //"      \"travel_time\": null," +
                        //"      \"json_route\": {" +
                        //"        \"0\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"448\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"1\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"446\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"2\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.72065," +
                        //"            \"lng\": 35.09486" +
                        //"          }," +
                        //"          \"name\": \"448-d\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 42," +
                        //"          \"distance\": 41.3," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"שיטה|הערבה\"" +
                        //"        }," +
                        //"        \"3\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.7197785," +
                        //"            \"lng\": 35.090003300000035" +
                        //"          }," +
                        //"          \"name\": \"446-d\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 42," +
                        //"          \"distance\": 41.9," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"הר כתרון | דרור\"" +
                        //"        }" +
                        //"      }," +
                        //"      \"user_id\": 516," +
                        //"      \"origin\": {" +
                        //"        \"type\": \"Point\"," +
                        //"        \"coordinates\": [" +
                        //"          31.7769," +
                        //"          35.2203" +
                        //"        ]" +
                        //"      }," +
                        //"      \"destination\": null," +
                        //"      \"min_time\": null," +
                        //"      \"max_time\": null," +
                        //"      \"number_passenger\": 0," +
                        //"      \"origin_junction_id\": 903," +
                        //"      \"created\": \"2018-08-13T12:18:59.273Z\"," +
                        //"      \"updated\": \"2018-08-13T12:18:59.273Z\"," +
                        //"      \"group_id\": 308," +
                        //"      \"destination_junction_id\": 895," +
                        //"      \"max_price\": 75," +
                        //"      \"preffered_time\": \"2018-08-13T12:24:00.000Z\"," +
                        //"      \"party_size\": null," +
                        //"      \"price_payed\": null," +
                        //"      \"passanger_updated\": false," +
                        //"      \"actual_time\": null" +
                        //"    }," +
                        //"    {" +
                        //"      \"date_formated\": \"Invalid date\"," +
                        //"      \"preffered_time_formated\": \"Invalid date\"," +
                        //"      \"id\": 448," +
                        //"      \"status\": 0," +
                        //"      \"createdAt\": \"2018-07-26T12:54:38.696Z\"," +
                        //"      \"updatedAt\": \"2018-08-27T17:56:04.137Z\"," +
                        //"      \"taxi_id\": null," +
                        //"      \"deleted\": false," +
                        //"      \"notes\": null," +
                        //"      \"travel_time\": null," +
                        //"      \"json_route\": {" +
                        //"        \"0\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"448\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"1\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"446\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"2\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.72065," +
                        //"            \"lng\": 35.09486" +
                        //"          }," +
                        //"          \"name\": \"448-d\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 42," +
                        //"          \"distance\": 41.3," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"שיטה|הערבה\"" +
                        //"        }," +
                        //"        \"3\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.7197785," +
                        //"            \"lng\": 35.090003300000035" +
                        //"          }," +
                        //"          \"name\": \"446-d\"," +
                        //"          \"phone\": \"0526864920\"," +
                        //"          \"arrival\": 42," +
                        //"          \"distance\": 41.9," +
                        //"          \"username\": \"Yoni1\"," +
                        //"          \"junction_name\": \"הר כתרון | דרור\"" +
                        //"        }" +
                        //"      }," +
                        //"      \"user_id\": 516," +
                        //"      \"origin\": {" +
                        //"        \"type\": \"Point\"," +
                        //"        \"coordinates\": [" +
                        //"          31.7769," +
                        //"          35.2203" +
                        //"        ]" +
                        //"      }," +
                        //"      \"destination\": null," +
                        //"      \"min_time\": null," +
                        //"      \"max_time\": null," +
                        //"      \"number_passenger\": 0," +
                        //"      \"origin_junction_id\": 903," +
                        //"      \"created\": \"2018-08-13T12:49:19.028Z\"," +
                        //"      \"updated\": \"2018-08-13T12:49:19.028Z\"," +
                        //"      \"group_id\": 308," +
                        //"      \"destination_junction_id\": 904," +
                        //"      \"max_price\": 75," +
                        //"      \"preffered_time\": \"2018-08-20T12:55:00.000Z\"," +
                        //"      \"party_size\": null," +
                        //"      \"price_payed\": null," +
                        //"      \"passanger_updated\": false," +
                        //"      \"actual_time\": null" +
                        //"    }," +
                        //"    {" +
                        //"      \"date_formated\": \"Invalid date\"," +
                        //"      \"preffered_time_formated\": \"Invalid date\"," +
                        //"      \"id\": 420," +
                        //"      \"status\": 0," +
                        //"      \"createdAt\": \"2018-07-26T13:00:44.651Z\"," +
                        //"      \"updatedAt\": \"2018-08-27T17:56:08.827Z\"," +
                        //"      \"taxi_id\": null," +
                        //"      \"deleted\": false," +
                        //"      \"notes\": null," +
                        //"      \"travel_time\": null," +
                        //"      \"json_route\": {" +
                        //"        \"0\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78197," +
                        //"            \"lng\": 35.31146" +
                        //"          }," +
                        //"          \"name\": \"418\"," +
                        //"          \"phone\": \"0528962581\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"\\\"danielpt\\\"\"," +
                        //"          \"junction_name\": \"הרכס|האלמוג\"" +
                        //"        }," +
                        //"        \"1\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78197," +
                        //"            \"lng\": 35.31146" +
                        //"          }," +
                        //"          \"name\": \"420\"," +
                        //"          \"phone\": \"0546548375\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"koursdinalklllll\"," +
                        //"          \"junction_name\": \"הרכס|האלמוג\"" +
                        //"        }," +
                        //"        \"2\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"421\"," +
                        //"          \"phone\": \"0528080043\"," +
                        //"          \"arrival\": 4," +
                        //"          \"distance\": 3.3," +
                        //"          \"username\": \"גלעד\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"3\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.718143," +
                        //"            \"lng\": 35.09829400000001" +
                        //"          }," +
                        //"          \"name\": \"418-d\"," +
                        //"          \"phone\": \"0528962581\"," +
                        //"          \"arrival\": 46," +
                        //"          \"distance\": 44.8," +
                        //"          \"username\": \"\\\"danielpt\\\"\"," +
                        //"          \"junction_name\": \"נרקיס|רימון\"" +
                        //"        }," +
                        //"        \"4\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.72065," +
                        //"            \"lng\": 35.09486" +
                        //"          }," +
                        //"          \"name\": \"420-d\"," +
                        //"          \"phone\": \"0546548375\"," +
                        //"          \"arrival\": 47," +
                        //"          \"distance\": 45.4," +
                        //"          \"username\": \"koursdinalklllll\"," +
                        //"          \"junction_name\": \"שיטה|הערבה\"" +
                        //"        }," +
                        //"        \"5\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.7197785," +
                        //"            \"lng\": 35.090003300000035" +
                        //"          }," +
                        //"          \"name\": \"421-d\"," +
                        //"          \"phone\": \"0528080043\"," +
                        //"          \"arrival\": 48," +
                        //"          \"distance\": 46," +
                        //"          \"username\": \"גלעד\"," +
                        //"          \"junction_name\": \"הר כתרון | דרור\"" +
                        //"        }" +
                        //"      }," +
                        //"      \"user_id\": 514," +
                        //"      \"origin\": {" +
                        //"        \"type\": \"Point\"," +
                        //"        \"coordinates\": [" +
                        //"          31.79068," +
                        //"          35.20187" +
                        //"        ]" +
                        //"      }," +
                        //"      \"destination\": null," +
                        //"      \"min_time\": null," +
                        //"      \"max_time\": null," +
                        //"      \"number_passenger\": 1," +
                        //"      \"origin_junction_id\": 894," +
                        //"      \"created\": \"2018-08-02T16:58:51.423Z\"," +
                        //"      \"updated\": \"2018-08-02T16:58:51.423Z\"," +
                        //"      \"group_id\": 309," +
                        //"      \"destination_junction_id\": 904," +
                        //"      \"max_price\": 75," +
                        //"      \"preffered_time\": \"2018-08-02T21:00:00.000Z\"," +
                        //"      \"party_size\": null," +
                        //"      \"price_payed\": null," +
                        //"      \"passanger_updated\": false," +
                        //"      \"actual_time\": null" +
                        //"    }," +
                        //"    {" +
                        //"      \"date_formated\": \"Invalid date\"," +
                        //"      \"preffered_time_formated\": \"Invalid date\"," +
                        //"      \"id\": 421," +
                        //"      \"status\": 0," +
                        //"      \"createdAt\": \"2018-07-26T13:00:44.651Z\"," +
                        //"      \"updatedAt\": \"2018-08-27T17:56:08.827Z\"," +
                        //"      \"taxi_id\": null," +
                        //"      \"deleted\": false," +
                        //"      \"notes\": null," +
                        //"      \"travel_time\": null," +
                        //"      \"json_route\": {" +
                        //"        \"0\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78197," +
                        //"            \"lng\": 35.31146" +
                        //"          }," +
                        //"          \"name\": \"418\"," +
                        //"          \"phone\": \"0528962581\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"\\\"danielpt\\\"\"," +
                        //"          \"junction_name\": \"הרכס|האלמוג\"" +
                        //"        }," +
                        //"        \"1\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78197," +
                        //"            \"lng\": 35.31146" +
                        //"          }," +
                        //"          \"name\": \"420\"," +
                        //"          \"phone\": \"0546548375\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"koursdinalklllll\"," +
                        //"          \"junction_name\": \"הרכס|האלמוג\"" +
                        //"        }," +
                        //"        \"2\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"421\"," +
                        //"          \"phone\": \"0528080043\"," +
                        //"          \"arrival\": 4," +
                        //"          \"distance\": 3.3," +
                        //"          \"username\": \"גלעד\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"3\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.718143," +
                        //"            \"lng\": 35.09829400000001" +
                        //"          }," +
                        //"          \"name\": \"418-d\"," +
                        //"          \"phone\": \"0528962581\"," +
                        //"          \"arrival\": 46," +
                        //"          \"distance\": 44.8," +
                        //"          \"username\": \"\\\"danielpt\\\"\"," +
                        //"          \"junction_name\": \"נרקיס|רימון\"" +
                        //"        }," +
                        //"        \"4\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.72065," +
                        //"            \"lng\": 35.09486" +
                        //"          }," +
                        //"          \"name\": \"420-d\"," +
                        //"          \"phone\": \"0546548375\"," +
                        //"          \"arrival\": 47," +
                        //"          \"distance\": 45.4," +
                        //"          \"username\": \"koursdinalklllll\"," +
                        //"          \"junction_name\": \"שיטה|הערבה\"" +
                        //"        }," +
                        //"        \"5\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.7197785," +
                        //"            \"lng\": 35.090003300000035" +
                        //"          }," +
                        //"          \"name\": \"421-d\"," +
                        //"          \"phone\": \"0528080043\"," +
                        //"          \"arrival\": 48," +
                        //"          \"distance\": 46," +
                        //"          \"username\": \"גלעד\"," +
                        //"          \"junction_name\": \"הר כתרון | דרור\"" +
                        //"        }" +
                        //"      }," +
                        //"      \"user_id\": 507," +
                        //"      \"origin\": {" +
                        //"        \"type\": \"Point\"," +
                        //"        \"coordinates\": [" +
                        //"          31.7909," +
                        //"          35.20162" +
                        //"        ]" +
                        //"      }," +
                        //"      \"destination\": null," +
                        //"      \"min_time\": null," +
                        //"      \"max_time\": null," +
                        //"      \"number_passenger\": 2," +
                        //"      \"origin_junction_id\": 903," +
                        //"      \"created\": \"2018-08-02T17:01:11.210Z\"," +
                        //"      \"updated\": \"2018-08-02T17:01:11.210Z\"," +
                        //"      \"group_id\": 309," +
                        //"      \"destination_junction_id\": 895," +
                        //"      \"max_price\": 56," +
                        //"      \"preffered_time\": \"2018-08-05T09:03:00.000Z\"," +
                        //"      \"party_size\": null," +
                        //"      \"price_payed\": null," +
                        //"      \"passanger_updated\": false," +
                        //"      \"actual_time\": null" +
                        //"    }," +
                        //"    {" +
                        //"      \"date_formated\": \"Invalid date\"," +
                        //"      \"preffered_time_formated\": \"Invalid date\"," +
                        //"      \"id\": 418," +
                        //"      \"status\": 0," +
                        //"      \"createdAt\": \"2018-07-26T13:00:44.651Z\"," +
                        //"      \"updatedAt\": \"2018-08-27T17:56:08.827Z\"," +
                        //"      \"taxi_id\": null," +
                        //"      \"deleted\": false," +
                        //"      \"notes\": null," +
                        //"      \"travel_time\": null," +
                        //"      \"json_route\": {" +
                        //"        \"0\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78197," +
                        //"            \"lng\": 35.31146" +
                        //"          }," +
                        //"          \"name\": \"418\"," +
                        //"          \"phone\": \"0528962581\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"\\\"danielpt\\\"\"," +
                        //"          \"junction_name\": \"הרכס|האלמוג\"" +
                        //"        }," +
                        //"        \"1\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78197," +
                        //"            \"lng\": 35.31146" +
                        //"          }," +
                        //"          \"name\": \"420\"," +
                        //"          \"phone\": \"0546548375\"," +
                        //"          \"arrival\": 0," +
                        //"          \"distance\": 0," +
                        //"          \"username\": \"koursdinalklllll\"," +
                        //"          \"junction_name\": \"הרכס|האלמוג\"" +
                        //"        }," +
                        //"        \"2\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.78375," +
                        //"            \"lng\": 35.29777" +
                        //"          }," +
                        //"          \"name\": \"421\"," +
                        //"          \"phone\": \"0528080043\"," +
                        //"          \"arrival\": 4," +
                        //"          \"distance\": 3.3," +
                        //"          \"username\": \"גלעד\"," +
                        //"          \"junction_name\": \"הכינור|הצלצל\"" +
                        //"        }," +
                        //"        \"3\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.718143," +
                        //"            \"lng\": 35.09829400000001" +
                        //"          }," +
                        //"          \"name\": \"418-d\"," +
                        //"          \"phone\": \"0528962581\"," +
                        //"          \"arrival\": 46," +
                        //"          \"distance\": 44.8," +
                        //"          \"username\": \"\\\"danielpt\\\"\"," +
                        //"          \"junction_name\": \"נרקיס|רימון\"" +
                        //"        }," +
                        //"        \"4\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.72065," +
                        //"            \"lng\": 35.09486" +
                        //"          }," +
                        //"          \"name\": \"420-d\"," +
                        //"          \"phone\": \"0546548375\"," +
                        //"          \"arrival\": 47," +
                        //"          \"distance\": 45.4," +
                        //"          \"username\": \"koursdinalklllll\"," +
                        //"          \"junction_name\": \"שיטה|הערבה\"" +
                        //"        }," +
                        //"        \"5\": {" +
                        //"          \"geom\": {" +
                        //"            \"lat\": 31.7197785," +
                        //"            \"lng\": 35.090003300000035" +
                        //"          }," +
                        //"          \"name\": \"421-d\"," +
                        //"          \"phone\": \"0528080043\"," +
                        //"          \"arrival\": 48," +
                        //"          \"distance\": 46," +
                        //"          \"username\": \"גלעד\"," +
                        //"          \"junction_name\": \"הר כתרון | דרור\"" +
                        //"        }" +
                        //"      }," +
                        //"      \"user_id\": 515," +
                        //"      \"origin\": {" +
                        //"        \"type\": \"Point\"," +
                        //"        \"coordinates\": [" +
                        //"          31.79069," +
                        //"          35.20186" +
                        //"        ]" +
                        //"      }," +
                        //"      \"destination\": null," +
                        //"      \"min_time\": null," +
                        //"      \"max_time\": null," +
                        //"      \"number_passenger\": 1," +
                        //"      \"origin_junction_id\": 894," +
                        //"      \"created\": \"2018-08-02T16:25:19.445Z\"," +
                        //"      \"updated\": \"2018-08-02T16:25:19.445Z\"," +
                        //"      \"group_id\": 309," +
                        //"      \"destination_junction_id\": 896," +
                        //"      \"max_price\": 75," +
                        //"      \"preffered_time\": \"2018-08-02T16:27:00.000Z\"," +
                        //"      \"party_size\": null," +
                        //"      \"price_payed\": null," +
                        //"      \"passanger_updated\": false," +
                        //"      \"actual_time\": null" +
                        //"    }"+
                        //" ]"+
                        //" } ";
                JSONObject result = new JSONObject(input);
                // Getting JSON Array node
                JSONArray rides = result.getJSONArray("rides");
                int id, maxPrice,groupId, taxiPrice;
                String travel_time;
                Date actualTime= new Date();
                Date preferredTime = new Date();
                Route route = null;
                // looping through All Contacts
                for (int i = 0; i < rides.length(); i++) {
                    JSONObject c = rides.getJSONObject(i);
                    travel_time = c.getString("travel_time");
                    id = c.getInt("id");
                    groupId = c.getInt("group_id");
                    if (!c.isNull("json_route")) {
                        route = JsonRouteToRoute(c.getJSONObject("json_route"));
                    }
                    if (travel_time.equalsIgnoreCase("null"))
                        travel_time = "--:--";
                    maxPrice = c.getInt("max_price");
                    taxiPrice =0;
                    try {
                        taxiPrice = c.getInt("taxi_p");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000", Locale.ENGLISH);
                    if (!(c.getString("preffered_time")).equalsIgnoreCase("null")) {
                        preferredTime = format.parse(c.getString("preffered_time").replace("T", " "));
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(preferredTime);
                        calendar.add(Calendar.HOUR, 2);
                        preferredTime = calendar.getTime();
                    }
                    if (!(c.getString("actual_time")).equalsIgnoreCase("null")) {
                        actualTime = format.parse(c.getString("actual_time").replace("T", " "));
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(actualTime);
                        calendar.add(Calendar.HOUR, 4);
                        actualTime = calendar.getTime();
                    }
                    Ride ride1 = new Ride(route, travel_time, id,groupId, actualTime, preferredTime, maxPrice, taxiPrice);
                    if (!AvailableRides.contains(ride1))
                        AvailableRides.add(ride1);
                    if (preferredTime.after(new Date()))
                    {
                        Ride ride = new Ride(route, travel_time, id,groupId, actualTime, preferredTime, maxPrice, taxiPrice);
                        if (!AvailableRides.contains(ride))
                            AvailableRides.add(ride);
                        else {
                            try {
                                //update if needed.
                                int rideIndex = convertGroupIdToIndexFirst(AvailableRidesListName,ride.getGroupId());
                                if(rideIndex != -1){
                                    if (AvailableRides.get(rideIndex).getPreferredTime().after(ride.getPreferredTime())){
                                        AvailableRides.get(rideIndex).setPreferredTime(ride.getPreferredTime());
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    //ride.setTravelTime(c.getString("travel_time"));
                }
                //Collections.sort(AvailableRides);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }
    
    private void AddOrUpdateAvailableRides(Ride maybeNewRide ){
        for (int i = 0; i < AvailableRides.size(); i++) {

        }
    }

    private void StringHistoricalRidesToJson(String input) {
        if (input != null) {
            try {
                JSONObject result = new JSONObject(input);
                // Getting JSON Array node
                JSONArray rides = result.getJSONArray("rides");
                int id;
                String travel_time;
                Route route = null;
                // looping through All Contacts
                for (int i = 0; i < rides.length(); i++) {
                    JSONObject c = rides.getJSONObject(i);
                    travel_time = c.getString("travel_time");
                    id = c.getInt("id");
                    if (!c.isNull("json_route")) {
                        route = JsonRouteToRoute(c.getJSONObject("json_route"));
                    }
                    if (travel_time.equalsIgnoreCase("null"))
                        travel_time = "--:--";
                    Ride ride = new Ride(route, travel_time, id);
                    HistoricRides.add(ride);
                    //ride.setTravelTime(c.getString("travel_time"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @Nullable
    private Route JsonRouteToRoute(JSONObject jsonRoute) {
        ArrayList<MyLocation> temp = new ArrayList<>();
        Route result = new Route(temp);
        //JSONObject point;
        if (jsonRoute != null) {
            try {
                for (int i = 0; i < jsonRoute.length(); i++) {
                    JSONObject point = jsonRoute.getJSONObject(String.valueOf(i));
                    String id = String.valueOf(i);
                    Location geom = new Location("");
                    geom.setLatitude(point.getJSONObject("geom").getDouble("lat"));
                    geom.setLongitude(point.getJSONObject("geom").getDouble("lng"));
                    //Double distance=
                    // tmp hash map for single contact
                    MyLocation myLocation = new MyLocation(0, geom, 5);
                    myLocation.setDistance(point.getDouble("distance"));
                    myLocation.setLocationId(i);
                    myLocation.setMyLocation(geom);
                    String test = point.getString("name");
                    if (jsonRoute.toString().contains("username")) {
                        myLocation.setUsername(point.getString("username"));
                    }
                    if (jsonRoute.toString().contains("phone")) {
                        myLocation.setPhone(point.getString("phone"));
                    }
                    if (test != null && test.contains("-d")) {
                        myLocation.setPickUp(false);
                        myLocation.setPassenger(test);
                    } else {
                        myLocation.setPickUp(true);
                        myLocation.setPassenger(test.replace("-d", ""));
                    }
                    if (jsonRoute.toString().contains("junction_name")) {
                        myLocation.setDestinationAddress(point.getString("junction_name"));
                    } else {
                        myLocation.setDestinationAddress("לא ידוע");
                    }
                    // adding contact to contact list
                    result.getLocations().add(myLocation);
                }
            } catch (final JSONException e) {

            }
        } else {
            return null;
        }
        return result;
    }

    public class UpdateDataTask extends AsyncTask<Integer, String, String> {
        protected String doInBackground(Integer... type) {
            if (type[0] == 0) {
                //Cursor matrixAgency=context.getContentResolver().query(uriAgency, new String[]{}, "", new String[]{}, "");
                //if(HistoricRides==null)
                //    publishProgress("The Agencies are empty");
                //updateAvailableRides(matrixAgency);
            } else if (type[0] == 1) {
                //Cursor matrixTrip = context.getContentResolver().query(uriTrip, new String[]{}, "", new String[]{}, "");
                //if(matrixTrip==null)
                //    publishProgress("The Trips are empty");
                //updateTrip(matrixTrip);
            } else if (type[0] == 2) {
                ///// TODO: 27/06/2017  implement

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            android.app.AlertDialog.Builder myAlert = new android.app.AlertDialog.Builder(context);
            myAlert.setMessage(values[0]).create();
            myAlert.setTitle("Error with load Data");
            //myAlert.setIcon(R.drawable.error);
            myAlert.show();
            //Toast.makeText(context, values[0], Toast.LENGTH_LONG).show();
        }
    }

    public static int convertRideIdToIndex(String list, int rideId) {
        switch (list) {
            case AvailableRidesListName:
                for (int i = 0; i < AvailableRides.size(); i++) {
                    if (AvailableRides.get(i).getRideId() == rideId)
                        return i;
                }
                break;
            case MyRideListName:
                for (int i = 0; i < MyRide.size(); i++) {
                    if (MyRide.get(i).getRideId() == rideId)
                        return i;
                }
                break;
            case HistoricRidesListName:
                for (int i = 0; i < HistoricRides.size(); i++) {
                    if (HistoricRides.get(i).getRideId() == rideId)
                        return i;
                }
                break;
            case RegionsListName:
                for (int i = 0; i < Regions.size(); i++) {
                    if (Regions.get(i).getRegionID() == rideId)
                        return i;
                }
            default:
                return -1;
        }
        return -1;
    }

    public static int convertGroupIdToIndex(String list, int groupId) {
        switch (list) {
            case AvailableRidesListName:
                for (int i = 0; i < AvailableRides.size(); i++) {
                    if (AvailableRides.get(i).getGroupId() == groupId)
                        return i;
                }
                break;
            case MyRideListName:
                for (int i = 0; i < MyRide.size(); i++) {
                    if (MyRide.get(i).getGroupId() == groupId)
                        return i;
                }
                break;
            case HistoricRidesListName:
                for (int i = 0; i < HistoricRides.size(); i++) {
                    if (HistoricRides.get(i).getGroupId() == groupId)
                        return i;
                }
                break;
            default:
                return -1;
        }
        return -1;
    }

    public static int convertGroupIdToIndexFirst(String list, int groupId) {
        switch (list) {
            case AvailableRidesListName:
                for (int i = 0; i < AvailableRides.size(); i++) {
                    if (AvailableRides.get(i).getGroupId() == groupId)
                        return i;
                }
                break;
            case MyRideListName:
                for (int i = 0; i < MyRide.size(); i++) {
                    if (MyRide.get(i).getRideId() == groupId)
                        return i;
                }
                break;
            case HistoricRidesListName:
                for (int i = 0; i < HistoricRides.size(); i++) {
                    if (HistoricRides.get(i).getRideId() == groupId)
                        return i;
                }
                break;
            case RegionsListName:
                for (int i = 0; i < Regions.size(); i++) {
                    if (Regions.get(i).getRegionID() == groupId)
                        return i;
                }
            default:
                return -1;
        }
        return -1;
    }
}
