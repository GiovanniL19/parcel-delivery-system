package giovannilenguito.co.uk.parceldelivery.Models;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Driver extends User {
    private int driverId;
    private Customer[] recipients;

    public Driver(String email, String username, String password, String fullName, Long contactNumber) {
        super(email, username, password, fullName, contactNumber);
    }

    public Customer[] getRecipients() {
        return recipients;
    }

    public void setRecipients(Customer[] recipients) {
        this.recipients = recipients;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}
