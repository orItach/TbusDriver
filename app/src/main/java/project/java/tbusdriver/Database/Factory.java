package project.java.tbusdriver.Database;

import android.content.Context;

/**
 * Created by אור איטח on 27/06/2017.
 */

public class Factory {
    private static ListDsManager ourInstance;
    //public static ListDsManager getInstance() throws Exception {
    //    if(ourInstance == null)
    //        throw new Exception("No Instance found");
    //    return ourInstance;
    //}

    public static ListDsManager getInstance(Context context){
        if(ourInstance == null)
            ourInstance = new ListDsManager(context);
        return ourInstance;
    }

    private Factory() {}
}
