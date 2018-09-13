package project.java.tbusdriver.Service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.BaseAdapter;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Activitys.MainActivity;
import project.java.tbusdriver.Controller.Travel;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;

import java.util.Date;
import java.util.Locale;

import static project.java.tbusdriver.usefulFunctions.GET;
import static project.java.tbusdriver.usefulFunctions.showAlert;


/**
 * Created by אור איטח on 11/04/2018.
 */
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////// Auto start ride if the time is come //////////
//////////////////////////////////////////////////////////////////////////////
public class rideStartService extends Service {
    ArrayList<Ride> currentMyRide;
    Travel travelFragment;
    ListDsManager listDsManager;

    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listDsManager = (ListDsManager) new Factory(null).getInstance();
        //listDsManager = new ListDsManager()
        try {
            Bundle temp = intent.getExtras();
            //currentMyRide = temp.getParcelable("myRide");
            //currentMyRide = intent.getParcelableExtra("myRide");
        } catch (Exception ex) {
            int x = 5;
        }
        new rideStartService.UsersTask().execute();
        doWork();
        //TODO do something useful
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void doWork() {
        int x = 5;
        if (listDsManager.getMyRide() == null) {
            return;
        }
        Date now = new Date();

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        for (Ride currentRide : listDsManager.getMyRide()) {
            try {
                Date currentTravelTime = format.parse(currentRide.getTravelTime());
                currentTravelTime.setTime(currentTravelTime.getTime() + 1);
                //if (currentTravelTime.after(now))
                if (true) {
                    travelFragment = Travel.newInstance();
                    travelFragment.startRide(currentRide.getRideId());
                    System.out.println("intent Received");
                    String jsonString = Const.TravelFragmentName;
                    Intent RTReturn = new Intent(MainActivity.RECEIVE_JSON);
                    RTReturn.putExtra("fragment", jsonString);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
                    //setFragment(Const.TravelFragmentName);
                    //do something
                    return;
                }
            } catch (Exception ex) {
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                }
            }
        });
    }

    class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";

            try {
                toReturn = GET(Const.USER_MY_RIDE_URI.toString());
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK") == 0) {
                    listDsManager.updateMyRides(toReturn);
                    publishProgress("");
                    toReturn = "";
                } else {
                    publishProgress("something get wrong      " + toReturn);
                }
            } catch (Exception e) {
                publishProgress(e.getMessage());

            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("")) {
                //every thing is okay
                //mListener.onFragmentInteraction("");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //user[0]=Phone user[1]=User Name
            //check if have any error
            if (values[0].length() > 1) {
                //showAlert(myActivity,values[0]);
            } else {
                //showAlert(myActivity,"נסיעה נלקחה בהצלחה");
                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }
    }
    //public void onCreate() {
//
    //    new Thread(new Runnable()
    //    {
    //        @Override
    //        public void run() {
    //            while (true)
    //            {
    //                if (currentMyRide == null)
    //                {
    //                    continue;
    //                }
    //                Date now = new Date();
    //                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    //                for ( Ride currentRide: currentMyRide) {
    //                    try {
    //                        Date currentTravelTime = format.parse(currentRide.getTravelTime());
    //                        if (currentTravelTime.after(now))
    //                        {
    //                            travelFragment = Travel.newInstance();
//
    //                            //do something
    //                        }
    //                    }
    //                    catch (Exception ex )
    //                    {
//
    //                    }
//
    //                }
    //            }
    //        }
    //    });
    //}
}
