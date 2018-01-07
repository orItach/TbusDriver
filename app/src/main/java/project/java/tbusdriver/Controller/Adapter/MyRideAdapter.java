package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.usefulFunctions.POST;
import static project.java.tbusdriver.usefulFunctions.showAlert;

/**
 * Created by אור איטח on 28/08/2017.
 */

public class MyRideAdapter extends ArrayAdapter<Ride> implements View.OnClickListener {
    ArrayList<Ride> content;
    ArrayList<Ride> agencyList;
    Context context;
    ListDsManager listDsManager;
    View listView;
    MyRideAdapter instance;
    OnFragmentInteractionListener mCallBack;


    public MyRideAdapter(Context c, int textViewResourceId, ArrayList<Ride> content) {
        super(c, textViewResourceId, content);
        context = c;
        this.content=new ArrayList<Ride>();
        this.content.addAll(content);
        this.agencyList=new ArrayList<Ride>();
        this.agencyList.addAll(content);
        listDsManager=(ListDsManager) new Factory(c).getInstance();
        mCallBack = (OnFragmentInteractionListener) context;
        instance=this;

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
            listView=inflater.inflate(R.layout.item_my_ride,null);

            // set image based on selected text
            Button unclaim = (Button) listView.findViewById(R.id.unclaim);
            Button startRide = (Button) listView.findViewById(R.id.startRide);

            TextView TVID = (TextView) listView.findViewById(R.id.name);
            String id=String.valueOf(content.get(position).getRideId());
            TVID.setText(id);

            TextView TVTravelTime = (TextView) listView.findViewById(R.id.travelTime);
            String travelTime= String.valueOf(content.get(position).getTravelTime());
            TVTravelTime.setText(travelTime);
            unclaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rideID=String.valueOf(content.get(position).getRideId());
                    String [] user=new String[1];
                    user[0]=rideID;
                    new MyRideAdapter.UsersTask().execute(user);
                }
            });
            startRide.setOnClickListener(this);
        //} else {
        //    listView = (View) convertView;
        //}
        return listView;
    }

    @Override
    public void onClick(View view) {
        TextView rideID=(TextView)listView.findViewById(R.id.name);
        switch(view.getId()) {
            case R.id.unclaim:
                //claim(view);
                String [] user=new String[1];
                user[0]=rideID.getText().toString();
                new MyRideAdapter.UsersTask().execute(user);
                break;
            case R.id.startRide:
                mCallBack.onMyRideAdapterFragmentInteraction(Integer.parseInt(rideID.getText().toString()));
                break;
            default:
                break;
        }
    }


    class UsersTask extends AsyncTask<String, String, String> {
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
                    //// TODO: 25/08/2017 add the ride to my ride
                    Ride temp=listDsManager.getMyRide().get(listDsManager.convertRideIdToIndex("MyRide",Integer.parseInt(params[0])));
                    listDsManager.getAvailableRides().add(temp);
                    listDsManager.getMyRide().remove(listDsManager.convertRideIdToIndex("MyRide",Integer.parseInt(params[0])));
                    content = listDsManager.getMyRide();
                    publishProgress("");
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
                instance.notifyDataSetChanged();
                //every thing is okay
                //mListener.onFragmentInteraction("");
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
            if(values[0].length()>1)
                showAlert(context,values[0]);
            else {
                instance.notifyDataSetChanged();
                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        //the fragmentReturn mean if the login work 1 for good 0 for bad
        void onMyRideAdapterFragmentInteraction(int rideId);
    }
}
