package project.java.tbusdriver.Controller.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Const;
import project.java.tbusdriver.Controller.Activitys.LoginAuth;
import project.java.tbusdriver.Controller.Activitys.MainActivity;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;
import project.java.tbusdriver.Service.BootReceiver;

import static project.java.tbusdriver.usefulFunctions.*;


public class Auth extends Fragment// implements BootReceiver.OnFragmentInteractionListener
{
    RWSetting rwSettings = null;
    boolean checkBoxIsCheck = true;
    private String phone;
    Activity myActivity;
    View myView;
    private EditText Authcode;
    OnFragmentInteractionListener mCallBack;
    private SharedPreferences pref;
     EditText AuthCode;
    private BroadcastReceiver messagesReceiver;
    /**
     * The Editor.
     */
    private SharedPreferences.Editor editor;

    public Auth() {
    }

    // receiver as a global variable in your Fragment class


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        myView = inflater.inflate(R.layout.fragment_auth, container, false);
        //phone=savedInstanceState.getString("PHONE");
        phone = getArguments().getString("PHONE");
        //phone=savedInstanceState.getString("PHONE");
        //getArguments().getString("PHONE");
        // Inflate the layout for this fragment
            AuthCode = (EditText) myView.findViewById(R.id.authCode);
          BootReceiver.bindListener(new BootReceiver.SmsListener.OTPListener() {
            @Override
            public void messageReceived(String messageText, String messageSender) {

                AuthCode.setText(getTheNumberInString(messageText));

            }
        });
        return myView;
    }


    public void onClickAuth(View v) {
        switch (v.getId()) {
            case R.id.authCheck:
                accessToData();
                break;
            default:
                showAlert(myActivity, "something really get wrong");
                break;
        }

    }

    private void accessToData() {
        EditText AuthCode = (EditText) myView.findViewById(R.id.authCode);
        String[] user = new String[2];
        user[0] = phone;
        user[1] = AuthCode.getText().toString();
        if (user[0].equals(""))
            showAlert(myActivity, "You must enter name");
        else if (user[1].equals(""))
            showAlert(myActivity, "you must enter phone");
        else
            //worng
            //user[0]=Phone user[1]=User Name
            new Auth.UsersTask().execute(user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pref = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        editor = pref.edit();
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
        //the fragmentReturn mean if the login work 1 for good 0 for bad
        void onAuthFragmentInteraction(int fragmentReturn);
    }


    class UsersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=Auth Code
            String toReturn = "";
            String answer;
            String[] answerArray = new String[2];
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("phone", params[0]);
            parameters.put("auth", params[1]);
            try {
                answer = POST(Const.AUTH_URI.toString(), parameters);
                answerArray = answer.split(",");
                if (answerArray[0].compareTo(("{\"status\":\"ok\"").toString()) == 0) {
                    Token = answerArray[1].split("\"")[3];
                    rwSettings = RWSetting.getInstance(getActivity());
                    rwSettings.setStringSetting("Token", Token);
                    publishProgress(Token);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    publishProgress("something get wrong      " + toReturn);
                }
            } catch (Exception e) {
                publishProgress(e.getMessage());

            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("")) {
                //every thing is okay
                mCallBack.onAuthFragmentInteraction(1);
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
                EditText authCode = (EditText) myActivity.findViewById(R.id.authCode);
                authCode.setText("");
                if (checkBoxIsCheck == true)
                {
                    editor.putString("PHONE", phone);
                    editor.commit();
                }
            }
        }
    }

    //the brodcast reciver brings us all the message and we need only the code
        String getTheNumberInString(String sms){
        String num="";
        char[] smschar=sms.toCharArray();
        for(int i=0;i<sms.length();i++){
            if(smschar[i]>='0'&&smschar[i]<='9')
                num+=smschar[i];
        }
        return num;
    }
}