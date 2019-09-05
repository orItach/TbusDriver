package project.java.tbusdriver;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by אור איטח on 12/06/2017.
 */
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////// Here go the app constant like URL and on on /////////////
/////////////////////////////////////////////////////////////////// ///////////////////
public class Const {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //region const and URI
    static {
        sUriMatcher.addURI("project.java.contact_ptovider.model.datasorce.ContentProvide", "BusList", 1);
        sUriMatcher.addURI("project.java.contact_ptovider.model.datasorce.ContentProvide", "ActList", 2);
        sUriMatcher.addURI("project.java.contact_ptovider.model.datasorce.ContentProvide", "UserList", 3);
    }


    public static final String PROVIDE_NAME = "http://52.16.221.155/api";

    static final String URL_LOGIN = PROVIDE_NAME + "/account/login";

    public static final Uri LOGIN_URI = Uri.parse(URL_LOGIN);

    static final String URL_AUTH = PROVIDE_NAME + "/account/auth";

    public static final Uri AUTH_URI = Uri.parse(URL_AUTH);

    static final String URL_PADDING_RIDES = PROVIDE_NAME + "/taxi/active";

    public static final Uri PADDING_RIDES_URI = Uri.parse(URL_PADDING_RIDES);

    static final String URL_USER_HISTORIC_RIDE = PROVIDE_NAME + "/taxi/historic";

    public static final Uri USER_HISTORIC_RIDE_URI = Uri.parse(URL_USER_HISTORIC_RIDE);

    static final String URL_USER_MY_RIDE = PROVIDE_NAME + "/taxi/MyRides";

    public static final Uri USER_MY_RIDE_URI = Uri.parse(URL_USER_MY_RIDE);

    static final String URL_UPDATE_LOCATION = PROVIDE_NAME + "/taxi/location";

    public static final Uri UPDATE_LOCATION_URI = Uri.parse(URL_UPDATE_LOCATION);

    static final String URL_CLAIM_A_RIDE = PROVIDE_NAME + "/taxi/claim/";

    public static final Uri CLAIM_A_RIDE_URI = Uri.parse(URL_CLAIM_A_RIDE);

    static final String URL_PRESONAL_INFO = PROVIDE_NAME + "/account/me/";

    public static final Uri PRESONAL_INFO_URI = Uri.parse(URL_PRESONAL_INFO);

    static final String URL_NOTIFICATION_TOKEN = PROVIDE_NAME + "/account/taxiDriverToken";

    public static final Uri NOTIFICATION_TOKEN_URI = Uri.parse(URL_NOTIFICATION_TOKEN);

    public static final String SMS_BUNDLE="android.provider.Telephony.SMS_RECEIVED";

    /////////////////////// App Constant //////////////////////////////////////

    /////// TODO: 20/10/2017  add all constant like fragment name keys and so on to here
    //public static final Enum FragmentName=;
    public static final String AvailableRidesListName = "AvailableRides";
    public static final String MyRideListName = "MyRide";
    public static final String HistoricRidesListName = "HistoricRides";
    public static final String RegionsListName = "Regions";

    public static final String TravelFragmentName = "travel";
    public static final String myRideFragmentName = "myRide";
    public static final String availableRideFragmentName = "availableRide";
    public static final String historicRideFragmentName = "historicRide";
    public static final String myRegionFragmentName = "myRegion";
    public static final String settingFragmentName = "setting";
    public static final String personalInfoFragmentName = "personalInfo";

    //public static final Integer BusySharedPreferences = 1;
    public static final String BusySharedPreferences = "busy";


}
