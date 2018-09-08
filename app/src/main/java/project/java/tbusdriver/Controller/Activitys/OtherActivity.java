package project.java.tbusdriver.Controller.Activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import project.java.tbusdriver.Controller.Fragments.HistoricalRide;
import project.java.tbusdriver.Controller.Fragments.Settings;
import project.java.tbusdriver.R;

////////////////////////////////////////////////////////////////////
/////////////////////////// NOT IN USE ////////////////////////////
////////////////////////////////////////////////////////////////////

public class OtherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Settings.OnFragmentInteractionListener,
        HistoricalRide.OnFragmentInteractionListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;


    FragmentManager fragmentManager;
    Settings settingsFragment;
    HistoricalRide historicalRideFragment;
    FragmentTransaction fragmentTransaction;
    boolean haveCommit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getFragmentManager();
        String fragmentIndex=getIntent().getStringExtra("index");
        setFragment(fragmentIndex);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent=new Intent(this,MainActivity.class);
        if (id == R.id.Travel) {
            ///0 for Travel
            intent.putExtra("index",0);
            startActivity(intent);
        }
        if (id == R.id.myRide) {
            ///1 for my Ride
            intent.putExtra("index",1);
            startActivity(intent);
        }
        if (id == R.id.availableRide) {
            ///2 for available Ride
            intent.putExtra("index",2);
            startActivity(intent);
        }
        if (id == R.id.historicRide) {
            setFragment("historicalRide");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setFragment(String fragmentIndex)
    {
        switch (fragmentIndex)
        {
            //historic
            case "historicalRide":
                historicalRideFragment= new HistoricalRide();
                fragmentTransaction = fragmentManager.beginTransaction();
                if(!haveCommit)
                {
                    //fragmentTransaction.add(R.id.otherContainer,historicalRideFragment);
                    haveCommit=true;
                }
                else
                {
                    //fragmentTransaction.replace(R.id.otherContainer,historicalRideFragment);
                }
                fragmentTransaction.commit();
                break;

            //settings
            case "settings":
                settingsFragment= new Settings();
                fragmentTransaction = fragmentManager.beginTransaction();
                if(!haveCommit)
                {
                    //fragmentTransaction.add(R.id.otherContainer,settingsFragment);
                    haveCommit=true;
                }
                else
                {
                    //fragmentTransaction.replace(R.id.otherContainer,settingsFragment);
                }
                fragmentTransaction.commit();
                break;
            ///for further
            case "further":
                //settingsFragment= new Settings();
                //fragmentTransaction = fragmentManager.beginTransaction();
                //if(!haveCommit)
                //{
                //    fragmentTransaction.add(R.id.otherContainer,settingsFragment);
                //    haveCommit=true;
                //}
                //else
                //{
                //    fragmentTransaction.replace(R.id.otherContainer,settingsFragment);
                //}
                //fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onSettingsFragmentInteraction(int sign) {

    }

    @Override
    public void onFragmentInteraction(String str) {

    }
}
