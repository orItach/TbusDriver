package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Ride;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.Const.HistoricRidesListName;
import static project.java.tbusdriver.Database.ListDsManager.convertRideIdToIndex;


/**
 * Created by אור איטח on 07/01/2018.
 */

public class HistoricalRideAdapter extends ArrayAdapter<Ride> {
    private ArrayList<Ride> rideList;
    private Context context;
    private ListDsManager listDsManager;
    private View listView;

    public HistoricalRideAdapter(Context c, int textViewResourceId, ArrayList<Ride> rideList) {
        super(c, textViewResourceId, rideList);
        context = c;
        this.rideList = new ArrayList<Ride>();
        this.rideList.addAll(rideList);
        listDsManager = (ListDsManager) new Factory(c).getInstance();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rideList.size();
    }

    @Override
    public Ride getItem(int position) {
        return rideList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // get layout from resources
        listView = inflater.inflate(R.layout.item_historical_ride, null);

        // set image based on selected text
        TextView TVID = (TextView) listView.findViewById(R.id.name);
        String id = String.valueOf(rideList.get(position).getRideId());
        TVID.setText(id);
        TextView TVTravelTime = (TextView) listView.findViewById(R.id.travelTime);
        String travelTime = String.valueOf(rideList.get(position).getTravelTime());
        TVTravelTime.setText(travelTime);
        Ride currentRide = ListDsManager.getHistoricRide().get(convertRideIdToIndex(HistoricRidesListName, Integer.valueOf(id)));
        if (currentRide.getRoute() != null) {
            int amountOfStation = currentRide.getRoute().getLocations().size();
            if (amountOfStation > 1) {
                TextView firstStation = (TextView) listView.findViewById(R.id.firstStation);
                firstStation.setText(String.valueOf(currentRide.getRoute().getLocations().get(0).getMyLocation().getLongitude())
                        + "   " + String.valueOf(currentRide.getRoute().getLocations().get(0).getMyLocation().getLatitude()));
                TextView lastStation = (TextView) listView.findViewById(R.id.lastStation);

                lastStation.setText(String.valueOf(currentRide.getRoute().getLocations().get(amountOfStation - 1).getMyLocation().getLongitude())
                        + "   " + String.valueOf(currentRide.getRoute().getLocations().get(amountOfStation - 1).getMyLocation().getLatitude()));
            }
        }

        //Route route =rideList.get(position).getRoute();
        //if(route != null) {
        //    TextView TVFirstStation = (TextView) listView.findViewById(R.id.firstStation);
        //    TVFirstStation.setText(route.getLocations().get(0).getLocationId());
        //    TextView TVLastStation = (TextView) listView.findViewById(R.id.lastStation);
        //    TVLastStation.setText(route.getLocations().get(route.getLocations().size() - 1).getLocationId());
        //}


        //} else {
        //    listView = (View) convertView;
        //}
        return listView;
    }
}
