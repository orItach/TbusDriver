package project.java.tbusdriver.Controller.Activitys;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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

import com.google.android.gms.maps.SupportMapFragment;

import project.java.tbusdriver.Controller.Adapter.MyRideAdapter;
import project.java.tbusdriver.Controller.Adapter.RegionAdapter;
import project.java.tbusdriver.Controller.Fragments.AvailableRide;
import project.java.tbusdriver.Controller.Fragments.HistoricalRide;
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

import static project.java.tbusdriver.Const.personalInfoFragmentName;
import static project.java.tbusdriver.Const.TravelFragmentName;
import static project.java.tbusdriver.Const.availableRideFragmentName;
import static project.java.tbusdriver.Const.historicRideFragmentName;
import static project.java.tbusdriver.Const.myRegionFragmentName;
import static project.java.tbusdriver.Const.myRideFragmentName;
import static project.java.tbusdriver.Const.settingFragmentName;
import static project.java.tbusdriver.Controller.Travel.newInstance;


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
        PersonalInfo.OnFragmentInteractionListener{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    RWSetting rwSettings = null;
    ImageView menuImage;
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
        //if(rwSettings.getStringSetting("Token")=="")
        //{
        //    //start login
        //    Intent intent =new Intent(this,LoginAuth.class);
        //    startActivity(intent);
        //}
        //else
        //{
        //    usefulFunctions.Token=rwSettings.getStringSetting("Token");
        //}
        setContentView(R.layout.activity_main);

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.addTab(tabLayout.newTab().setText("Map"));
        //tabLayout.addTab(tabLayout.newTab().setText("My Rides"));
        //tabLayout.addTab(tabLayout.newTab().setText("Active Rides"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


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
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View hView =  navigationView.getHeaderView(0);
        //TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
        //nav_user.setText(user);
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
        // need to be deleted after sync with the server
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
        //final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        //final PagerAdapter adapter = new PagerAdapter
        //        (getSupportFragmentManager(), tabLayout.getTabCount());
        //viewPager.setAdapter(adapter);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        //    @Override
        //    public void onTabSelected(TabLayout.Tab tab) {
        //        viewPager.setCurrentItem(tab.getPosition());
//
        //    }
//
        //    @Override
        //    public void onTabUnselected(TabLayout.Tab tab) {
//
        //    }
//
        //    @Override
        //    public void onTabReselected(TabLayout.Tab tab) {
//
        //    }
        //});

        //int tabIndex=getIntent().getIntExtra("index",0);

        //viewPager.setCurrentItem(tabIndex);

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
    }

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
                Toast.makeText(this,"לחץ חזור שוב כדי לצאת", Toast.LENGTH_SHORT);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                myRideFragment= new MyRide();
                fragmentTransaction.replace(R.id.content_main,myRideFragment);
                navigationView.setCheckedItem(R.id.myRide);
                drawer.closeDrawers();
                break;
            case availableRideFragmentName:
                availableRideFragment=new AvailableRide();
                fragmentTransaction.replace(R.id.content_main,availableRideFragment);
                navigationView.setCheckedItem(R.id.availableRide);
                drawer.closeDrawers();
                break;
            case historicRideFragmentName:
                historicalRideFragment=new HistoricalRide();
                fragmentTransaction.replace(R.id.content_main,historicalRideFragment);
                navigationView.setCheckedItem(R.id.historicRide);
                drawer.closeDrawers();
                break;
            case myRegionFragmentName:
                myRegionFragment = new MyRegion();
                fragmentTransaction.replace(R.id.content_main,myRegionFragment);
                navigationView.setCheckedItem(R.id.myRegion);
                drawer.closeDrawers();
                break;
            case settingFragmentName:
                settingsFragment = Settings.newInstance();
                fragmentTransaction.replace(R.id.content_main,settingsFragment);
                //navigationView.setCheckedItem(R.id.settings);
                break;
            case "exit":
                exitApp();
                break;
            case personalInfoFragmentName:
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

    @Override
    public void onMyRideAdapterFragmentInteraction(int rideId) {
        Bundle bundle=new Bundle();
        bundle.putInt("RIDEID",rideId);
        travelFragment.setArguments(bundle);
        setFragment("travel");
    }

    @Override
    public void onRegionFragmentInteraction(int sign) {
        settingsFragment =Settings.newInstance();
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