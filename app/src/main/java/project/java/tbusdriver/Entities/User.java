package project.java.tbusdriver.Entities;

/**
 * Created by אור איטח on 28/01/2018.
 */
//////////////////////////////////////////////////////
//////////////////// User class /////////////////////
////////////////////////////////////////////////////
public class User {
    private int driverNumber;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String photo;

    public User(int driverNumber, String name, String phone, String email, String address, String gender, String photo) {
        this.driverNumber = driverNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.photo = photo;
    }

    public int getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(int driverNumber) {
        this.driverNumber = driverNumber;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddrss() {
        return address;
    }

    public void setAddrss(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
