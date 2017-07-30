package project.java.tbusdriver.Entities;

import android.location.Location;

/**
 * Created by אור איטח on 26/06/2017.
 */

public class MyLocation {

    private Location myLocation;
    private String instruction;
    private String phone;
    private boolean manLocation;

    public MyLocation(Location myLocation,String instruction, String phone,boolean manLocation) {
        this.myLocation = myLocation;
        this.instruction=instruction;
        this.phone = phone;
        this.manLocation=manLocation;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isManLocation() {
        return manLocation;
    }

    public void setManLocation(boolean manLocation) {
        this.manLocation = manLocation;
    }
}
