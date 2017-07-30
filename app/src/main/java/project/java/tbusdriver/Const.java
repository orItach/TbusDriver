package project.java.tbusdriver;

import android.content.UriMatcher;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by אור איטח on 12/06/2017.
 */

public class Const {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //region const and URI
    static {
        sUriMatcher.addURI("project.java.contact_ptovider.model.datasorce.ContentProvide", "BusList", 1);
        sUriMatcher.addURI("project.java.contact_ptovider.model.datasorce.ContentProvide", "ActList", 2);
        sUriMatcher.addURI("project.java.contact_ptovider.model.datasorce.ContentProvide", "UserList", 3);
    }


    public static final String PROVIDE_NAME = "http://ec2-34-250-193-161.eu-west-1.compute.amazonaws.com/api";

    static final String URL_LOGIN = PROVIDE_NAME + "/account/login";

    public static final Uri LOGIN_URI = Uri.parse(URL_LOGIN);

    static final String URL_AUTH = PROVIDE_NAME + "/account/auth";

    public static final Uri AUTH_URI = Uri.parse(URL_AUTH);

    static final String URL_PADDING_RIDES = PROVIDE_NAME + "/taxi/active";

    public static final Uri PADDING_RIDES_URI = Uri.parse(URL_PADDING_RIDES);

    static final String URL_USER_HISTORIC_RIDE = PROVIDE_NAME + "/taxi/historic";

    public static final Uri USER_HISTORIC_RIDE_URI = Uri.parse(URL_USER_HISTORIC_RIDE);

    static final String URL_UPDATE_LOCATION = PROVIDE_NAME + "/taxi/location";

    public static final Uri UPDATE_LOCATION_URI = Uri.parse(URL_UPDATE_LOCATION);

    static final String URL_CLAIM_A_RIDE = PROVIDE_NAME + "/taxi/claim/";

    public static final Uri CLAIM_A_RIDE_URI = Uri.parse(URL_CLAIM_A_RIDE);

}
