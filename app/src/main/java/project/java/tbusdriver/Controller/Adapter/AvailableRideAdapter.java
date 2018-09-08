package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import project.java.tbusdriver.Controller.Activitys.MainActivity;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.Const.AvailableRidesListName;
import static project.java.tbusdriver.Controller.Travel.TAG;
import static project.java.tbusdriver.Database.ListDsManager.convertRideIdToIndex;
import static project.java.tbusdriver.usefulFunctions.POST;
import static project.java.tbusdriver.usefulFunctions.showAlert;

/**
 * Created by אור איטח on 22/08/2017.
 */

public class AvailableRideAdapter extends ArrayAdapter<Ride> implements View.OnClickListener {
    ArrayList<Ride> content;
    ArrayList<Ride> agencyList;
    Context context;
    ListDsManager listDsManager;
    View listView;
    AvailableRideAdapter instance;
    OnFragmentInteractionListener mCallBack;


    public AvailableRideAdapter(Context c, int textViewResourceId, ArrayList<Ride> content) {
        super(c, textViewResourceId, content);
        context = c;
        this.content=new ArrayList<Ride>();
        this.content.addAll(content);
        this.agencyList=new ArrayList<Ride>();
        this.agencyList.addAll(content);
        listDsManager=(ListDsManager) new Factory(c).getInstance();
        instance=this;
        mCallBack = (OnFragmentInteractionListener) context;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Ride getItem(int position) {
        return content.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //if (convertView == null) {

            //listView = new View(context);

            // get layout from resources
            listView=inflater.inflate(R.layout.item_available_ride,null);

            // set image based on selected text
            Button btn = (Button) listView.findViewById(R.id.button_item_ride);
            Button showRide =(Button) listView.findViewById(R.id.showRide);
            TextView TVID = (TextView) listView.findViewById(R.id.name);
            String id=String.valueOf(content.get(position).getRideId());
            TVID.setText(id);
            TextView TVTravelTime = (TextView) listView.findViewById(R.id.travelTime);
            String travelTime= String.valueOf(content.get(position).getTravelTime());
            TVTravelTime.setText(travelTime);
            Ride currentRide= ListDsManager.getAvailableRides().get(convertRideIdToIndex(AvailableRidesListName, Integer.valueOf(id)));
            if(currentRide.getRoute() != null)
            {
                int amountOfStation = currentRide.getRoute().getLocations().size();
                if(amountOfStation>1)
                {
                    TextView firstStation = (TextView) listView.findViewById(R.id.firstStation);
                    String firstStationAddress = currentRide.getRoute().getLocations().get(0).getDestinationAddress(); //convertLocationToAddress((currentRide.getRoute().getLocations().get(0).getMyLocation()));
                    firstStation.setText(firstStationAddress);
                    TextView lastStation = (TextView) listView.findViewById(R.id.lastStation);

                    //lastStation.setText(String.valueOf(currentRide.getRoute().getLocations().get(amountOfStation-1).getMyLocation().getLongitude())
                    //        +"   "+ String.valueOf(currentRide.getRoute().getLocations().get(amountOfStation-1).getMyLocation().getLatitude()));
                    String lastStationAddress =currentRide.getRoute().getLocations().get(currentRide.getRoute().getLocations().size()-1).getDestinationAddress();//convertLocationToAddress((currentRide.getRoute().getLocations().get(amountOfStation-1).getMyLocation()));
                    lastStation.setText(lastStationAddress);
                }
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rideID=String.valueOf(content.get(position).getRideId());
                    String [] user=new String[1];
                    user[0]=rideID;
                    new AvailableRideAdapter.UsersTask().execute(user);
                }
            });
            showRide.setOnClickListener(this);
        //} else {
        //    listView = (View) convertView;
        //}
        return listView;
    }

    @Override
    public void onClick(View view) {
        TextView rideID=(TextView)listView.findViewById(R.id.name);
        switch(view.getId()) {
            case R.id.button_item_ride:
                //claim(view);
                //TextView rideID=(TextView)listView.findViewById(R.id.name);
                //String [] user=new String[1];
                //user[0]=rideID.getText().toString();
                //new AvailableRideAdapter.UsersTask().execute(user);
                break;
            case R.id.showRide:
                mCallBack.onAvailableRideAdapterFragmentInteraction(Integer.parseInt(rideID.getText().toString()));
                break;
            default:
                break;
        }
    }

    public void claim(View view)
    {
        String rideID=((TextView)listView.findViewById(R.id.name)).getText().toString();
        Ride temp=listDsManager.getAvailableRides().get(convertRideIdToIndex("AvailableRides",Integer.parseInt(rideID)));
        listDsManager.getMyRide().add(temp);
        listDsManager.getAvailableRides().remove(convertRideIdToIndex("AvailableRides",Integer.parseInt(rideID)));
        //new AvailableRideAdapter.UsersTask().execute(rideID);

    }

    private class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]= groupID    user[1]=User Name
            String toReturn = "";
            Map<String,Object> parameters = new HashMap<String, Object>();
            try {
                toReturn= POST("http://52.16.221.155/api/taxi/claim/"+params[0],parameters);
                //toReturn = POST(Const.CLAIM_A_RIDE_URI+params[0],parameters);
                String httpResult = new JSONObject(toReturn).getString("status");
                if (httpResult.compareTo("OK")==0) {
                    /// maybe not need
                    //// TODO: 25/08/2017 add the ride to my ride
                    Ride temp=listDsManager.getAvailableRides().get(
                            convertRideIdToIndex("AvailableRides",Integer.parseInt(params[0])));
                    listDsManager.getMyRide().add(temp);
                    listDsManager.getAvailableRides().remove(convertRideIdToIndex("AvailableRides",Integer.parseInt(params[0])));
                    //new AvailableRideAdapter.UsersTask().execute(rideID);
                    //content = listDsManager.getAvailableRides();
                    //instance.notifyDataSetChanged();
                    publishProgress(params[0]);
                    toReturn="";
                } else {
                    publishProgress("something get wrong\n" + toReturn);
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
                content = listDsManager.getAvailableRides();
                instance.notifyDataSetChanged();
                showAlert(context, "נסיעה נלקחה בהצלחה");
            }
            else
            {
                showAlert(context, "ישנה בעיה, אנא פנה אלינו בהקדם");
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
            try {
                //int rideID= Integer.parseInt(values[0]);
                //Ride temp=listDsManager.getAvailableRides().get(listDsManager.convertRideIdToIndex("AvailableRides",rideID));
                //listDsManager.getMyRide().add(temp);
                //listDsManager.getAvailableRides().remove(listDsManager.convertRideIdToIndex("AvailableRides",rideID));
            }
            catch (Exception e)
            {
                showAlert(context,values[0]);
            }
            //if()
            //
            //else {
            //    //mCallBack.OnLoginFragmentInteractionListener(1);
            //}
        }
    }

    private String convertLocationToAddress(Location location) {
        String addressText = location.getLatitude()+" "+location.getLongitude();

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );
        } catch (IOException ioException) {
            // Network or other I/O issues
        } catch (IllegalArgumentException illegalArgumentException) {
            // Invalid long / lat
        }

        // No address was found
        if (addresses == null || addresses.size() == 0) {

        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines, join them, and return to thread
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            addressText =
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments);
        }

        return addressText;
    }

    public interface OnFragmentInteractionListener {
        //the fragmentReturn mean if the login work 1 for good 0 for bad
        void onAvailableRideAdapterFragmentInteraction(int rideId);
    }
}
