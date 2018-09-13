package project.java.tbusdriver.Service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.java.tbusdriver.Const;

import static project.java.tbusdriver.usefulFunctions.POST;

public class NotificationIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        new NotificationIDService.SendNotficationToken().execute(token);
    }

    class SendNotficationToken extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //user[0]=Phone user[1]=User Name
            String toReturn = "";
            try {
                Log.d(TAG, "Refreshed token: " + params[0]);
            } catch (Exception e) {
                Log.d(TAG, "error: ");
            }
            try {
                Map<String, Object> parameters = new HashMap<String, Object>();
                try {
                    parameters.put("token", params[0]);
                } catch (Exception e) {
                    int x = 5;
                }
                parameters.put("device", "android");
                toReturn = POST(Const.NOTIFICATION_TOKEN_URI.toString(), parameters);
                String httpResult = new JSONObject(toReturn).getString("user");
                if (!httpResult.equalsIgnoreCase("")) {
                    //listDsManager.updateAvailableRides(toReturn);
                    publishProgress("");
                    toReturn = "";
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

            if (result.equals("")) {
                //every thing is okay
                //mListener.onFragmentInteraction("");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //user[0]=Phone user[1]=User Name
            //check if have any error
            if (values[0].length() > 1) {
                //showAlert(,values[0]);
            } else {

                //mCallBack.OnLoginFragmentInteractionListener(1);
            }

        }
    }

}
