package project.java.tbusdriver.Database;

import android.content.Context;

/**
 * Created by אור איטח on 27/06/2017.
 */
/////////////////////////////////////////////////////////////////////////
/////////// implement the factory and singleton pattern  /////////////////
/////////////////////////////////////////////////////////////////////////
public class Factory {
    static Context context;
    private static ListDsManager theInstance;

    public Factory(Context context) {
        this.context = context;
    }

    public static ListDsManager getInstance() {
        if (theInstance == null)
            theInstance = new ListDsManager(context);
        return theInstance;
    }

    private Factory() {
    }
}
