package project.java.tbusdriver.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.MyLocation;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;
import project.java.tbusdriver.usefulFunctions;

import static android.content.Context.LOCATION_SERVICE;
import static java.lang.Math.pow;

public class Travel extends Fragment
            implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            View.OnClickListener{

        //region Variable
        private static int DEFAULT_ZOOM =10 ;
        private GoogleApiClient mGoogleApiClient;
        private GoogleMap mMap;

        boolean mRequestingLocationUpdates=true;
        FusedLocationProviderClient mFusedLocationClient;
        LocationCallback mLocationCallback;
        LocationRequest mLocationRequest;

        private boolean mLocationPermissionGranted=false;
        private Location mLastKnownLocation=null;
        private Location mPreviousLastKnownLocation=null;
        private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
        private LatLng mDefaultLocation;
        private CameraPosition mCameraPosition;
        public static final String TAG = Travel.class.getSimpleName();
        View myView;
        FragmentActivity myActivity;
        private OnFragmentInteractionListener mListener;
        Button availableButton;
        Button busyButton;
        FloatingActionButton myFloating;
        SupportMapFragment mapFragment;
        private MapView mMapView;
        public static Travel instance;
        LocationManager locationManager;
        Marker mPositionMarker;
        boolean doZoom;
        ListDsManager listDsManager;
        Ride temp;
        boolean inRoute;
        //endregion

        public Travel() {
            // Required empty public constructor
        }


        public static Travel newInstance() {
            if(instance==null) {
                instance = new Travel();
            }
            else {

                return instance;
            }
            return instance;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            myActivity=getActivity();
            doZoom=true;


            locationManager = (LocationManager) myActivity.getSystemService(LOCATION_SERVICE);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) myActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            listDsManager=(ListDsManager) new Factory(getActivity()).getInstance();
            //mapFragment.getMapAsync(this);
            // Do other setup activities here too, as described elsewhere in this tutorial.
            // Build the Play services client for use by the Fused Location Provider and the Places API.
            // Use the addApi() method to request the Google Places API and the Fused Location Provider.
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(myActivity);
            mLocationRequest=new LocationRequest();
            mLocationRequest.setInterval(3000000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    for (Location location : locationResult.getLocations()) {
                        if(mPreviousLastKnownLocation!=null)
                        {   double LKLLo=mPreviousLastKnownLocation.getLongitude();
                            double LKLLa=mPreviousLastKnownLocation.getLatitude();
                            double LLo=location.getLongitude();
                            double LLa=location.getLatitude();
                            double deviation=0.01;
                            if (pow(deviation,2)<pow((LKLLa-LLa),2)+pow((LKLLo-LLo),2))
                                usefulFunctions.showAlert(myActivity,"the location change");
                        }
                    }
                }
            };

            if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
                mGoogleApiClient = new GoogleApiClient.Builder(myActivity)
                        .enableAutoManage(myActivity /* FragmentActivity */,
                                this /* OnConnectionFailedListener */)
                        .addConnectionCallbacks(this)
                        //.addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .build();
                mGoogleApiClient.connect();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            myView= inflater.inflate(R.layout.fragment_travel, container, false);

            availableButton = (Button) myView.findViewById(R.id.available); // you have to use rootview object..
            availableButton.setOnClickListener(this);

            busyButton = (Button) myView.findViewById(R.id.busy); // you have to use rootview object..
            busyButton.setOnClickListener(this);

            myFloating=(FloatingActionButton) myView.findViewById(R.id.getMyLocation); // you have to use rootview object..
            myFloating.bringToFront();
            myFloating.setClickable(true);
            myFloating.setOnClickListener(this);
            myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            mapFragment=(SupportMapFragment) myActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mMapView=(MapView)myActivity.findViewById(R.id.map);
            PercentRelativeLayout allInstruction=(PercentRelativeLayout)myActivity.findViewById(R.id.allInstruction);
            allInstruction.bringToFront();


            return myView;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            //onMapReady();
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
            if(getActivity()!=null)
            {
                myActivity=getActivity();
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

                /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */

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
            //mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
//
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
        @Override
        public void onMapReady(GoogleMap map) {
            mMap =map;
            //// Get the current location of the device and set the position of the map.
            if(temp!=null && inRoute==true)
            {
                drawMap();
                drawStation(temp.getRoute().getLocations());
                ArrayList<MyLocation> station=temp.getRoute().getLocations();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                station.get(0).getMyLocation().getLatitude(),
                                station.get(0).getMyLocation().getLongitude()))
                        .title("station")

                ).showInfoWindow();
            }
            getDeviceLocation();
        }

        //region Button Handler
        public void busy(View v) {
            if (mListener != null) {
                //mListener.onFragmentInteraction(uri);
                myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                busyButton.setBackgroundResource(R.drawable.busy);
                availableButton.setBackgroundResource(R.drawable.start);
                usefulFunctions.busy=true;
                drawNavigationInstruction();
                setBigInstruction("left");
                setSmallInstructionP("right");
                drawStation(temp.getRoute().getLocations());
                //drawStation(listDsManager.getAvailableRides().get(2).getRoute().getLocations());
            }
        }

        public void available(View v)
        {
            if (mListener != null) {
                //mListener.onFragmentInteraction(uri);
                myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                busyButton.setBackgroundResource(R.drawable.start);
                availableButton.setBackgroundResource(R.drawable.available);
                usefulFunctions.busy=false;
                drawMap();
            }
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.available:
                    available(v);
                    //myFloating.setDrawingCacheBackgroundColor(323);
                    break;
                case R.id.busy:
                    busy(v);
                    break;
                case R.id.getMyLocation:
                    if(mMap != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                        ///DEFAULT_ZOOM =14; // TODO: 30/06/2017 good for view nibgerood
                        if(doZoom) {
                            DEFAULT_ZOOM = 16;
                            doZoom=false;
                        }
                        else {
                            DEFAULT_ZOOM = 14;
                            doZoom=true;
                        }
                        getDeviceLocation();
                        updateLocationUI();
                    }
                    break;
                default:
                    break;
            }
        }
        //endregion

        //maybe here the problem, look good and work just one time
        private void getDeviceLocation() {
            boolean mLocationPermissionGranted= false;
            //permission handler
            if (ContextCompat.checkSelfPermission(myActivity.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(myActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            if (mLocationPermissionGranted) {
                mPreviousLastKnownLocation=mLastKnownLocation;
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
            if (mMap == null)
            {
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
            }
            else {
                if (Build.VERSION.SDK_INT >= 23) {
                    ActivityCompat.requestPermissions(myActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                else
                    /////////////////////////check maybe need to be false
                    mLocationPermissionGranted = true;
            }

            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
            }
            else
            {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;//?
            }
        }

        //region GPS Enable?
        private void checkGPSEnable()
        {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //Toast.makeText(myActivity, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            }else{
                showGPSDisabledAlertToUser();
            }
        }

        private void showGPSDisabledAlertToUser(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myActivity);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Goto Settings Page To Enable GPS",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        //endregion


        @Override
        public void onResume()
        {
            super.onResume();

            if(isHidden()==false)
            {
                int rideid;
                int index;
                myActivity=getActivity();
                Bundle bundle = this.getArguments();
                if (bundle != null) {
                    rideid = bundle.getInt("RIDEID", 0);
                    index = listDsManager.convertRideIdToIndex("MyRide", rideid);
                    temp = listDsManager.getMyRide().get(index);
                    inRoute=true;
                }
                myActivity.setTitle("נסיעה");
                mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
                mapFragment.getMapAsync(this);

                if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }
            }
        }

        private void startLocationUpdates() {
            if (ContextCompat.checkSelfPermission(myActivity.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
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
        public void onPause()
        {
            super.onPause();
            drawMap();
        }

        //region NavigationInstruction
        private void drawNavigationInstruction()
        {
            if(getActivity()!=null)
            {
                myActivity=getActivity();
                RelativeLayout allInstruction=(RelativeLayout)myActivity.findViewById(R.id.allInstruction);
                allInstruction.setVisibility(View.VISIBLE);
            }
            //ins.invalidate();
        }
        private void drawMap()
        {
            if(getActivity()!=null)
            {
                myActivity=getActivity();
                //PercentRelativeLayout allTravel=(PercentRelativeLayout)myActivity.findViewById(R.id.allTravel);
                //allTravel.bringToFront();
                //allTravel.invalidate();
                RelativeLayout allInstruction=(RelativeLayout)myActivity.findViewById(R.id.allInstruction);
                allInstruction.setVisibility(View.GONE);
            }
        }
        private void setBigInstruction(String direction)
        {
            ImageView imageFirstInstruction = (ImageView) myActivity.findViewById(R.id.imageFirstInstruction);
            TextView textFirstInstruction = (TextView)myActivity.findViewById(R.id.textFirstInstruction);
            switch (direction)
            {
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
        private void setSmallInstructionP(String direction)
        {
            ImageView imageSecondInstruction = (ImageView) myActivity.findViewById(R.id.imageSecondInstruction);
            switch (direction)
            {
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

        //region custom marker


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
//
//
            //animateMarker(mPositionMarker, location); // Helper method for smooth
            // animation
//
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
    //endregion
    //region routeAndDraw
    public void drawStation(ArrayList<MyLocation> station )
    {
        for (int i = 0; i < station.size(); i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            station.get(i).getMyLocation().getLatitude(),
                            station.get(i).getMyLocation().getLongitude()))
                    .title("station"+i)
            ).showInfoWindow();
        }
    }
    //end region
}
