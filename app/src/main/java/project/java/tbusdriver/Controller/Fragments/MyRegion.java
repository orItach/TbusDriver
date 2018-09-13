package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import project.java.tbusdriver.Controller.Adapter.RegionAdapter;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.R;


public class MyRegion extends Fragment implements View.OnClickListener {

    View myView;
    Activity myActivity;
    RegionAdapter myRegionAdapter;
    ListDsManager listDsManager;
    ListView listView;
    FloatingActionButton addRegion;
    private OnFragmentInteractionListener mListener;

    public MyRegion() {
        // Required empty public constructor
    }

    //public static MyRegion newInstance(String param1, String param2) {
    //    MyRegion fragment = new MyRegion();
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

    }

    public void fillMyRegion() {
        myRegionAdapter = new RegionAdapter(myActivity, R.layout.item_region, listDsManager.getRegions());
        //myRegionAdapter.notifyDataSetChanged();
        listView.setAdapter(myRegionAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //inflater.inflate(R.layout.fragment_my_region, container, false);
        myView = inflater.inflate(R.layout.fragment_my_region, container, false);
        listView = (ListView) myView.findViewById(R.id.myRegionList);
        addRegion = (FloatingActionButton) myView.findViewById(R.id.addRegion);
        addRegion.setOnClickListener(this);
        fillMyRegion();
        return myView;
    }


    public void onAddButtonPressed(int sign) {
        if (mListener != null) {
            mListener.onRegionFragmentInteraction(sign);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addRegion:
                // 1 mean add Region
                onAddButtonPressed(1);
                break;
            default:
                break;
        }
    }

    //@Override
    //public void OnRegionAdapterDelete(int regionId) {
    //    myRegionAdapter.notifyDataSetChanged();
    //}

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

        void onRegionFragmentInteraction(int sign);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("זמני העבודה שלי");
    }
}
