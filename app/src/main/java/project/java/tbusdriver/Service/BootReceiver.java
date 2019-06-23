package project.java.tbusdriver.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import project.java.tbusdriver.Const;

/**
 * Created by אור איטח on 04/05/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    public boolean checksmsnumber(SmsMessage message) {
        int temp = Integer.parseInt(message.getMessageBody());
        if (temp <= 9 && temp >= 0)
            return true;
        else
            return false;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Const.SMS_BUNDLE)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String num = "";
                final SmsMessage[] messages= new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    if (checksmsnumber(messages[i])) {
                        num += messages[i].getMessageBody();
                    }

                }

                if (messages.length > -1) {
                    Intent message = new Intent(/*/BootReceiver.this,Auth.class*/);
                    message.putExtra("code", num);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(message);
                }
            }
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                    ||
                    intent.getAction().equals("action.APP_KILLED")) {
                context.startService(new Intent(context, startAlarmService.class));
            }
        }

    }
}

