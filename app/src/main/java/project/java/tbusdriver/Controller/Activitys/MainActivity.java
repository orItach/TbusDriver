package project.java.tbusdriver.Controller.Activitys;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;

import project.java.tbusdriver.Controller.Adapter.MyRideAdapter;
import project.java.tbusdriver.Controller.Fragments.AvailableRide;
import project.java.tbusdriver.Controller.Fragments.HistoricalRide;
import project.java.tbusdriver.Controller.Fragments.MyRegion;
import project.java.tbusdriver.Controller.Fragments.MyRide;
import project.java.tbusdriver.Controller.Fragments.Settings;
import project.java.tbusdriver.Controller.Travel;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;

import static project.java.tbusdriver.Controller.Travel.newInstance;


public class MainActivity extends AppCompatActivity
        implements MyRide.OnFragmentInteractionListener,
        AvailableRide.OnFragmentInteractionListener,
        HistoricalRide.OnFragmentInteractionListener,
        Travel.OnFragmentInteractionListener,
        MyRideAdapter.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        Settings.OnFragmentInteractionListener,
        MyRegion.OnFragmentInteractionListener{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    RWSetting rwSettings = null;
    SupportMapFragment mapFragment;
    Travel travelFragment;
    MyRide myRideFragment;
    AvailableRide availableRideFragment;
    Settings settingsFragment;
    MyRegion myRegionFragment;
    HistoricalRide historicalRideFragment;


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

        //set Travel to be the first fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        travelFragment = newInstance();
        fragmentTransaction.add(R.id.content_main,travelFragment);
        fragmentTransaction.commit();



        //mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment=SupportMapFragment.newInstance();
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
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //maybe not so good idea
            //super.onBackPressed();
            setFragment("travel");
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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
            case "travel":
                travelFragment = newInstance();
                fragmentTransaction.replace(R.id.content_main,travelFragment);
                break;
            case "myRide":
                myRideFragment= new MyRide();
                fragmentTransaction.replace(R.id.content_main,myRideFragment);
                break;
            case "availableRide":
                availableRideFragment=new AvailableRide();
                fragmentTransaction.replace(R.id.content_main,availableRideFragment);
                break;
            case "historicRide":
                historicalRideFragment=new HistoricalRide();
                fragmentTransaction.replace(R.id.content_main,historicalRideFragment);
                break;
            case "myRegion":
                myRegionFragment = new MyRegion();
                fragmentTransaction.replace(R.id.content_main,myRegionFragment);
                break;
            case "setting":
                settingsFragment = new Settings();
                fragmentTransaction.replace(R.id.content_main,settingsFragment);
                break;
            case "exit":
                exitApp();
                break;
            default:
                travelFragment = newInstance();
                fragmentTransaction.replace(R.id.content_main,travelFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    private String convertIdToName(int id)
    {
        switch (id)
        {
            case R.id.Travel: return "travel";
            case R.id.myRide: return "myRide";
            case R.id.availableRide: return "availableRide";
            case R.id.historicRide: return "historicRide";
            case R.id.myRegion: return "myRegion";
            case R.id.exit: return "exit";

        }
        return "myRide";
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
        setFragment("setting");
    }
}