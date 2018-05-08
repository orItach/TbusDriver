package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Entities.MyLocation;
import project.java.tbusdriver.R;

/**
 * Created by אור איטח on 19/03/2018.
 */
//MyLocation
public class StationsAdapter extends ArrayAdapter<MyLocation> {
    ArrayList<MyLocation> content;
    ArrayList<MyLocation> agencyList;
    Context context;
    View listView;
    StationsAdapter instance;

    public StationsAdapter(Context c, int textViewResourceId, ArrayList<MyLocation> content) {
        super(c, textViewResourceId, content);
        context = c;
        this.content=new ArrayList<MyLocation>();
        this.content.addAll(content);
        this.agencyList=new ArrayList<MyLocation>();
        this.agencyList.addAll(content);
        instance=this;
        //this.notifyDataSetChanged();
        int i =0;
        for(MyLocation station:content )
        {
            getItem(i);
            i++;
        }
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public MyLocation getItem(int position) {
        return content.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        listView=inflater.inflate(R.layout.item_station,null);

        // set image based on selected text
        TextView TVID = (TextView) listView.findViewById(R.id.name);
        String id=String.valueOf(content.get(position).getLocationId());
        TVID.setText(id);
        TextView TVTravelTime = (TextView) listView.findViewById(R.id.travelTime);
        String travelTime= String.valueOf(content.get(position).getDistance());
        TVTravelTime.setText(travelTime);
        //Ride currentRide= ListDsManager.getHistoricRide().get(convertRideIdToIndex(HistoricRidesListName, Integer.valueOf(id)));


        return listView;
    }
}
