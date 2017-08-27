package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import project.java.tbusdriver.Controller.TimePickerDialog;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Settings extends Fragment implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener
        {

    private OnFragmentInteractionListener mListener;

    private static RWSetting rwSettings = null;

    private String Region;
    private int FarInMinutes,TimeForReplay,TimeForNotFound,DistanceForNotFound;


    Button updateButton;
    TextView TVTime;
    TextView TVSecondTime;
    View myView;
    Activity myActivity;
    final int REQUEST_CODE=0;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView=inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle("Setting");
        String[] region = new String[]{"jerusalem", "bney-brak", "tel-aviv"};
        Spinner spinnerRegion = (Spinner)myView.findViewById(R.id.region);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, region);
        spinnerRegion.setAdapter(spinnerAdapter);
        spinnerRegion.setOnItemSelectedListener(this);

        updateButton = (Button) myView.findViewById(R.id.update); // you have to use rootview object..
        updateButton.setOnClickListener(this);

        TVTime = (TextView) myView.findViewById(R.id.TVTime); // you have to use rootview object..
        TVTime.setOnClickListener(this);
        TVSecondTime=(TextView)myView.findViewById(R.id.TVSecondTime);
        TVSecondTime.setOnClickListener(this);
        return myView;
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

    public void updateSettings(View v)
    {
        if (mListener != null) {

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.update:
                updateSettings(v);
                break;
            case R.id.TVTime:
                showDialog(R.id.TVTime);
                break;
            case R.id.TVSecondTime:
                showDialog(R.id.TVSecondTime);
                break;
            default:
                break;
        }
    }

    void showDialog(int source) {
        TimePickerDialog dialog = new TimePickerDialog();
        dialog.setTargetFragment(this, source);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Region=(String) parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Region="jerusalem";
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        if (requestCode == R.id.TVTime) {
            TVTime=(TextView)myView.findViewById(R.id.TVTime);
            String result=data.getStringExtra("result");
            if(result!="")
                TVTime.setText(result);
        }
        if (requestCode == R.id.TVSecondTime) {
            TVSecondTime=(TextView)myView.findViewById(R.id.TVSecondTime);
            String result=data.getStringExtra("result");
            if(result!="")
                TVSecondTime.setText(result);
        }
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
        //EditText ERegion=(EditText) myView.findViewById(R.id.ERegion);
        //EditText EFarInMinutes=(EditText) myView.findViewById(R.id.EFarInMinutes);
        //EditText ETimeForReplay=(EditText) myView.findViewById(R.id.ETimeForReplay);
        //EditText ETimeForNotFound=(EditText) myView.findViewById(R.id.ETimeForNotFound);
        //EditText EDistanceForNotFound=(EditText) myView.findViewById(R.id.EDistanceForNotFound);
        //Region=ERegion.getText().toString();
        //FarInMinutes=Integer.valueOf(EFarInMinutes.getText().toString());
        //TimeForReplay=Integer.valueOf(ETimeForReplay.getText().toString());
        //TimeForNotFound=Integer.valueOf(ETimeForNotFound.getText().toString());
        //DistanceForNotFound =Integer.valueOf(EDistanceForNotFound.getText().toString());
        //if(FarInMinutes<1 || TimeForReplay<1|| TimeForNotFound<1 ||DistanceForNotFound<1)
        //{
        //    showAlert("some thing get wrong");
        //}
        //else {
        //    rwSettings.setStringSetting("Region", Region);
        //    rwSettings.setIntSetting("FarInMinutes", FarInMinutes);
        //    rwSettings.setIntSetting("TimeForReplay", TimeForReplay);
        //    rwSettings.setIntSetting("TimeForNotFound", TimeForNotFound);
        //    rwSettings.setIntSetting("DistanceForNotFound", DistanceForNotFound);
        //    ERegion.setText("");
        //    EFarInMinutes.setText("");
        //    ETimeForReplay.setText("");
        //    ETimeForNotFound.setText("");
        //    EDistanceForNotFound.setText("");
        //}
    }
    @Override
    public void onResume()
    {
        super.onResume();
        myActivity=getActivity();
        myActivity.setTitle("זמני עבודה");
    }
}
