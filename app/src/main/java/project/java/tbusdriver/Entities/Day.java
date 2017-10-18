package project.java.tbusdriver.Entities;

import java.sql.Time;

/**
 * Created by אור איטח on 27/08/2017.
 */

public class Day {
    String dayName;
    Time startTime;
    Time endTime;

    public Day(String dayName, Time startTime, Time endTime) {
        this.dayName = dayName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return dayName;
    }

    public void setName(String dayName) {
        this.dayName = dayName;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
