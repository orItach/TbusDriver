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
import static project.java.tbusdriver.usefulFunctions.POST;
import static project.java.tbusdriver.usefulFunctions.showAlert;

/**
 * Created by אור איטח on 07/01/2018.
 */

public class HistoricalRideAdapter extends ArrayAdapter<Ride> {
    ArrayList<Ride> content;
    ArrayList<Ride> agencyList;
    Context context;
    ListDsManager listDsManager;
    View listView;
    HistoricalRideAdapter instance;

    public HistoricalRideAdapter(Context c, int textViewResourceId, ArrayList<Ride> content) {
        super(c, textViewResourceId, content);
        context = c;
        this.content=new ArrayList<Ride>();
        this.content.addAll(content);
        this.agencyList=new ArrayList<Ride>();
        this.agencyList.addAll(content);
        listDsManager=(ListDsManager) new Factory(c).getInstance();
        instance=this;
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
        listView=inflater.inflate(R.layout.item_historical_ride,null);

        // set image based on selected text
        TextView TVID = (TextView) listView.findViewById(R.id.name);
        String id=String.valueOf(content.get(position).getRideId());
        TVID.setText(id);
        TextView TVTravelTime = (TextView) listView.findViewById(R.id.travelTime);
        String travelTime= String.valueOf(content.get(position).getTravelTime());
        TVTravelTime.setText(travelTime);
        Ride currentRide= ListDsManager.getHistoricRide().get(convertRideIdToIndex(HistoricRidesListName, Integer.valueOf(id)));
        if(currentRide.getRoute() != null)
        {
            int amountOfStation = currentRide.getRoute().getLocations().size();
            if(amountOfStation>1)
            {
                TextView firstStation = (TextView) listView.findViewById(R.id.firstStation);
                firstStation.setText(String.valueOf(currentRide.getRoute().getLocations().get(0).getMyLocation().getLongitude())
                        +"   "+ String.valueOf(currentRide.getRoute().getLocations().get(0).getMyLocation().getLatitude()));
                TextView lastStation = (TextView) listView.findViewById(R.id.lastStation);

                lastStation.setText(String.valueOf(currentRide.getRoute().getLocations().get(amountOfStation-1).getMyLocation().getLongitude())
                        +"   "+ String.valueOf(currentRide.getRoute().getLocations().get(amountOfStation-1).getMyLocation().getLatitude()));
            }
        }

        //Route route =content.get(position).getRoute();
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
