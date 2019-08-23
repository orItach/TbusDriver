package project.java.tbusdriver.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Adapter.StationsAdapter;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.MyLocation;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;
import project.java.tbusdriver.Service.updateLocationService;
import project.java.tbusdriver.usefulFunctions;

import static android.content.Context.LOCATION_SERVICE;
import static java.lang.Math.pow;
import static project.java.tbusdriver.Const.AvailableRidesListName;
import static project.java.tbusdriver.usefulFunctions.POST;

public class Travel extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
        , com.google.android.gms.location.LocationListener {
    //GoogleApiClient.ConnectionCallbacks,
    //GoogleApiClient.OnConnectionFailedListener,
    //LocationListener {


    //region Variable
    // default zoom on the map
    private static int DEFAULT_ZOOM = 10;
    private GoogleApiClient mGoogleApiClient;
    // the map as object
    private GoogleMap mMap;

    // not in use
    boolean mRequestingLocationUpdates = true;
    // the client to get the current location
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;

    // bool that say if the location premmsion is granted
    private boolean mLocationPermissionGranted = false;
    // the last known location, not have to be the current
    private Location mLastKnownLocation = null;
    // same as above but the previous
    private Location mPreviousLastKnownLocation = null;
    // some const to req location premission
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private LatLng mDefaultLocation;
    private CameraPosition mCameraPosition;
    public static final String TAG = Travel.class.getSimpleName();
    View myView;
    FragmentActivity myActivity;
    private OnFragmentInteractionListener mListener;
    Button availableButton;
    Button busyButton;
    FloatingActionButton myFloating;
    // the map fragment as a object
    SupportMapFragment mapFragment;
    private MapView mMapView;
    public static Travel instance;
    LocationManager locationManager;
    Marker mPositionMarker;
    boolean doZoom;
    ListDsManager listDsManager;
    Ride ride;
    // bool say if we in ride
    public boolean inRoute;
    // bool say if we want to show the station list
    boolean showStations; ///SHOWSTATIONS
    ListView stationsList;
    ArrayList<MyLocation> stations;
    StationsAdapter stationsAdapter;
    TextView DragbleText;
    //endregion

    public Travel() {
        // Required empty public constructor
    }

    // implement the singleton pattern
    public static Travel newInstance() {
        if (instance == null) {
            instance = new Travel();
        } else {

            return instance;
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
        doZoom = true;
        SharedPreferences sharedPref = myActivity.getPreferences(Context.MODE_PRIVATE);
        //boolean busySharedPreferences = getResources().getBoolean(Const.BusySharedPreferences);
        boolean busySharedPreferences = sharedPref.getBoolean(Const.BusySharedPreferences, false);
        usefulFunctions.busy =busySharedPreferences;

        locationManager = (LocationManager) myActivity.getSystemService(LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) myActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        listDsManager = (ListDsManager) new Factory(getActivity()).getInstance();
        //mapFragment.getMapAsync(this);
        // Do other setup activities here too, as described elsewhere in this tutorial.
        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.

        // define some parameter of the client location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(myActivity);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // define what happen when we get the location
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    if (mPreviousLastKnownLocation != null) {
                        double LKLLo = mPreviousLastKnownLocation.getLongitude();
                        double LKLLa = mPreviousLastKnownLocation.getLatitude();
                        double LLo = location.getLongitude();
                        double LLa = location.getLatitude();
                        double deviation = 0.01;
                        if (pow(deviation, 2) < pow((LKLLa - LLa), 2) + pow((LKLLo - LLo), 2))
                            usefulFunctions.showAlert(myActivity, "the location change");
                    }
                }
            }
        };

        // define some parameter of the googleApiClient
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(myActivity)
                    .enableAutoManage(myActivity /* FragmentActivity */,
                            this /* OnConnectionFailedListener */)
                    .addConnectionCallbacks(this)
                    //.addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_travel, container, false);

        availableButton = (Button) myView.findViewById(R.id.available); // you have to use rootview object..
        availableButton.setOnClickListener(this);

        busyButton = (Button) myView.findViewById(R.id.busy); // you have to use rootview object..
        busyButton.setOnClickListener(this);

        // define some view od the floating button
        myFloating = (FloatingActionButton) myView.findViewById(R.id.getMyLocation); // you have to use rootview object..
        myFloating.bringToFront();
        myFloating.setClickable(true);
        myFloating.setOnClickListener(this);
        myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

        mapFragment = (SupportMapFragment) myActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mMapView = (MapView) myActivity.findViewById(R.id.map);
        PercentRelativeLayout allInstruction = (PercentRelativeLayout) myActivity.findViewById(R.id.allInstruction);
        allInstruction.bringToFront();
        //stationsList =(ListView) myView.findViewById(R.id.DragbleText);

        //stationsList.setOnDragListener(new View.OnDragListener()
        stationsList = (ListView) myView.findViewById(R.id.stationList);
        DragbleText = (TextView) myView.findViewById(R.id.Test);

        // define what happen when hold the TextView
        DragbleText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                //ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(DragbleText);

                v.startDrag(null, myShadow, null, 0);
                return true;
            }
        });

        // define what happen when drag the TextView
        DragbleText.setOnDragListener(new View.OnDragListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) v.getLayoutParams();
                DisplayMetrics displayMetrics = myActivity.getResources().getDisplayMetrics();

                // dpWidth is the 100% ?
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        //where its now

                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();

                        v.invalidate();
                        // Invalidate the view to force a redraw in the new tint

                        // returns true to indicate that the View can accept the dragged data.
                        return true;


                    case DragEvent.ACTION_DRAG_ENTERED:

                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        ShowListStations();
                        //usefulFunctions.showAlert(myActivity, "ACTION_DRAG_ENTERED x: " +x_cord + "y: " + y_cord);
                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        //usefulFunctions.showAlert(myActivity, "ACTION_DRAG_LOCATION x: " +x_cord + "y: " + y_cord);
                        //layoutParams.leftMargin = x_cord;
                        //layoutParams.topMargin = y_cord;
                        //v.setLayoutParams(layoutParams);
                        // Ignore the event
                        // return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        //usefulFunctions.showAlert(myActivity, "ACTION_DRAG_EXITED x: " +x_cord + "y: " + y_cord);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:
                        //x_cord = (int) event.getX();
                        //y_cord = (int) event.getY();
                        // Invalidates the view to force a redraw

                        v.invalidate();
                        // Returns true. DragEvent.getResult() will return true.
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        // Invalidates the view to force a redraw

                        v.invalidate();

                        // returns true; the value is ignored.
                        return true;

                    // An unknown action type was received.
                    default:
                        Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                        break;
                }
                return true;
            }
        });
        if(usefulFunctions.busy){
            drawBusy();
        }
        else {
            drawAvailable();
        }
        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //onMapReady();
        if (mMap != null) {
            //// Get the current location of the device and set the position of the map.
            DEFAULT_ZOOM = 16;
            getDeviceLocation();
            //updateLocationUI();
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (getActivity() != null) {
            myActivity = getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    //* This interface must be implemented by activities that contain this
    //* fragment to allow an interaction in this fragment to be communicated
    //* to the activity and potentially other fragments contained in that
    //* activity.
    //* <p>
    //* See the Android Training lesson <a href=
    //* "http://developer.android.com/training/basics/fragments/communicating.html"
    //* >Communicating with Other Fragments</a> for more information.
    //*/

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    //region Connection Handler
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "onConnected");
        if (mMap != null) {
            //// Get the current location of the device and set the position of the map.
            DEFAULT_ZOOM = 16;
            getDeviceLocation();
            updateLocationUI();
        }


        //mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        ////mapFragment = (SupportMapFragment) myActivity.getSupportFragmentManager()
        ////        .findFragmentById(R.id.map);
        //if (mapFragment!=null){
        //    mapFragment.getMapAsync(this);
        //    checkGPSEnable();
        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "onConnectionSuspended");
        //super.onConnectionSuspended(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.v(TAG, "onConnectionFailed");
        //super.onConnectionFailed(connectionResult);
    }
    //endregion

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //@Override
    //public void onMapReady(GoogleMap googleMap) {
    //    mMap = googleMap;
    //    // Add a marker in Sydney and move the camera
    //    LatLng sydney = new LatLng(-34, 151);
    //    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    //}

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    // define what happen when we get the map object from google
    @SuppressLint("NewApi")
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //// Get the current location of the device and set the position of the map.
        if (mMap != null) {
            //// Get the current location of the device and set the position of the map.
            DEFAULT_ZOOM = 16;
            getDeviceLocation();
            updateLocationUI();
        }

        // define what happen when we are in route, e.g. draw route
        if (ride != null && inRoute) {
            drawMap();
            drawStation(ride.getRoute().getLocations());
            stations = ride.getRoute().getLocations();
            if (showStations) {
                ShowListStations();
            }
            drawRoute(ride.getRoute().getLocations());
            DEFAULT_ZOOM = 13;
            if (mLastKnownLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            }
            if (mLastKnownLocation == null && ride != null && ride.getRoute() != null) {

                MyLocation firstLocation = ride.getRoute().getLocations().get(0);
                LatLng newMapLocation = new LatLng(firstLocation.getMyLocation().getLatitude(),
                        firstLocation.getMyLocation().getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        newMapLocation, DEFAULT_ZOOM));
            }
        } else {
            try {
                RemoveListStations();
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
    }

    //region Button Handler
    public void busy(View v) {
        myActivity.startService(new Intent(myActivity, updateLocationService.class));
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
            drawBusy();
            usefulFunctions.busy = true;
            SharedPreferences sharedPref = myActivity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Const.BusySharedPreferences,true );
            editor.commit();
            //drawNavigationInstruction();
            //setBigInstruction("left");
            //setSmallInstructionP("right");
            //drawStation(ride.getRoute().getLocations());
            Double[] LatLng = new Double[2];
            LatLng[0] = null;
            LatLng[1] = null;
            new Travel.UpdateLocation().execute(LatLng);
        }
    }

    // update the server with the current driver location
    static class UpdateLocation extends AsyncTask<Double, String, String> {
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

    public void available(View v) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
            drawAvailable();
            usefulFunctions.busy = false;
            SharedPreferences sharedPref = myActivity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Const.BusySharedPreferences,false );
            editor.commit();
            drawMap();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.available:
                available(v);
                //myFloating.setDrawingCacheBackgroundColor(323);
                break;
            case R.id.busy:
                busy(v);
                break;
            case R.id.getMyLocation:
                if (mMap != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    ///DEFAULT_ZOOM =14; // good for view nibgerood
                    if (doZoom) {
                        DEFAULT_ZOOM = 16;
                        doZoom = false;
                    } else {
                        DEFAULT_ZOOM = 14;
                        doZoom = true;
                    }
                    getDeviceLocation();
                    updateLocationUI();
                }
                break;
            default:
                break;
        }
    }
    private void drawBusy(){
        myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        busyButton.setBackgroundResource(R.drawable.busy);
        availableButton.setBackgroundResource(R.drawable.start);
    }

    private void drawAvailable(){
        myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        busyButton.setBackgroundResource(R.drawable.start);
        availableButton.setBackgroundResource(R.drawable.available);
    }
    //endregion

    //maybe here the problem, look good and work just one time
    private void getDeviceLocation() {
        boolean mLocationPermissionGranted = false;
        //permission handler
        if (ContextCompat.checkSelfPermission(myActivity.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(myActivity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mPreviousLastKnownLocation = mLastKnownLocation;
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            //mFusedLocationClient.getLastLocation()
            //        .addOnSuccessListener((Executor) myActivity, new OnSuccessListener<Location>() {
            //            @Override
            //            public void onSuccess(Location location) {
            //                // Got last known location. In some rare situations this can be null.
            //                if (location != null) {
            //                    // ...
            //                }
            //            }
            //        });
        }
        // Set the map's camera position to the current location of the device.
        //look like the problem is here
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            LatLng jerusalem = new LatLng(31.75, 35.2);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(jerusalem));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
            Log.v(TAG, "Current location is null. Using defaults.");
            //the problem, probably DEFAULT_ZOOM initialize to something wrong
            //mMap.moveCamera(CameraUpdateFactory.newLatLn3.
            // gZoom(mDefaultLocation, DEFAULT_ZOOM));
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        // A step later in the tutorial adds the code to get the device location.
    }

    // define what happen when we get the permission req result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(myActivity.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(myActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else
                /////////////////////////check maybe need to be false
                mLocationPermissionGranted = true;
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;//?
        }
    }

    //region GPS Enable?
    private void checkGPSEnable() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(myActivity, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myActivity);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    //endregion


    @Override
    public void onResume() {
        super.onResume();

        //if (mRequestingLocationUpdates) {
        //    startLocationUpdates();
        //    //getDeviceLocation();
        //}
        if (mMap != null) {
            //// Get the current location of the device and set the position of the map.
            DEFAULT_ZOOM = 16;
            //startLocationUpdates();
            getDeviceLocation();
            //updateLocationUI();
        }

        if (!isHidden()) {
            int rideId;
            int index;
            myActivity = getActivity();
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                boolean haveInMyRide =true;
                rideId = bundle.getInt("RIDEID", 0);
                index = ListDsManager.convertGroupIdToIndex("MyRide", rideId);
                if( index == -1){
                    index = ListDsManager.convertGroupIdToIndex(AvailableRidesListName,rideId);
                    haveInMyRide = false;
                }
                if (haveInMyRide){
                    ride = ListDsManager.getMyRide().get(index);
                }
                else {
                    ride = ListDsManager.getAvailableRides().get(index);
                }
                boolean showRide = bundle.getBoolean("SHOWSTATIONS"); ///SHOWRIDE
                if (showRide) {
                    showStations = true;
                }
                inRoute = true;
            }
            //else
            //{
            //    try {
            //        RemoveListStations();
            //    } catch (Exception e) {
            //        Log.e("error", e.toString());
            //    }
            //}
            myActivity.setTitle("נסיעה");
            mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            mapFragment.getMapAsync(this);
        }

    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(myActivity.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(myActivity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        drawMap();
    }

    //region NavigationInstruction
    private void drawNavigationInstruction() {
        if (getActivity() != null) {
            myActivity = getActivity();
            RelativeLayout allInstruction = (RelativeLayout) myActivity.findViewById(R.id.allInstruction);
            allInstruction.setVisibility(View.VISIBLE);
        }
        //ins.invalidate();
    }

    private void drawMap() {
        if (getActivity() != null) {
            myActivity = getActivity();
            //PercentRelativeLayout allTravel=(PercentRelativeLayout)myActivity.findViewById(R.id.allTravel);
            //allTravel.bringToFront();
            //allTravel.invalidate();
            RelativeLayout allInstruction = (RelativeLayout) myActivity.findViewById(R.id.allInstruction);
            allInstruction.setVisibility(View.GONE);
        }
    }

    private void setBigInstruction(String direction) {
        ImageView imageFirstInstruction = (ImageView) myActivity.findViewById(R.id.imageFirstInstruction);
        TextView textFirstInstruction = (TextView) myActivity.findViewById(R.id.textFirstInstruction);
        switch (direction) {
            case "left":
                imageFirstInstruction.setImageResource(R.drawable.left);
                textFirstInstruction.setText("turn left");
                break;
            case "right":
                textFirstInstruction.setText("turn right");
                break;
            case "straight":
                break;
            case "left U-turn":
                break;

        }
    }

    private void setSmallInstructionP(String direction) {
        ImageView imageSecondInstruction = (ImageView) myActivity.findViewById(R.id.imageSecondInstruction);
        switch (direction) {
            case "left":
                break;
            case "right":
                imageSecondInstruction.setImageResource(R.drawable.rigth);
                break;
            case "straight":
                break;
            case "left U-turn":
                break;
        }
    }
    //endregion

    //region custom marker - currently not in use
    public void mLocationCallback(Location location) {
        if (location == null)
            return;
        //if (mPositionMarker == null) {
        //    mPositionMarker = mMap.addMarker(new MarkerOptions()
        //            .flat(false)
        //            .anchor(0.5f, 0.5f)
        //            .icon(BitmapDescriptorFactory
        //                    .fromResource(R.drawable.taxi))
        //            .position(
        //                    new LatLng(location.getLatitude(), location
        //                            .getLongitude())));
        //}


        //animateMarker(mPositionMarker, location); // Helper method for smooth
        // animation

        //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location
        //        .getLatitude(), location.getLongitude())));
    }

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    //end region

    //region routeAndDraw
    private void drawStation(@NonNull ArrayList<MyLocation> station) {
        for (int i = 1; i < station.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            station.get(i).getMyLocation().getLatitude(),
                            station.get(i).getMyLocation().getLongitude()))
                    .title("station " + i)
            ).showInfoWindow();
        }
    }

    private void drawRoute(@NonNull ArrayList<MyLocation> station) {
        int numberOfStation = station.size() - 1;
        if (numberOfStation < 1) {
            usefulFunctions.showAlert(myActivity, "אין תחנות בנסיעה זאת, אנא פנה אלינו בהקדם");
            return;
        }
        LatLng first = new LatLng(station.get(0).getMyLocation().getLatitude(), station.get(0).getMyLocation().getLongitude());
        LatLng last = new LatLng(station.get(numberOfStation).getMyLocation().getLatitude(), station.get(numberOfStation).getMyLocation().getLongitude());
        DragbleText.setVisibility(View.VISIBLE);
        myView.invalidate();
        String url = getDirectionsUrl(first, last);

        // Start downloading json data from Google Directions API
        new Travel.DownloadTask().execute(url);

        //for (int i=0; i< station.size()-1; i++)
        //{
        //    mMap.addPolygon(new PolygonOptions()
        //            .add(new LatLng(
        //                    station.get(i).getMyLocation().getLatitude(),
        //                    station.get(i).getMyLocation().getLongitude()))
        //            .add(new LatLng(
        //                    station.get(i+1).getMyLocation().getLatitude(),
        //                    station.get(i+1).getMyLocation().getLongitude())
        //            )
        //    );
        //}
    }

    private String getDirectionsUrl(@NonNull LatLng origin,@NonNull LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        String keyParam = "key="+"AIzaSyBa5Sdd6-OM5tZO6QEysPziGBgDEzYPTUY";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&" + keyParam;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";

        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            try (InputStream iStream = urlConnection.getInputStream()) {

                // Creating an http connection to communicate with url

                // Connecting to url

                // Reading data from url

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                Log.d("Exception url", e.toString());
            } finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e){
            Log.d("Exception url", e.toString());
        }
        return data;
    }

    // req the route from google api
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                //DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
            for (int i = 0; result != null && i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.BLUE);
            }
            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return routes;
    }

    private List<LatLng> decodePoly(@NonNull String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
    //end region

    public Location getmLastKnownLocation() {
        return mLastKnownLocation;
    }

    public void startRide(int rideId) {
        int index = ListDsManager.convertRideIdToIndex("MyRide", rideId);
        ride = ListDsManager.getMyRide().get(index);
        inRoute = true;

    }

    //Location update region
    // define what happen when the driver change is location
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        int speed = (int) (location.getSpeed() * 2.2369);
        // send location data to server
        Intent myUpdateLocationServiceIntent = new Intent(getActivity(), updateLocationService.class);
        myUpdateLocationServiceIntent.putExtra("Lat", currentLatitude);
        myUpdateLocationServiceIntent.putExtra("Lng", currentLongitude);
        try {
            getContext().startService(myUpdateLocationServiceIntent);
        } catch (Exception e) {
            int x = 5;
        }
        //if (isSubscribed) {
        //    JSONObject json = new JSONObject();
        //    try {
        //        json.put("lat", currentLatitude);
        //        json.put("long", currentLongitude);
        //        json.put("time", location.getTime());
        //        json.put("accuracy", location.getAccuracy());
        //        json.put("speed", speed);
        //        json.put("latLng", latLng);
        //        mChannel.trigger("client-location-changed", json.toString());
        //    } catch (JSONException e) {
        //        Log.e(TAG, "Problem adding JSON");
        //    }
        //}
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void ShowListStations() {
        float dpHeight = getDpHeight();
        availableButton.setVisibility(View.GONE);
        busyButton.setVisibility(View.GONE);
        myFloating.setVisibility(View.GONE);
        if (mapFragment.getView() != null) {
            ViewGroup.LayoutParams mapFragmentParams = mapFragment.getView().getLayoutParams();
            mapFragmentParams.height = (int) (dpHeight * 1.50);
            mapFragment.getView().setLayoutParams(mapFragmentParams);
        }
        ViewGroup.LayoutParams stationsListParams = stationsList.getLayoutParams();

        stationsListParams.height = (int) (dpHeight * 0.20);
        stationsList.setLayoutParams(stationsListParams);
        stationsList.setVisibility(View.VISIBLE);
        stationsAdapter = new StationsAdapter(myActivity, R.layout.item_station, stations);
        stationsList.setAdapter(stationsAdapter);
        RelativeLayout.LayoutParams draggableLayoutParams = (RelativeLayout.LayoutParams) DragbleText.getLayoutParams();
        draggableLayoutParams.removeRule(RelativeLayout.ABOVE);
        draggableLayoutParams.addRule(RelativeLayout.ABOVE, R.id.stationList);
        DragbleText.setText("אתה בנסיעה עם תחנות, גרור למטה על מנת להעלימן");
        stationsList.invalidateViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void RemoveListStations() {
        DisplayMetrics displayMetrics = myActivity.getResources().getDisplayMetrics();
        float dpHeight = getDpHeight();
        availableButton.setVisibility(View.VISIBLE);
        busyButton.setVisibility(View.VISIBLE);
        myFloating.setVisibility(View.VISIBLE);
        if (mapFragment != null && mapFragment.getView() != null) {
            ViewGroup.LayoutParams mapFragmentParams = mapFragment.getView().getLayoutParams();
            mapFragmentParams.height = (int) (displayMetrics.heightPixels);
            mapFragment.getView().setLayoutParams(mapFragmentParams);
        }
        ViewGroup.LayoutParams stationsListParams = stationsList.getLayoutParams();

        stationsListParams.height = (int) (0);
        stationsList.setLayoutParams(stationsListParams);
        stationsList.setVisibility(View.GONE);
        //stationsAdapter = new StationsAdapter(myActivity,R.layout.item_station,stations);
        //stationsList.setAdapter(stationsAdapter);
        RelativeLayout.LayoutParams draggableLayoutParams = (RelativeLayout.LayoutParams) DragbleText.getLayoutParams();
        draggableLayoutParams.removeRule(RelativeLayout.BELOW);
        draggableLayoutParams.addRule(RelativeLayout.BELOW, R.id.stationList);
        DragbleText.setText("אתה בנסיעה עם תחנות, גרור למטה על מנת להעלימן");
        stationsList.invalidateViews();
    }

    private float getDpHeight() {
        DisplayMetrics displayMetrics = myActivity.getResources().getDisplayMetrics();

        // dpHeight is the 100% ?
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        return dpHeight;
    }

    public void setInRoute(boolean inRoute) {
        this.inRoute = inRoute;
    }
}
