package project.java.tbusdriver.Service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by אור איטח on 04/05/2018.
 */

public class startAlarmService extends Service {

    Alarm alarm = new Alarm();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        alarm.setAlarm(this);
        return super.startService(service);
    }
}
