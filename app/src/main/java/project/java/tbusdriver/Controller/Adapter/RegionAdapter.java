package project.java.tbusdriver.Controller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.R;

/**
 * Created by אור איטח on 16/10/2017.
 */

public class RegionAdapter extends ArrayAdapter<Region> implements View.OnClickListener {
    ArrayList<Region> content;
    ArrayList<Region> agencyList;
    Context context;
    ListDsManager listDsManager;
    View listView;

    public RegionAdapter(Context c, int textViewResourceId, ArrayList<Region> content) {
        super(c, textViewResourceId, content);
        context = c;
        this.content=new ArrayList<Region>();
        this.content.addAll(content);
        this.agencyList=new ArrayList<Region>();
        this.agencyList.addAll(content);
        listDsManager=(ListDsManager) new Factory(c).getInstance();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Region getItem(int position) {
        return content.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            //listView = new View(context);

            // get layout from resources
            listView=inflater.inflate(R.layout.item_region,null);

            // set image based on selected text
            //Button btn = (Button) listView.findViewById(R.id.button_item_ride);
            TextView TVRegionName = (TextView) listView.findViewById(R.id.regionName);

            String regionName=String.valueOf(content.get(position).getRegionName());

            TVRegionName.setText(regionName);
            //btn.setOnClickListener(this);
        } else {
            listView = (View) convertView;
        }
        return listView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_item_ride:
                //claim(view);

                break;
            default:
                break;
        }
    }

}
