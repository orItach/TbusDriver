package project.java.tbusdriver.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by אור איטח on 04/05/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                ||
                intent.getAction().equals("action.APP_KILLED")) {
            context.startService(new Intent(context, startAlarmService.class));
        }
    }
}
