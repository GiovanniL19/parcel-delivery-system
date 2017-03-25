package giovannilenguito.co.uk.parceldelivery.model;

import java.io.Serializable;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class User implements Serializable {
    private String email;
    private String username;
    private String password;
    private String fullName;
    private Long contactNumber;

    public User(String email, String username, String password, String fullName, Long contactNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
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

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }
}