package project.java.tbusdriver.Entities;

/**
 * Created by אור איטח on 27/08/2017.
 */

public class Region {
    String regionName;
    Day[] days;

    public Region(String regionName, Day[] days) {
        this.regionName = regionName;
        this.days = days;
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
