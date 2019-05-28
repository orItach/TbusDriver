package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONObject;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Adapter.AvailableRideAdapter;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.usefulFunctions.GET;
import static project.java.tbusdriver.usefulFunctions.showAlert;


public class AvailableRide extends Fragment {

    private OnFragmentInteractionListener mListener;
    View myView;
    Activity myActivity;
    ListDsManager listDsManager;
    AvailableRideAdapter availableRideAdapter;
    ListView listView;

    public AvailableRide() {
        // Required empty public constructor
    }


    //public static AvailableRide newInstance(String param1, String param2) {
    //    AvailableRide fragment = new AvailableRide();
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
        fillAvailableRideList();

    }

    private void fillAvailableRideList() {
        new AvailableRide.UsersTask().execute();
    }

    class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";

            try {
                toReturn = GET(Const.PADDING_RIDES_URI.toString());
                //listDsManager.updateAvailableRides(toReturn);
                //publishProgress("");
                //toReturn="";
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK") == 0) {
                    listDsManager.updateAvailableRides(toReturn);
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
                mListener.onFragmentInteraction("");
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

                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
            availableRideAdapter = new AvailableRideAdapter(myActivity, R.layout.item_available_ride, listDsManager.getAvailableRides());
            availableRideAdapter.notifyDataSetChanged();
            listView.setAdapter(availableRideAdapter);
            //listView.deferNotifyDataSetChanged();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().setTitle("Available Ride");
        myView = inflater.inflate(R.layout.fragment_available_ride, container, false);

        // Inflate the layout for this fragment
        listView = (ListView) myView.findViewById(R.id.availableRideList);


        return myView;
    }

    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String str);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null)
                getActivity().setTitle("Available Ride");
        } else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myActivity = getActivity();
        myActivity.setTitle("נסיעות פנויות");
    }
}
