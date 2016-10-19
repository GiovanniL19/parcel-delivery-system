package giovannilenguito.co.uk.parceldelivery.Models;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class User {
    private int UserID;
    private String Email;
    private String Username;
    private String Password;
    private String FullName;
    private int ContactNumber;

    public User(int userID, String email, String username, String password, String fullName, int contactNumber) {
        UserID = userID;
        Email = email;
        Username = username;
        Password = password;
        FullName = fullName;
        ContactNumber = contactNumber;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public int getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(int contactNumber) {
        ContactNumber = contactNumber;
    }
}
