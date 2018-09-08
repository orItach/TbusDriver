package project.java.tbusdriver.Controller.Activitys;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.SupportMapFragment;

import project.java.tbusdriver.Controller.Fragments.AvailableRide;
import project.java.tbusdriver.Controller.Fragments.MyRide;
import project.java.tbusdriver.Controller.Travel;
import project.java.tbusdriver.R;

////////////////////////////////////////////////////////////////////
/////////////////////////// NOT IN USE ////////////////////////////
////////////////////////////////////////////////////////////////////
public class Main4Activity extends AppCompatActivity implements
        MyRide.OnFragmentInteractionListener,
        AvailableRide.OnFragmentInteractionListener,
        Travel.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    SupportMapFragment mapFragment;
    private Toolbar myToolbar;
    String[] mMenuChoices;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.addTab(tabLayout.newTab().setText("Map"));
        //tabLayout.addTab(tabLayout.newTab().setText("My Rides"));
        //tabLayout.addTab(tabLayout.newTab().setText("Active Rides"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //setContentView(R.layout.activity_main5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        //final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        //final PagerAdapter adapter = new PagerAdapter
        //        (getSupportFragmentManager(), tabLayout.getTabCount());
        //viewPager.setAdapter(adapter);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        //    @Override
        //    public void onTabSelected(TabLayout.Tab tab) {
        //        viewPager.setCurrentItem(tab.getPosition());
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
    }
//    @Override
//    public boolean onPrepareOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }

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
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFragmentInteraction(String str) {

    }

}
