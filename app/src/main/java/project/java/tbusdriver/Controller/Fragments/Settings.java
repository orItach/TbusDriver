package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import project.java.tbusdriver.Controller.TimePickerDialog;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Day;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;

import static project.java.tbusdriver.usefulFunctions.showAlert;


public class Settings extends Fragment implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener
        {

    private OnFragmentInteractionListener mListener;

    private static RWSetting rwSettings = null;

    private String Region;
    private int FarInMinutes,TimeForReplay,TimeForNotFound,DistanceForNotFound;

    private Day[] userDays=new Day[7];
    Spinner spinnerRegion;
    Button updateButton;
    TextView TVTime;
    TextView TVSecondTime;
    View myView;
    Activity myActivity;
    DateFormat formatter;

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
        String[] region = new String[]{"ירושלים", "בני-ברק", "תל-אביב"};
        spinnerRegion = (Spinner)myView.findViewById(R.id.region);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, region);
        spinnerRegion.setAdapter(spinnerAdapter);
        spinnerRegion.setOnItemSelectedListener(this);

        updateButton = (Button) myView.findViewById(R.id.update); // you have to use rootview object..
        updateButton.setOnClickListener(this);

        TVTime = (TextView) myView.findViewById(R.id.TVTime); // you have to use rootview object..
        TVTime.setOnClickListener(this);
        TVSecondTime=(TextView)myView.findViewById(R.id.TVSecondTime);
        TVSecondTime.setOnClickListener(this);
        //Days button
        Button firstDay=(Button)myView.findViewById(R.id.firstDay);
        firstDay.setOnClickListener(this);
        Button secondDay=(Button)myView.findViewById(R.id.secondDay);
        secondDay.setOnClickListener(this);
        Button thirdDay=(Button)myView.findViewById(R.id.thirdDay);
        thirdDay.setOnClickListener(this);
        Button fourthDay=(Button)myView.findViewById(R.id.fourthDay);
        fourthDay.setOnClickListener(this);
        Button fifthDay=(Button)myView.findViewById(R.id.fifthDay);
        fifthDay.setOnClickListener(this);
        Button sixthDay=(Button)myView.findViewById(R.id.sixthDay);
        sixthDay.setOnClickListener(this);
        Button seventhDay=(Button)myView.findViewById(R.id.seventhDay);
        seventhDay.setOnClickListener(this);



        formatter = new SimpleDateFormat("HH:mm");
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
        String regionName=spinnerRegion.getSelectedItem().toString();
        Region temp = new Region(regionName,userDays);
        ListDsManager listDsManager=(ListDsManager) new Factory(getActivity()).getInstance();
        listDsManager.getRegions().add(temp);
    }
    public void updateUserDay(View v)
    {
        Button dayButton=(Button) v.findViewById(v.getId());
        String dayName=dayButton.getText().toString();
        if(userDays[dayNameToIndex(dayName)]!=null)
        {
            userDays[dayNameToIndex(dayName)]=null;
            dayButton.setBackgroundColor(Color.DKGRAY);
        }
        else
        {
            String dayStartTimeString=TVTime.getText().toString();
            String dayFinishTimeString=TVSecondTime.getText().toString();
            if(dayStartTimeString.equalsIgnoreCase("בחר זמן") ||dayFinishTimeString.equalsIgnoreCase("בחר זמן")){
                //// TODO: 16/10/2017 show massage for user
                showAlert(myActivity, "pleas choose time before choose day");
                return;
            }
            dayStartTimeString=dayStartTimeString+":00";
            dayFinishTimeString=dayFinishTimeString+":00";
            userDays[dayNameToIndex(dayName)]=new Day(dayName, Time.valueOf(dayStartTimeString),Time.valueOf(dayFinishTimeString));
            dayButton.setBackgroundColor(Color.GREEN);
        }
    }

    private int dayNameToIndex(String dayName)
    {
        switch(dayName) {
            case "א":
                return 0;
            case "ב":
                return 1;
            case "ג":
                return 2;
            case "ד":
                return 3;
            case "ה":
                return 4;
            case "ו":
                return 5;
            case "ש":
                return 6;
            default:
                return 0;
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
            case R.id.firstDay:
            case R.id.secondDay:
            case R.id.thirdDay:
            case R.id.fourthDay:
            case R.id.fifthDay:
            case R.id.sixthDay:
            case R.id.seventhDay:
                updateUserDay(v);
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
        myActivity.setTitle("הגדרת זמני עבודה");
    }


}
