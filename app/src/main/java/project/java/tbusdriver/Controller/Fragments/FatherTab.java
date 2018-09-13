package project.java.tbusdriver.Controller.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import project.java.tbusdriver.R;

////////////////////////////////////////////////////////////////////
/////////////////////////// NOT IN USE ////////////////////////////
////////////////////////////////////////////////////////////////////
public class FatherTab extends Fragment {


    private OnFragmentInteractionListener mListener;

    public FatherTab() {
        // Required empty public constructor
    }


    //public static FatherTab newInstance(String param1, String param2) {
    //    FatherTab fragment = new FatherTab();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_father_tab, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    private void setupTabs(View v) {
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        //if (numOfYears > 3)
        //    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //for (int i = 0; i < ; i++)
        tabLayout.addTab(tabLayout.newTab(), false);
        tabLayout.addTab(tabLayout.newTab(), false);
        tabLayout.addTab(tabLayout.newTab(), false);


        //final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        //tabLayout.setupWithViewPager(viewPager, true);
        //SamplePageAdapter mAdapter = new SamplePageAdapter(getFragmentManager(), (HashMap<Integer, GradesList>) gradesSorted.clone());
        //mAdapter.notifyDataSetChanged();
        //viewPager.setAdapter(mAdapter);
        ////if(checkHebrew())
        //tabLayout.getTabAt(numOfYears - 1).select();
    }
}
