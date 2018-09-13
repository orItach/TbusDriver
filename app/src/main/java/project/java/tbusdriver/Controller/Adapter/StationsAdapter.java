package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import junit.framework.Test;

import java.util.ArrayList;

import javax.sql.StatementEvent;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Entities.MyLocation;
import project.java.tbusdriver.R;

/**
 * Created by אור איטח on 19/03/2018.
 */
//MyLocation
public class StationsAdapter extends ArrayAdapter<MyLocation> {
    ArrayList<MyLocation> stationList;
    Context context;
    View listView;
    //TODO check if we need the instance
    StationsAdapter instance;

    public StationsAdapter(Context c, int textViewResourceId, ArrayList<MyLocation> stationList) {
        super(c, textViewResourceId, stationList);
        context = c;
        this.stationList = new ArrayList<MyLocation>();
        this.stationList.addAll(stationList);
        //this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public MyLocation getItem(int position) {
        return stationList.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        listView = inflater.inflate(R.layout.item_station, null);

        // set image based on selected text
        //TVID is text view for ride id R.id.name is used for ride id
        TextView TVID = (TextView) listView.findViewById(R.id.name);
        String id = String.valueOf(stationList.get(position).getLocationId());
        TVID.setText(id);
        // travel time is used as distance
        TextView TVTravelTime = (TextView) listView.findViewById(R.id.travelTime);
        String travelTime = String.valueOf(stationList.get(position).getDistance());
        TVTravelTime.setText(travelTime);

        TextView isPickup = (TextView) listView.findViewById(R.id.isPickup);
        if (stationList.get(position).isPickUp()) {
            isPickup.setText("עליה ");
        } else {
            isPickup.setText("ירידה ");
        }
        TextView TVPassenger = (TextView) listView.findViewById(R.id.passenger);
        String passenger = stationList.get(position).getPassenger();
        if (stationList.get(position).getUsername() == null && stationList.get(position).getPhone() == null) {
            TVPassenger.setText(passenger);
        } else {
            TVPassenger.setText(stationList.get(position).getUsername() + " " + stationList.get(position).getPhone());
        }
        //Ride currentRide= ListDsManager.getHistoricRide().get(convertRideIdToIndex(HistoricRidesListName, Integer.valueOf(id)));
        return listView;
    }
}
