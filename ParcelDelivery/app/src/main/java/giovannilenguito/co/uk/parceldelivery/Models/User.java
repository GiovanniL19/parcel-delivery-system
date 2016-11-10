package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id; //used for SQL Lite

    private String email;
    private String username;
    private String password;
    private String fullName;
    private int contactNumber;

    public User(String email, String username, String password, String fullName, int contactNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int _id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }
}