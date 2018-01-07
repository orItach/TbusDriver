package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.Database.ListDsManager.convertRideIdToIndex;

/**
 * Created by אור איטח on 16/10/2017.
 */

public class RegionAdapter extends ArrayAdapter<Region>  {

    private ArrayList<Region> content;
    //private ArrayList<Region> agencyList;
    private Context context;
    private ListDsManager listDsManager;
    View listView;
    private RegionAdapter instance;

    private OnRegionAdapterInteractionListener mListener;
    
    public RegionAdapter(Context c, int textViewResourceId, ArrayList<Region> content) {
        super(c, textViewResourceId, content);
        context = c;
        this.content=new ArrayList<Region>();
        this.content.addAll(content);
        listDsManager= new Factory(c).getInstance();
        mListener  =(OnRegionAdapterInteractionListener) context;
        instance=this;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Region getItem(int position) {
        return content.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //if (convertView == null) {

            //listView = new View(context);

            // get layout from resources
            listView=inflater.inflate(R.layout.item_region,null);


            Button update =(Button) listView.findViewById(R.id.editRegion);
            Button delete = (Button) listView.findViewById(R.id.deleteRegion);
            TextView TVRegionName = (TextView) listView.findViewById(R.id.regionName);
            TextView TVRegionId=(TextView) listView.findViewById(R.id.regionId);
            String regionName=String.valueOf(content.get(position).getRegionName());
            String regionId=String.valueOf(content.get(position).getRegionID());
            TVRegionName.setText(regionName);
            TVRegionId.setText(regionId);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String regionId =String.valueOf(content.get(position).getRegionID());
                    if(mListener != null)
                    {
                        mListener.OnRegionAdapterInteractionListener(Integer.parseInt(regionId));
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String regionId = String.valueOf(content.get(position).getRegionID());
                    int regionIndex = convertRideIdToIndex("Regions",Integer.parseInt(regionId));
                    //Region regionToDelete= (regionId);
                    ListDsManager.getRegions().remove(regionIndex);
                    content = ListDsManager.getRegions();
                    instance.notifyDataSetChanged();

                }
            });
        //}
        //else {
        //    listView = (View) convertView;
        //}
        return listView;
    }

    //@Override
    //@Deprecated
    //public void onClick(View view) {
    //    switch(view.getId()) {
    //        case R.id.deleteRegion:
    //            deleteRegion(view);
    //            break;
    //        case R.id.editRegion:
    //            updateRegion(view);
    //        default:
    //            break;
    //    }
    //}
    //@Deprecated
    //private void deleteRegion(View view)
    //{
    //    TextView TVRegionId= (TextView) listView.findViewById(R.id.regionId);
    //    String regionId = TVRegionId.getText().toString();
    //    int regionIndex = listDsManager.convertRideIdToIndex("Regions",Integer.parseInt(regionId));
    //    //Region regionToDelete= (regionId);
    //    ListDsManager.getRegions().remove(regionIndex);
    //}
    //@Deprecated
    //private void updateRegion(View view)
    //{
    //    TextView TVRegionId= (TextView) listView.findViewById(R.id.regionId);
    //    String regionId = TVRegionId.getText().toString();
    //    if(mListener != null)
    //    {
    //        mListener.OnRegionAdapterInteractionListener(Integer.parseInt(regionId));
    //    }
    //}

    public interface OnRegionAdapterInteractionListener {
        // TODO: Update argument type and name
        void OnRegionAdapterInteractionListener(int regionId);
    }
}
