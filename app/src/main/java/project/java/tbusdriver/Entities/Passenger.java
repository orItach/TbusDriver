package project.java.tbusdriver.Entities;

import android.location.Location;

/**
 * Created by אור איטח on 04/06/2017.
 */
//////////////////// NOT IN USE //////////////////////
//////////////////////////////////////////////////////
//////////////////// Passenger //////////////////////
////////////////////////////////////////////////////
public class Passenger {
    private int passengerNumber;
    private String name;
    private String phone;
    private String note;
    private Location source;
    private Location destination;
    private String paymentType;

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(int passengerNumber) {
        this.passengerNumber = passengerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public Location getdestination() {
        return destination;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public void setdestination(Location destination) {
        this.destination = destination;
    }


    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Passenger(int passengerNumber_in, String name_in, String phone_in, String note_in, Location source_in, Location destination_in, String paymentType_in) {
        this.passengerNumber = passengerNumber_in;
        this.name = name_in;
        this.phone = phone_in;
        this.note = note_in;
        this.source = source_in;
        this.destination = destination_in;
        this.paymentType = paymentType_in;
    }

}
