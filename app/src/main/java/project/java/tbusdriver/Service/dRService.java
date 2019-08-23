package project.java.tbusdriver.Service;

/**
 * Created by אור איטח on 12/06/2017.
 */

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import project.java.tbusdriver.Controller.Travel;
import project.java.tbusdriver.usefulFunctions;

import static project.java.tbusdriver.Controller.Travel.newInstance;

///////////////////////////////// currently not in use //////////////////////
public class dRService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    public void onCreate() {
        int x = 4;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(final Void... params) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Intent intent = new Intent("myUpdateIntent");
                        //int oldCountBusiness = (getContentResolver().query(ContentProvide.ACTION_URI, new String[]{}, "", new String[]{}, "")).getCount();
                        //int oldCountActivities = (getContentResolver().query(ContentProvide.BUSINESS_URI, new String[]{}, "", new String[]{}, "")).getCount();
                        //int newCountBusiness = oldCountBusiness;
                        //int newCountActivities = oldCountActivities;
                        //Your logic that service will perform will be placed here
                        //In this example we are just looping and waits for 1000 milliseconds in each loop.
                        while (true) {
                            try {
                                Thread.sleep(5000);
                                if (usefulFunctions.busy == false && usefulFunctions.Token != null) {
                                    //// TODO: 17/07/2017 send update to server
                                    Travel travelFragment = newInstance();
                                    Location mLastKnowLocation = travelFragment.getmLastKnownLocation();
                                }

                                //send query to the data base and count him
                                //newCountActivities = (getContentResolver().query(ContentProvide.ACTION_URI, new String[]{}, "", new String[]{}, "")).getCount();
                                //newCountBusiness = (getContentResolver().query(ContentProvide.BUSINESS_URI, new String[]{}, "", new String[]{}, "")).getCount();
                                // we check if added some Activity
                                //if (oldCountActivities != newCountActivities) {
                                //    oldCountActivities = newCountActivities;
                                //    //send intent who say have more Activity
                                //    intent.putExtra("message", "ActivitiesUpdate");
                                //    intent.setAction("ActivitiesUpdate");
                                //    sendBroadcast(intent);
                                //}
                                //// we check if added some Business
                                //if (oldCountBusiness != newCountBusiness) {
                                //    oldCountBusiness = newCountBusiness;
                                //    //send intent who say have more Business
                                //    intent.putExtra("message", "BusinessUpdate");
                                //    intent.setAction("BusinessUpdate");
                                //    sendBroadcast(intent);
                                //}

                            } catch (Exception e) {
                            }
                        }
                    }
                }).start();
                return null;
            }
        }.execute();
    }
}
