package project.java.tbusdriver.Controller.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Settings extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static RWSetting rwSettings = null;

    private String Region;
    private int FarInMinutes,TimeForReplay,TimeForNotFound,DistanceForNotFound;

    View myView;

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Setting");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }

    public void onClickUpdate(View v)
    {
        EditText ERegion=(EditText) myView.findViewById(R.id.ERegion);
        EditText EFarInMinutes=(EditText) myView.findViewById(R.id.EFarInMinutes);
        EditText ETimeForReplay=(EditText) myView.findViewById(R.id.ETimeForReplay);
        EditText ETimeForNotFound=(EditText) myView.findViewById(R.id.ETimeForNotFound);
        EditText EDistanceForNotFound=(EditText) myView.findViewById(R.id.EDistanceForNotFound);
        Region=ERegion.getText().toString();
        FarInMinutes=Integer.valueOf(EFarInMinutes.getText().toString());
        TimeForReplay=Integer.valueOf(ETimeForReplay.getText().toString());
        TimeForNotFound=Integer.valueOf(ETimeForNotFound.getText().toString());
        DistanceForNotFound =Integer.valueOf(EDistanceForNotFound.getText().toString());
        if(FarInMinutes<1 || TimeForReplay<1|| TimeForNotFound<1 ||DistanceForNotFound<1)
        {
            showAlert("some thing get wrong");
        }
        else {
            rwSettings.setStringSetting("Region", Region);
            rwSettings.setIntSetting("FarInMinutes", FarInMinutes);
            rwSettings.setIntSetting("TimeForReplay", TimeForReplay);
            rwSettings.setIntSetting("TimeForNotFound", TimeForNotFound);
            rwSettings.setIntSetting("DistanceForNotFound", DistanceForNotFound);
            ERegion.setText("");
            EFarInMinutes.setText("");
            ETimeForReplay.setText("");
            ETimeForNotFound.setText("");
            EDistanceForNotFound.setText("");
        }
    }

    void showAlert(String alert)
    {
        //AlertDialog.Builder myAlert=new AlertDialog.Builder(this);
        //myAlert.setMessage(alert).create();
        //myAlert.show();
    }
}
