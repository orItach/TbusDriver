package project.java.tbusdriver.Controller.Activitys;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.FirebaseApp;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Adapter.AvailableRideAdapter;
import project.java.tbusdriver.Controller.Adapter.MyRideAdapter;
import project.java.tbusdriver.Controller.Adapter.RegionAdapter;
import project.java.tbusdriver.Controller.Fragments.Auth;
import project.java.tbusdriver.Controller.Fragments.AvailableRide;
import project.java.tbusdriver.Controller.Fragments.HistoricalRide;
import project.java.tbusdriver.Controller.Fragments.Login;
import project.java.tbusdriver.Controller.Fragments.MyRegion;
import project.java.tbusdriver.Controller.Fragments.MyRide;
import project.java.tbusdriver.Controller.Fragments.PersonalInfo;
import project.java.tbusdriver.Controller.Fragments.Settings;
import project.java.tbusdriver.Controller.Travel;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Day;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;
import project.java.tbusdriver.Service.NotificationIDService;
import project.java.tbusdriver.usefulFunctions;

import static com.firebase.client.utilities.HttpUtilities.HttpRequestType.POST;
import static project.java.tbusdriver.Const.personalInfoFragmentName;
import static project.java.tbusdriver.Const.TravelFragmentName;
import static project.java.tbusdriver.Const.availableRideFragmentName;
import static project.java.tbusdriver.Const.historicRideFragmentName;
import static project.java.tbusdriver.Const.myRegionFragmentName;
import static project.java.tbusdriver.Const.myRideFragmentName;
import static project.java.tbusdriver.Const.settingFragmentName;
import static project.java.tbusdriver.Controller.Travel.newInstance;
import static project.java.tbusdriver.usefulFunctions.POST;
import static project.java.tbusdriver.usefulFunctions.showAlert;


