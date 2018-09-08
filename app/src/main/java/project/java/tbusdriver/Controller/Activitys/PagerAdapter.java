package project.java.tbusdriver.Controller.Activitys;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import project.java.tbusdriver.Controller.Fragments.AvailableRide;
import project.java.tbusdriver.Controller.Fragments.MyRide;
import project.java.tbusdriver.Controller.Travel;

import static project.java.tbusdriver.Controller.Travel.newInstance;

////////////////////////////////////////////////////////////////////
/////////////////////////// NOT IN USE ////////////////////////////
////////////////////////////////////////////////////////////////////

public class PagerAdapter extends FragmentStatePagerAdapter {


    private OnFragmentInteractionListener mListener;

    int mNumOfTabs=3;

    public static Travel travelFragment;
    MyRide myRideFragment;
    AvailableRide myAvailableRideFragment;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        travelFragment = newInstance();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return travelFragment;
            case 1:
                myRideFragment = new MyRide();
                return myRideFragment;
            case 2:
                myAvailableRideFragment = new AvailableRide();
                return myAvailableRideFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
