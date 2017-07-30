package project.java.tbusdriver.Database;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import project.java.tbusdriver.Entities.Ride;

/**
 * Created by אור איטח on 27/06/2017.
 */

public class ListDsManager {
    private static ArrayList<Ride> AvailableRides;
    private static ArrayList<Ride> HistoricRides;
    private static ArrayList<String> Region;
    Context context;

    public ListDsManager(Context context)
    {
        this.context=context;
        AvailableRides=new ArrayList<Ride>();
        HistoricRides=new ArrayList<Ride>();
        Region=new ArrayList<String>();
        new UpdateDataTask().execute(0);//update agency
        new UpdateDataTask().execute(1);//update trips
        new UpdateDataTask().execute(2);
    }

    public static ArrayList<Ride> getAvailableRides() {
        return AvailableRides;
    }

    public static void setAvailableRides(ArrayList<Ride> availableRides) {
        AvailableRides = availableRides;
    }

    public static ArrayList<Ride> getHistoricRide() {
        return HistoricRides;
    }

    public static void setHistoricRide(ArrayList<Ride> historicRide) {
        HistoricRides = historicRide;
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

    public void updateAvailableRides(Cursor matrixTrip) {
        try {
            AvailableRides.clear();
            if (matrixTrip == null) throw new Exception("The trips are empty");
            for (matrixTrip.moveToFirst(); !matrixTrip.isAfterLast(); matrixTrip.moveToNext()) {
                //if(Description.values()[matrixTrip.getInt(matrixTrip.getColumnIndex("description"))]==Description.Travel)
                //AvailableRides.add(new Ride(Description.values()[matrixTrip.getInt(matrixTrip.getColumnIndex("description"))],
                //        matrixTrip.getString(matrixTrip.getColumnIndex("countryName")),
                //        new Date(matrixTrip.getLong(matrixTrip.getColumnIndex("startDate"))),
                //        new Date(matrixTrip.getLong(matrixTrip.getColumnIndex("endDate"))),
                //        matrixTrip.getInt(matrixTrip.getColumnIndex("price")),
                //        matrixTrip.getString(matrixTrip.getColumnIndex("des")),
                //        matrixTrip.getLong(matrixTrip.getColumnIndex("businessID")),
                //        matrixTrip.getString(matrixTrip.getColumnIndex("image")),
                //        matrixTrip.getString(matrixTrip.getColumnIndex("link"))));
            }
        } catch (Exception e) {

        }
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





}
