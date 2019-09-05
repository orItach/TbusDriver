package project.java.tbusdriver.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import project.java.tbusdriver.Controller.Fragments.Auth;

import static project.java.tbusdriver.Controller.Travel.TAG;

/**
 * Created by אור איטח on 04/05/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
        int x =5;
    }
    /*/OnFragmentInteractionListener send;
    public interface OnFragmentInteractionListener {
        void onAuthFragmentInteraction(String fragmentReturn);

    }/*/
    public interface SmsListener {
        interface OTPListener{
            void messageReceived(String messageText,String messageSender);
        }
    }
    private static SmsListener.OTPListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            Bundle data = intent.getExtras();
            Object[] pdus = new Object[0];
            if (data != null) {
                pdus = (Object[]) data.get("pdus");
            }
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();
                    if (mListener != null) {
                        mListener.messageReceived(messageBody, sender);
                        break;
                    }
                }
            }
        }
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals("action.APP_KILLED")) {
            context.startService(new Intent(context, startAlarmService.class));
        }
    }
        public static void bindListener(SmsListener.OTPListener listener) {
            mListener = listener;
        }

        public static void unbindListener() {
            mListener = null;
        }
}
