package project.java.tbusdriver;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by אור איטח on 06/06/2017.
 */

public class RWSetting {
    private static RWSetting instance = null;
    private SharedPreferences pref;
    /**
     * The Editor.
     */
    private SharedPreferences.Editor editor;


    private RWSetting(Context context) {
        String name = "Setting";
        pref = context.getSharedPreferences("SettingPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public int getIntSetting(String parameter) {
        return pref.getInt(parameter, -1);
    }

    public String getStringSetting(String parameter) {
        return pref.getString(parameter, "");
    }

    public static RWSetting getInstance(Context context) {
        if (instance == null) {
            instance = new RWSetting(context);
        }
        return instance;
    }

    public void setIntSetting(String parameter, int setting) {
        editor.putInt(parameter, setting);
        editor.commit();
    }

    public void setStringSetting(String parameter, String setting) {
        editor.putString(parameter, setting);
        editor.commit();
    }
}
