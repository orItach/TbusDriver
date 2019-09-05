package project.java.tbusdriver.Controller.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.R;

import static project.java.tbusdriver.usefulFunctions.POST;
import static project.java.tbusdriver.usefulFunctions.showAlert;


public class Login extends Fragment {

    private static final int REQUEST_MULTIPLE_PERMISSIONS =127;
    Context context;
    boolean checkBoxIsCheck = true;
    private String phone;
    private String userName;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Activity myActivity;
    View myView;
    LayoutInflater myinflater;
    OnFragmentInteractionListener mCallBack;
    private BroadcastReceiver receiver;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     **/

    //public static Login1 newInstance() {
    //    Login1 fragment = new Login1();
    //    Bundle args = new Bundle();
    //    return fragment;
    //}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myinflater = inflater;
        myView = inflater.inflate(R.layout.fragment_login, container, false);
        EditText Phone = (EditText) myView.findViewById(R.id.phone);
        EditText UserName = (EditText) myView.findViewById(R.id.userName);
        //Phone.setText("0524700286");
        //UserName.setText("אור איטח");
        return myView;
    }

    public void onClickLogin(View v) {
        switch (v.getId()) {
            case R.id.login:
                accessToData();
                break;
            default:
                showAlert(myActivity, "something really get wrong");
                break;
        }
    }

    public void accessToData() {
        EditText Phone = (EditText) myView.findViewById(R.id.phone);
        EditText UserName = (EditText) myView.findViewById(R.id.userName);
        String[] user = new String[2];
        user[0] = Phone.getText().toString();
            user[1] = UserName.getText().toString();
        if (user[0].equals(""))
            showAlert(myActivity, "You must enter name");
        else if (user[1].equals(""))
            showAlert(myActivity, "you must enter phone");
        else
            //user[0]=Phone user[1]=User Name
            new Login.UsersTask().execute(user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mCallBack = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public interface OnFragmentInteractionListener {
        //the fragmentReturn mean if the login work 1 for good 0 for bad
        void OnLoginFragmentInteractionListener(int fragmentReturn);
    }

    class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("username", params[1]);
            parameters.put("phone", params[0]);
            parameters.put("taxiFlag", 1);

            try {
                toReturn = POST(Const.LOGIN_URI.toString(), parameters);
                if (toReturn.compareTo(("{\"status\":\"ok\"}").toString()) == 0) {
                    publishProgress("");
                    toReturn = "";
                } else {
                    publishProgress("something get wrong      " + toReturn);
                }
                parameters.clear();
            } catch (Exception e) {
                parameters.clear();
                publishProgress(e.getMessage());

            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(String result) {


            if (result.equals("")) {
                //every thing is okay
                mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //user[0]=Phone user[1]=User Name
            //check if have any error
            if (values[0].length() > 1)
                showAlert(myActivity, values[0]);
            else {

                //mCallBack.OnLoginFragmentInteractionListener(1);
            }
        }
    }

}

