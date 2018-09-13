package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONObject;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Adapter.HistoricalRideAdapter;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.usefulFunctions.GET;
import static project.java.tbusdriver.usefulFunctions.showAlert;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoricalRide.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HistoricalRide extends Fragment {

    Activity myActivity;
    private OnFragmentInteractionListener mListener;
    View myView;
    ListDsManager listDsManager;
    HistoricalRideAdapter historicalRideAdapter;
    ListView listView;

    public HistoricalRide() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        myActivity = getActivity();
        listDsManager = (ListDsManager) new Factory(getActivity()).getInstance();
        fillHistoricalRideList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_historical_ride, container, false);

        // Inflate the layout for this fragment
        listView = (ListView) myView.findViewById(R.id.historicalRideList);


        return myView;
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

    public void onResume() {
        super.onResume();
        myActivity = getActivity();
        myActivity.setTitle("היסטוריית נסיעות");
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
        void onFragmentInteraction(String str);
    }


    private void fillHistoricalRideList() {
        new HistoricalRide.UsersTask().execute();
    }

    class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";

            try {
                toReturn = GET(Const.USER_HISTORIC_RIDE_URI.toString());
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK") == 0) {
                    listDsManager.updateHistoricRides(toReturn);
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
            historicalRideAdapter = new HistoricalRideAdapter(myActivity, R.layout.item_historical_ride, listDsManager.getHistoricRide());
            historicalRideAdapter.notifyDataSetChanged();
            listView.setAdapter(historicalRideAdapter);
            //listView.deferNotifyDataSetChanged();
        }
    }

}
