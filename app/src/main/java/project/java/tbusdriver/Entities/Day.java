package project.java.tbusdriver.Entities;

/**
 * Created by אור איטח on 27/08/2017.
 */

public class Day {
    private String dayName;
    private String startTime;
    private String endTime;

    public Day(String dayName, String startTime, String endTime) {
        this.dayName = dayName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        Day current = (Day) obj;
        if (current != null)
            return dayName.equalsIgnoreCase(current.dayName) &&
                    startTime.equalsIgnoreCase(current.startTime) &&
                    endTime.equalsIgnoreCase(current.endTime);
        return false;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