public class MainActivity extends AppCompatActivity
        implements MyRide.OnFragmentInteractionListener,
        AvailableRide.OnFragmentInteractionListener,
        HistoricalRide.OnFragmentInteractionListener,
        Travel.OnFragmentInteractionListener,
        MyRideAdapter.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        Settings.OnFragmentInteractionListener,
        MyRegion.OnFragmentInteractionListener,
        RegionAdapter.OnRegionAdapterInteractionListener,
        PersonalInfo.OnFragmentInteractionListener,
        AvailableRideAdapter.OnFragmentInteractionListener{

    // for manage fragment
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // for navigation drawer
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;

    RWSetting rwSettings = null;
    ImageView menuImage;
    // prepare variable to hold all fragment
    SupportMapFragment mapFragment;
    Travel travelFragment;
    MyRide myRideFragment;
    AvailableRide availableRideFragment;
    Settings settingsFragment;
    MyRegion myRegionFragment;
    HistoricalRide historicalRideFragment;
    PersonalInfo personalInfoFragment;
    boolean doubleBackToExitPressedOnce = false;

    public static final String RECEIVE_JSON = "com.your.package.RECEIVE_RIDE_START";
    private BroadcastReceiver bReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rwSettings=RWSetting.getInstance(this);
        boolean haveToken = false;
        // check if we have the token
        if((rwSettings.getStringSetting("Token")=="" || rwSettings.getStringSetting("Token")==null) )
        {
            //start login
            haveToken = false;
            Intent intent = new Intent(this,LoginAuth.class);
            startActivity(intent);
            return;
        }
        else
        {
            usefulFunctions.Token = rwSettings.getStringSetting("Token");
            haveToken = true;
        }
        //if (haveToken){
            setContentView(R.layout.activity_main);
            // for navigation drawer
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            LinearLayout nevHeader= (LinearLayout)  navigationView.getHeaderView(0);
            menuImage = (ImageView) nevHeader.findViewById(R.id.menuImage);
            //set Travel to be the first fragment
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            travelFragment = newInstance();
            fragmentTransaction.add(R.id.content_main,travelFragment);
            fragmentTransaction.commit();
            //mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment=SupportMapFragment.newInstance();
            menuImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setFragment(personalInfoFragmentName);
                }

            });
        //}

        // need to be deleted after sync with the server
        // init work item
        {
            ListDsManager listDsManager = (ListDsManager) new Factory(this).getInstance();
            String[] region = new String[]{"ירושלים", "בני-ברק", "תל-אביב"};
            Day[] userDaysJ = new Day[7];
            userDaysJ[0] = new Day("א", "00:01", "23:59");
            userDaysJ[1] = new Day("ב", "00:01", "23:59");
            userDaysJ[2] = new Day("ג", "00:01", "23:59");
            userDaysJ[3] = new Day("ד", "00:01", "23:59");
            userDaysJ[4] = new Day("ה", "00:01", "23:59");
            userDaysJ[5] = new Day("ו", "00:01", "23:59");
            userDaysJ[6] = new Day("ש", "00:01", "23:59");
            Region tempRegionJ = new Region(region[0], userDaysJ);
            ListDsManager.getRegions().add(tempRegionJ);
            Day[] userDaysB = new Day[7];
            userDaysB[0] = new Day("א", "00:01", "23:59");
            userDaysB[1] = new Day("ב", "00:01", "23:59");
            userDaysB[2] = new Day("ג", "00:01", "23:59");
            userDaysB[3] = new Day("ד", "00:01", "23:59");
            userDaysB[4] = new Day("ה", "00:01", "23:59");
            userDaysB[5] = new Day("ו", "00:01", "23:59");
            userDaysB[6] = new Day("ש", "00:01", "23:59");
            Region tempRegionB = new Region(region[1], userDaysB);
            ListDsManager.getRegions().add(tempRegionB);
            Day[] userDaysT = new Day[7];
            userDaysT[0] = new Day("א", "00:01", "23:59");
            userDaysT[1] = new Day("ב", "00:01", "23:59");
            userDaysT[2] = new Day("ג", "00:01", "23:59");
            userDaysT[3] = new Day("ד", "00:01", "23:59");
            userDaysT[4] = new Day("ה", "00:01", "23:59");
            userDaysT[5] = new Day("ו", "00:01", "23:59");
            userDaysT[6] = new Day("ש", "00:01", "23:59");
            Region tempRegionT = new Region(region[2], userDaysT);
            ListDsManager.getRegions().add(tempRegionT);
        }

        // currently not in use
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(RECEIVE_JSON)) {
                    String fragmentToSet = intent.getStringExtra("fragment");
                    setFragment(fragmentToSet);
                    //Do something with the string
                }
            }
        };
        LocalBroadcastManager bManager= LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);
        Intent intent = new Intent(this, NotificationIDService.class);
        startService(intent);
        ///until here

        //com.google.firebase.FirebaseApp.initializeApp(this);
        //new MainActivity.SendNotficationToken().execute("");
    }

    // when user click on show ride
    @Override
    public void onAvailableRideAdapterFragmentInteraction(int rideId) {
        Bundle bundle=new Bundle();
        bundle.putInt("RIDEID",rideId);
        bundle.putBoolean("SHOWSTATIONS",true);
        travelFragment.setArguments(bundle);
        setFragment(TravelFragmentName);
    }

    // currently not in use
    class SendNotficationToken extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";

            try {
                Map<String,Object> parameters = new HashMap<String, Object>();
                try {
                    parameters.put("token", FirebaseInstanceId.getInstance().getToken());
                }
                catch (Exception e)
                {
                    int x =5;
                }
                parameters.put("device","android");
                toReturn = POST(Const.NOTIFICATION_TOKEN_URI.toString(),parameters);
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK")==0) {
                    //listDsManager.updateAvailableRides(toReturn);
                    publishProgress("");
                    toReturn="";
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

            if(result.equals("")) {
                //every thing is okay
                //mListener.onFragmentInteraction("");
            }
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //user[0]=Phone user[1]=User Name
            //check if have any error
            if(values[0].length()>1) {
                //showAlert(,values[0]);
            }
            else {

                //mCallBack.OnLoginFragmentInteractionListener(1);
            }

        }
    }

    //when back button pressed
    @Override
    public void onBackPressed() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //maybe not so good idea
            //super.onBackPressed();
            Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content_main);
            if (currentFragment instanceof Travel) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    exitApp();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this,"לחץ חזור שוב כדי לצאת", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
            if (currentFragment instanceof Settings) {
                setFragment("myRegion");
            }
            if (currentFragment instanceof Auth) {
                exitApp();
            }
            if (currentFragment instanceof Login) {
                exitApp();
            }
            else
            {
                setFragment(TravelFragmentName);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        setFragment(convertIdToName(id));
        return super.onOptionsItemSelected(item);
    }

    // click on navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        setFragment(convertIdToName(id));
        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //not implemented
    @Override
    public void onFragmentInteraction(Uri uri) {
        //throw new Exception();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // change fragment
    private void setFragment(String fragmentName)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        switch (fragmentName)
        {
            case TravelFragmentName:
                travelFragment = newInstance();
                fragmentTransaction.replace(R.id.content_main,travelFragment);
                navigationView.setCheckedItem(R.id.Travel);
                drawer.closeDrawers();
                break;
            case myRideFragmentName:
                travelFragment = newInstance();
                travelFragment.setArguments(null);
                myRideFragment = new MyRide();
                fragmentTransaction.replace(R.id.content_main,myRideFragment);
                navigationView.setCheckedItem(R.id.myRide);
                drawer.closeDrawers();
                break;
            case availableRideFragmentName:
                travelFragment = newInstance();
                travelFragment.setArguments(null);
                availableRideFragment=new AvailableRide();
                fragmentTransaction.replace(R.id.content_main,availableRideFragment);
                navigationView.setCheckedItem(R.id.availableRide);
                drawer.closeDrawers();
                break;
            case historicRideFragmentName:
                travelFragment = newInstance();
                travelFragment.setArguments(null);
                historicalRideFragment=new HistoricalRide();
                fragmentTransaction.replace(R.id.content_main,historicalRideFragment);
                navigationView.setCheckedItem(R.id.historicRide);
                drawer.closeDrawers();
                break;
            case myRegionFragmentName:
                travelFragment = newInstance();
                travelFragment.setArguments(null);
                myRegionFragment = new MyRegion();
                fragmentTransaction.replace(R.id.content_main,myRegionFragment);
                navigationView.setCheckedItem(R.id.myRegion);
                drawer.closeDrawers();
                break;
            case settingFragmentName:
                travelFragment = newInstance();
                travelFragment.setArguments(null);;
                settingsFragment = Settings.newInstance();
                fragmentTransaction.replace(R.id.content_main,settingsFragment);

                //navigationView.setCheckedItem(R.id.settings);
                break;
            case "exit":
                exitApp();
                break;
            case personalInfoFragmentName:
                travelFragment = newInstance();
                travelFragment.setArguments(null);
                personalInfoFragment = new PersonalInfo();
                fragmentTransaction.replace(R.id.content_main,personalInfoFragment);
                //navigationView.setCheckedItem(R.id.);
                drawer.closeDrawers();
                break;
            default:
                travelFragment = newInstance();
                fragmentTransaction.replace(R.id.content_main,travelFragment);
                navigationView.setCheckedItem(R.id.Travel);
                drawer.closeDrawers();
                break;
        }
        fragmentTransaction.commit();

    }

    private String convertIdToName(int id)
    {
        switch (id)
        {
            case R.id.Travel: return TravelFragmentName;
            case R.id.myRide: return myRideFragmentName;
            case R.id.availableRide: return availableRideFragmentName;
            case R.id.historicRide: return historicRideFragmentName;
            case R.id.myRegion: return myRegionFragmentName;
            case R.id.personalInfo: return personalInfoFragmentName;
            case R.id.exit: return "exit";
        }
        return myRideFragmentName;
    }

    private void exitApp()
    {
        this.finish();
    }


    @Override
    public void onFragmentInteraction(String str) {
    }

    // when user click on show ride
    @Override
    public void onMyRideAdapterFragmentInteraction(int rideId) {
        Bundle bundle=new Bundle();
        bundle.putInt("RIDEID",rideId);
        bundle.putBoolean("SHOWSTATIONS",true);
        travelFragment.setArguments(bundle);
        setFragment(TravelFragmentName);
    }

    @Override
    public void onRegionFragmentInteraction(int sign) {
        settingsFragment = Settings.newInstance();
        settingsFragment.setArguments(null);
        setFragment(settingFragmentName);
    }

    @Override
    public void onSettingsFragmentInteraction(int sign) {
        setFragment(myRegionFragmentName);
    }

    @Override
    public void OnRegionAdapterInteractionListener(int regionId) {
        if(regionId == -2) //mean we delete object
        {

        }
        settingsFragment =Settings.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("REGIONID",regionId);
        settingsFragment.setArguments(bundle);
        setFragment(settingFragmentName);
    }
}