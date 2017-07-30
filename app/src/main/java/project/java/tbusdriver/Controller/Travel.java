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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import project.java.tbusdriver.R;
import project.java.tbusdriver.usefulFunctions;
import project.java.tbusdriver.usefulFunctions.*;

import static android.content.Context.LOCATION_SERVICE;

public class Travel extends Fragment
            implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    private static  int DEFAULT_ZOOM =10 ;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted=false;
    private Location mLastKnownLocation=null;
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

    boolean doZoom;

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
        //mapFragment.getMapAsync(this);
        // Do other setup activities here too, as described elsewhere in this tutorial.
        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
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
        mapFragment=(SupportMapFragment) myActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mMapView=(MapView)myActivity.findViewById(R.id.map);

        //myActivity.setTitle("Travel");

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
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "onConnected");
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        //mapFragment = (SupportMapFragment) myActivity.getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkGPSEnable();
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
        getDeviceLocation();
    }

    public void busy(View v) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
            myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            usefulFunctions.busy=true;

        }
    }

    public void available(View v)
    {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
            myFloating.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            usefulFunctions.busy=false;

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

    //maybe here the problem, look good and work just one time
    private void getDeviceLocation() {
        boolean mLocationPermissionGranted= false;
        //premmision hendler
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
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }
        // Set the map's camera position to the current location of the device.
        //look like the problem is here
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            LatLng jerusalem = new LatLng(31.75, 35.2);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(jerusalem));
            mMap.animateCamera(CameraUpdateFactory.zoomTo((float) 10.5));
            Log.v(TAG, "Current location is null. Using defaults.");
            //the problem, probably DEFAULT_ZOOM inisilaze to something wrong
            //mMap.moveCamera(CameraUpdateFactory.newLatLn3.
            // gZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
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
            mLastKnownLocation = null;
        }
    }

    private void checkGPSEnable()
    {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(myActivity, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
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
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(getActivity()!=null)
                getActivity().setTitle("Travel");
        }
        else {
        }
    }

}
