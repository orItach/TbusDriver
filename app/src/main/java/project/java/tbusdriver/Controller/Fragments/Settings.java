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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import project.java.tbusdriver.Controller.TimePickerDialog;
import project.java.tbusdriver.Database.Factory;
import project.java.tbusdriver.Database.ListDsManager;
import project.java.tbusdriver.Entities.Day;
import project.java.tbusdriver.Entities.Region;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;

import static project.java.tbusdriver.Const.RegionsListName;
import static project.java.tbusdriver.Database.ListDsManager.convertRideIdToIndex;
import static project.java.tbusdriver.usefulFunctions.showAlert;


public class Settings extends Fragment implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;

    private static RWSetting rwSettings = null;

    private String RegionName;
    private int FarInMinutes, TimeForReplay, TimeForNotFound, DistanceForNotFound;

    private Day[] userDays = new Day[7];
    Spinner spinnerRegion;
    Button updateButton;
    TextView TVTime;
    TextView TVSecondTime;
    View myView;
    Activity myActivity;
    DateFormat formatter;
    ListDsManager listDsManager;
    Button firstDay;
    Button secondDay;
    Button thirdDay;
    Button fourthDay;
    Button fifthDay;
    Button sixthDay;
    Button seventhDay;
    Region region;
    CheckBox allDay;
    public static Settings instance;

    final int REQUEST_CODE = 0;

    public Settings() {
        // Required empty public constructor
    }

    public static Settings newInstance() {
        if (instance == null) {
            instance = new Settings();
        } else {

            return instance;
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDsManager = (ListDsManager) new Factory(getActivity()).getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle("Setting");
        String[] region = new String[]{"ירושלים", "בני-ברק", "תל-אביב"};
        spinnerRegion = (Spinner) myView.findViewById(R.id.region);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, region);
        spinnerRegion.setAdapter(spinnerAdapter);
        spinnerRegion.setOnItemSelectedListener(this);

        updateButton = (Button) myView.findViewById(R.id.update); // you have to use rootview object..
        updateButton.setOnClickListener(this);

        TVTime = (TextView) myView.findViewById(R.id.TVTime); // you have to use rootview object..
        TVTime.setOnClickListener(this);
        //TVTime.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        showDialog(R.id.TVTime);
        //    }
        //});
        TVSecondTime = (TextView) myView.findViewById(R.id.TVSecondTime);
        TVSecondTime.setOnClickListener(this);

        //Days button
        firstDay = (Button) myView.findViewById(R.id.firstDay);
        firstDay.setOnClickListener(this);
        secondDay = (Button) myView.findViewById(R.id.secondDay);
        secondDay.setOnClickListener(this);
        thirdDay = (Button) myView.findViewById(R.id.thirdDay);
        thirdDay.setOnClickListener(this);
        fourthDay = (Button) myView.findViewById(R.id.fourthDay);
        fourthDay.setOnClickListener(this);
        fifthDay = (Button) myView.findViewById(R.id.fifthDay);
        fifthDay.setOnClickListener(this);
        sixthDay = (Button) myView.findViewById(R.id.sixthDay);
        sixthDay.setOnClickListener(this);
        seventhDay = (Button) myView.findViewById(R.id.seventhDay);
        seventhDay.setOnClickListener(this);


        allDay = (CheckBox) myView.findViewById(R.id.allDay);
        allDay.setOnClickListener(this);
        allDay.setChecked(false);
        formatter = new SimpleDateFormat("HH:mm");
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction(1);
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

    // called when the user press update
    public void updateSettings(View v) {
        ListDsManager listDsManager = (ListDsManager) new Factory(getActivity()).getInstance();
        Region temp;
        Bundle bundle = this.getArguments();
        String dayStartTimeString = TVTime.getText().toString();
        String dayFinishTimeString = TVSecondTime.getText().toString();
        if (allDay.isChecked()) {
            dayStartTimeString = "00:01";
            dayFinishTimeString = "23:59";
            TVTime.setText(dayStartTimeString);
            TVSecondTime.setText(dayFinishTimeString);
        }
        // if new time added
        if (bundle == null) {
            String regionName = spinnerRegion.getSelectedItem().toString();
            if (dayStartTimeString.equalsIgnoreCase("בחר זמן") || dayFinishTimeString.equalsIgnoreCase("בחר זמן")) {
                showAlert(myActivity, "הכנס זמן התחלה וסיום לפני בחירת יום");
                return;
            }
            //try {
            //    if(formatter.parse(dayStartTimeString+":00").after(formatter.parse(dayFinishTimeString+":00")))
            //    {
            //        showAlert(myActivity, "בבקשה בחר זמן סיום אחרי שמן התחלה");
            //        return;
            //    }
            //}
            //catch (ParseException e)
            //{
            //}
            /// change the time in days
            Day[] newDays = new Day[7];
            for (int i = 0; i < newDays.length; i++) {
                if (userDays[i] != null) {
                    newDays[i] = new Day(userDays[i].getDayName(), userDays[i].getStartTime(), userDays[i].getEndTime());
                }
            }
            temp = new Region(regionName, newDays);
            boolean isNewRegion = checkIfNewRegion(temp);
            if (isNewRegion == false) {
                showAlert(myActivity, "הכנס אזור עבודה חדש");
                return;
            }
            ListDsManager.getRegions().add(temp);
            showAlert(myActivity, "אזור עבודה נוסף בהצלחה");
        }
        //if send some RegionId- its mean update
        else {
            temp = ListDsManager.getRegions().get(convertRideIdToIndex(RegionsListName, bundle.getInt("REGIONID", 0)));
            temp.setRegionName(spinnerRegion.getSelectedItem().toString());
            if (dayStartTimeString.equalsIgnoreCase("בחר זמן") || dayFinishTimeString.equalsIgnoreCase("בחר זמן")) {
                showAlert(myActivity, "הכנס זמן התחלה וזמן סיום לפני בחירת יום");
                return;
            }

            try {
                if (formatter.parse(dayStartTimeString + ":00").after(formatter.parse(dayFinishTimeString + ":00"))) {
                    showAlert(myActivity, "בחר זמן התחלה לפני זמן סיום");
                    return;

                }
            } catch (ParseException e) {

            }
            Day[] newDays = new Day[7];
            for (int i = 0; i < newDays.length; i++) {
                if (userDays[i] != null) {
                    newDays[i] = new Day(userDays[i].getDayName(), dayStartTimeString, dayFinishTimeString);
                }
            }
            temp.setDays(newDays);
            showAlert(myActivity, "אזור עבודה עודכן בהצלחה");
        }
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction(1);
        }
    }

    public void updateUserDay(View v) {
        Button dayButton = (Button) v.findViewById(v.getId());
        String dayName = dayButton.getText().toString();
        // 18/11/17 Or: if the user all ready select this Day
        if (userDays[dayNameToIndex(dayName)] != null) {
            userDays[dayNameToIndex(dayName)] = null;
            dayButton.setBackgroundColor(Color.DKGRAY);
        } else {
            String dayStartTimeString = TVTime.getText().toString();
            String dayFinishTimeString = TVSecondTime.getText().toString();
            if (dayStartTimeString.equalsIgnoreCase("בחר זמן") || dayFinishTimeString.equalsIgnoreCase("בחר זמן")) {
                showAlert(myActivity, "pleas choose time before choose day");
                return;
            }

            userDays[dayNameToIndex(dayName)] = new Day(dayName, dayStartTimeString, dayFinishTimeString);
            dayButton.setBackgroundColor(Color.GREEN);
        }
    }

    private int dayNameToIndex(String dayName) {
        switch (dayName) {
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
        switch (v.getId()) {
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
            case R.id.allDay:
                checkAllDay(v);
                break;
            default:
                break;
        }
    }

    public void checkAllDay(View v) {
        if (allDay.isChecked()) {
            TVTime.setEnabled(false);
            TVSecondTime.setEnabled(false);
            TVTime.setClickable(false);
            TVSecondTime.setClickable(false);
            TVTime.setAlpha(0.5f);
            TVSecondTime.setAlpha(0.5f);
            for (Day userDay : userDays) {
                if (userDay != null) {
                    userDay.setStartTime("00:01:00");
                    userDay.setEndTime("23:59:00");
                }
            }
            TVTime.setText("00:01");
            TVSecondTime.setText("23:59");
        } else {
            TVTime.setEnabled(true);
            TVSecondTime.setEnabled(true);
            TVTime.setClickable(true);
            TVSecondTime.setClickable(true);
            TVTime.setAlpha(1.0f);
            TVSecondTime.setAlpha(1.0f);
        }
    }

    void showDialog(int source) {
        TimePickerDialog dialog = new TimePickerDialog();
        dialog.setTargetFragment(this, source);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        RegionName = (String) parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        RegionName = "jerusalem";
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up

        if (requestCode == R.id.TVTime) {
            TVTime = (TextView) myView.findViewById(R.id.TVTime);
            String TVTimeBeforeAction = TVTime.getText().toString();
            String result = data.getStringExtra("result");
            if (result != "") {
                try {
                    if (TVSecondTime.getText().toString() == "--:--" || TVSecondTime.getText().toString().equalsIgnoreCase("בחר זמן"))
                        TVSecondTime.setText("23:59");
                    if (formatter.parse(result + ":00")
                            .after(formatter.parse(TVSecondTime.getText().toString() + ":00"))) {
                        showAlert(myActivity, "זמן הסיום צריך להיות מוגדר מאוחר יותר מזמן ההתחלה");
                        return;
                    } else {
                        TVTime.setText(result);
                    }
                } catch (ParseException e) {
                    TVTime.setText(TVTimeBeforeAction);
                }
            }
        }
        if (requestCode == R.id.TVSecondTime) {
            TVSecondTime = (TextView) myView.findViewById(R.id.TVSecondTime);
            String TVSecondTimeBeforeAction = TVSecondTime.getText().toString();
            String result = data.getStringExtra("result");
            if (result != "") {
                try {
                    if (TVTime.getText().toString() == "--:--" || TVTime.getText().toString().equalsIgnoreCase("בחר זמן"))
                        TVTime.setText("00:01");
                    //if(TVSecondTime.getText().toString() == "--:--" || TVSecondTime.getText().toString() == "בחר זמן")
                    //    TVSecondTime.setText("00:01");
                    if (formatter.parse(TVTime.getText().toString() + ":00")
                            .after(formatter.parse(result + ":00"))) {
                        showAlert(myActivity, "זמן הסיום צריך להיות מוגדר מאוחר יותר מזמן ההתחלה");
                        return;
                    } else {
                        TVSecondTime.setText(result);
                    }
                } catch (ParseException e) {
                    TVSecondTime.setText("--:--");
                }
            }
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

        void onSettingsFragmentInteraction(int sign);
    }

    public void onClickUpdate(View v) {
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
    public void onResume() {
        super.onResume();
        //if(isHidden() == false) {
        int regionId;
        int regionIndex;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            regionId = bundle.getInt("REGIONID", 0);
            regionIndex = convertRideIdToIndex("Regions", regionId);
            region = ListDsManager.getRegions().get(regionIndex);
            proccesDaysArgument(region.getDays());
            userDays = region.getDays();
            int firstExistDay = findFirstDay(userDays);
            String[] startTime = region.getDays()[firstExistDay].getStartTime().split(":");
            String[] endTime = region.getDays()[firstExistDay].getEndTime().split(":");
            TVTime.setText(startTime[0] + ":" + startTime[1]);
            TVSecondTime.setText(endTime[0] + ":" + endTime[1]);
            //spinnerRegion.setSelection();
            updateButton.setText("עדכן");
            spinnerRegion.setSelection(findRegionNameSpinnerIndex(spinnerRegion, region.getRegionName()), true);
        } else {
            userDays = new Day[7];
        }
        //}
        myActivity = getActivity();
        myActivity.setTitle("הגדרת זמני עבודה");
    }

    private void proccesDaysArgument(Day[] argumentDays) {
        for (int i = 0; i < argumentDays.length; i++) {
            if (argumentDays[i] != null) {
                switch (argumentDays[i].getDayName()) {
                    case "א":
                        firstDay.setBackgroundColor(Color.GREEN);
                        break;
                    case "ב":
                        secondDay.setBackgroundColor(Color.GREEN);
                        break;
                    case "ג":
                        thirdDay.setBackgroundColor(Color.GREEN);
                        break;
                    case "ד":
                        fourthDay.setBackgroundColor(Color.GREEN);
                        break;
                    case "ה":
                        fifthDay.setBackgroundColor(Color.GREEN);
                        break;
                    case "ו":
                        sixthDay.setBackgroundColor(Color.GREEN);
                        break;
                    case "ש":
                        seventhDay.setBackgroundColor(Color.GREEN);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int findFirstDay(Day[] days) {
        for (int i = 0; i < days.length; i++) {
            if (days[i] != null) {
                return i;
            }
        }
        // 25/10/17 maybe need to be zero
        return 0;
    }

    private List<String> getAllSpinnerContent(Spinner spinner) {
        Adapter adapter = spinner.getAdapter();
        int n = adapter.getCount();
        List<String> regionNames = new ArrayList<String>(n);
        for (int i = 0; i < n; i++) {
            String regionName = adapter.getItem(i).toString();
            regionNames.add(regionName);
        }
        return regionNames;
    }

    private int findRegionNameSpinnerIndex(Spinner spinner, String regionName) {
        List<String> regionNames = getAllSpinnerContent(spinner);
        int i = 0;
        for (String tempRegionName : regionNames) {
            if (tempRegionName.toLowerCase().equals(regionName.toLowerCase()))
                return i;
            else
                i++;
        }
        return 0;
    }

    /// return true if the newRegion is really new Region
    private boolean checkIfNewRegion(Region newRegion) {
        boolean check = true;
        // if in the inner loop we find that is all the same so check will stay true that means the object is not new.
        // but if one day is different so check will be false till the end of the loop and the
        // object is different so we need to check the other objects.
        for (Region myRegion : ListDsManager.getRegions()) {
            check = true;
            for (int i = 0; i < newRegion.getDays().length; i++) {
                Day[] myRegionDay = myRegion.getDays();
                Day[] newRegionDay = newRegion.getDays();
                if (newRegionDay[i] != null)
                    check = check && newRegionDay[i].equals(myRegionDay[i]);
                else if (myRegionDay[i] == null)
                    check = check && true;
                else
                    check = check && false;

                //if(myRegionDay[i] !=null && newRegionDay[i] == null)
                //    return false;
                //if(myRegionDay[i] ==null && newRegionDay[i] != null)
                //    return false;
                //if(myRegionDay[i] ==null && newRegionDay[i] == null)
                //    continue;
                //if(!(myRegionDay[i].getStartTime().equalsIgnoreCase(newRegionDay[i].getStartTime())) &&
                //        !(myRegionDay[i].getEndTime().equalsIgnoreCase(newRegionDay[i].getEndTime())))
                //{
                //    return false;
                //}
            }
            if (check) // the current region is different;
                return false;
        }
        return true;
    }
}
