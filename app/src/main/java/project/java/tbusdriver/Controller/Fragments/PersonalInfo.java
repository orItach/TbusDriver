package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Activitys.MainActivity;
import project.java.tbusdriver.Entities.User;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.usefulFunctions.GET;
import static project.java.tbusdriver.usefulFunctions.PUT;
import static project.java.tbusdriver.usefulFunctions.showAlert;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PersonalInfo extends Fragment
        implements AdapterView.OnItemSelectedListener {

    Activity myActivity;
    View myView;
    TextView name;
    TextView phone;
    TextView email;
    TextView address;
    String gender;
    Spinner spinnerGender;
    Button update;

    private OnFragmentInteractionListener mListener;

    public PersonalInfo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getActivity().setTitle("Available Ride");
        myView = inflater.inflate(R.layout.fragment_personal_info, container, false);


        // Inflate the layout for this fragment
        name = (TextView) myView.findViewById(R.id.name);
        phone = (TextView) myView.findViewById(R.id.phone);
        email = (TextView) myView.findViewById(R.id.email);
        address = (TextView) myView.findViewById(R.id.address);
        spinnerGender = (Spinner) myView.findViewById(R.id.gender);
        String[] genders = new String[]{"זכר", "נקבה",};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genders);
        spinnerGender.setAdapter(spinnerAdapter);
        spinnerGender.setOnItemSelectedListener(this);
        update = (Button) myView.findViewById(R.id.update);
        update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] user = new String[2];
                        user[0] = name.getText().toString().trim();
                        user[1] = email.getText().toString().trim();
                        //if(user[0].equals(""))
                        //    showAlert(myActivity,"You must enter name");
                        //else if(user[1].equals(""))
                        //    showAlert(myActivity,"you must enter phone");
                        //else
                        //user[0]=Phone user[1]=User Name
                        new PersonalInfo.UpdateUser().execute(user);
                    }
                }
        );
        fillPersonalInfo();
        // Inflate the layout for this fragment
        return myView;
    }

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

    private void fillPersonalInfo() {
        new PersonalInfo.GetUser().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        gender = "זכר";
    }

    class GetUser extends AsyncTask<String, User, User> {
        @Override
        protected User doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";
            User resultUser = null;

            try {
                toReturn = GET(Const.PRESONAL_INFO_URI.toString());
                resultUser = parsePersonalInfo(toReturn);
                if (resultUser != null) {
                    //listDsManager.updateAvailableRides(toReturn);
                    publishProgress(resultUser);
                    toReturn = "";
                } else {
                    publishProgress(null);
                }
            } catch (Exception e) {
                publishProgress(null);

            }
            return resultUser;
        }

        @Override
        protected void onPostExecute(User result) {
            if (result != null) {
                //every thing is okay
                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(User... values) {
            //user[0]=Phone user[1]=User Name
            //check if have any error
            if (values == null) {
                showAlert(myActivity, "something get wrong");
                return;
            }
            if (values[0] == null) {
                showAlert(myActivity, "something get wrong");
                return;
            } else {
                name.setText(values[0].getName());
                phone.setText(values[0].getPhone());
                email.setText(values[0].getEmail());
                address.setText(values[0].getAddrss());

                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }
    }

    class UpdateUser extends AsyncTask<String, User, User> {
        @Override
        protected User doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";
            User updatedUser = null;
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("name", params[0]);
            parameters.put("email", params[1]);

            try {
                toReturn = PUT(Const.PRESONAL_INFO_URI.toString(), parameters);
                updatedUser = parsePersonalInfo(toReturn);
                if (updatedUser != null) {
                    //listDsManager.updateAvailableRides(toReturn);
                    publishProgress(updatedUser);
                    toReturn = "";
                } else {
                    publishProgress(null);
                }
                parameters.clear();
            } catch (Exception e) {
                parameters.clear();
                publishProgress(null);

            }
            return updatedUser;
        }

        @Override
        protected void onPostExecute(User result) {


            if (result.equals("")) {
                //every thing is okay
                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(User... values) {
            //user[0]=Phone user[1]=User Name
            //check if have any error
            //if(values[0].length()>1)
            //    showAlert(myActivity,values[0]);
            //else {
            //    //mCallBack.OnLoginFragmentInteractionListener(1);
            //}
            if (values == null) {
                showAlert(myActivity, "something get wrong");
                return;
            }
            if (values[0] == null) {
                showAlert(myActivity, "something get wrong");
                return;
            } else {
                name.setText(values[0].getName());
                phone.setText(values[0].getPhone());
                email.setText(values[0].getEmail());
                address.setText(values[0].getAddrss());
                showAlert(myActivity, "עדכון פרטים אישיים עבר בהצלחה");
                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }
    }

    @Nullable
    private User parsePersonalInfo(String data) {
        //JSONObject c = rides.getJSONObject(i);
        //travel_time = c.getString("travel_time");
        //String name =
        ///{ user: {name: x, id:}
        try {
            String userData = new JSONObject(data).getString("user");
            int driverNumber = new JSONObject(userData).getInt("id");
            String name = new JSONObject(userData).getString("name");
            String phone = new JSONObject(userData).getString("phone");
            String email = new JSONObject(userData).getString("email");
            if (email.toLowerCase() == "null") {
                email = "";
            }
            String address = new JSONObject(userData).getString("address");
            if (address.toLowerCase() == "null") {
                address = "";
            }
            String gender = new JSONObject(userData).getString("gender");
            if (gender == null) {
                gender = "";
            }
            return new User(driverNumber, name, phone, email, address, gender, "");
        } catch (Exception e) {

        }
        return null;
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
        void onFragmentInteraction(Uri uri);
    }
}
