package project.java.tbusdriver.Service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Travel;
import project.java.tbusdriver.usefulFunctions;

import static project.java.tbusdriver.usefulFunctions.POST;

/**
 * Created by אור איטח on 02/05/2018.
 */
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////update the server with the current location of the driver //////////////
////////////////////////////////////////////////////////////////////////////////////////////////
public class updateLocationService extends Service {

    double Lat;
    double Lng;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LatLng currentLocation;
        Double[] LatLng = new Double[2];
        try {
            Travel travelFragment = Travel.newInstance();
            Bundle data = intent.getExtras();
            if (data != null) {
                LatLng[0] = data.getDouble("Lat");
                LatLng[1] = data.getDouble("Lng");
            } else {
                Location lastKnownLocation = travelFragment.getmLastKnownLocation();
                if (lastKnownLocation != null) {
                    LatLng[0] = lastKnownLocation.getLatitude();
                    LatLng[1] = lastKnownLocation.getLongitude();
                } else {
                    LatLng[0] = (double) 5;
                    LatLng[1] = (double) 5;
                }
            }
            // if the driver is busy we send null
            if (usefulFunctions.busy == true) {
                LatLng[0] = null;
                LatLng[1] = null;
            }
            //currentMyRide = temp.getParcelable("myRide");
            //currentMyRide = intent.getParcelableExtra("myRide");
        } catch (Exception ex) {
            int x = 5;
        }
        new updateLocationService.UpdateLocation().execute(LatLng);
        return super.onStartCommand(intent, flags, startId);
    }

    class UpdateLocation extends AsyncTask<Double, String, String> {
        @Override
        protected String doInBackground(Double... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";

            try {
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("lat", params[0]);
                parameters.put("lng", params[1]);
                toReturn = POST(Const.UPDATE_LOCATION_URI.toString(), parameters);
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK") == 0) {
                    //listDsManager.updateMyRides(toReturn);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
