package project.java.tbusdriver.Entities;

/**
 * Created by אור איטח on 27/08/2017.
 */

public class Region {
    static  int oldRegionID;
    private int regionID;
    private String regionName;
    private Day[] days;

    public Region(String regionName, Day[] days) {
        this.regionID=oldRegionID+1;
        oldRegionID=this.regionID;
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
