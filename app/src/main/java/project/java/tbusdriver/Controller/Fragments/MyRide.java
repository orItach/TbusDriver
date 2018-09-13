package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Adapter.MyRideAdapter;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;
import project.java.tbusdriver.Service.rideStartService;

import static project.java.tbusdriver.usefulFunctions.GET;
import static project.java.tbusdriver.usefulFunctions.showAlert;


public class MyRide extends Fragment {

    View myView;
    Activity myActivity;
    MyRideAdapter myRideAdapter;
    ListDsManager listDsManager;
    ListView listView;

    private OnFragmentInteractionListener mListener;

    public MyRide() {
        // Required empty public constructor
    }


    //public static MyRide newInstance(String param1, String param2) {
    //    MyRide fragment = new MyRide();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
        listDsManager = (ListDsManager) new Factory(getActivity()).getInstance();

        fillMyRide();
    }

    private void fillMyRide() {
        new MyRide.UsersTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().setTitle("My Ride");
        myView = inflater.inflate(R.layout.fragment_my_ride, container, false);
        listView = (ListView) myView.findViewById(R.id.myRideList);
        return myView;
    }

    class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";

            try {
                toReturn = GET(Const.USER_MY_RIDE_URI.toString());
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK") == 0) {
                    listDsManager.updateMyRides(toReturn);
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
            if (values[0].length() > 1)
                showAlert(myActivity, values[0]);
            else {
                //showAlert(myActivity,"נסיעה נלקחה בהצלחה");
                //mCallBack.OnLoginFragmentInteractionListener(1);
            }

            myRideAdapter = new MyRideAdapter(myActivity, R.layout.item_available_ride, listDsManager.getMyRide());
            listView.setAdapter(myRideAdapter);
            myRideAdapter.notifyDataSetChanged();
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            listView.invalidateViews();
            listView.scrollBy(0, 0);
            //Intent myRideStartServiceIntent = new Intent(getActivity(), rideStartService.class);
            //ArrayList<Ride> temp =listDsManager.getMyRide();

            //myRideStartServiceIntent.putExtra("myRide", temp);
            //try {
            //    getContext().startService(myRideStartServiceIntent);
            //}
            //catch (Exception e)
            {
                int x = 5;
            }
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("נסיעות שלי");
    }
}
