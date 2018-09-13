package project.java.tbusdriver.Entities;

/**
 * Created by אור איטח on 27/08/2017.
 */

//////////////////////////////////////////////////////
//////////////////// Region //////////////////////////
////////////////////////////////////////////////////
public class Region {
    // just because the app manage the region
    static int oldRegionID;
    // real region ID
    private int regionID;
    // region name, e.g tel aviv
    private String regionName;
    // array of days
    private Day[] days;

    public Region(String regionName, Day[] days) {
        this.regionID = oldRegionID + 1;
        oldRegionID = this.regionID;
        this.regionName = regionName;
        this.days = days;
    }


    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }


}
