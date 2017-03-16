package giovannilenguito.co.uk.parceldelivery.Models;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Driver extends User {
    private Parcel[] parcels;
    private Customer[] recipients;
    private String type = "Driver";

    public String getType() {
        return type;
    }

    public Driver(String email, String username, String password, String fullName, Long contactNumber) {
        super(email, username, password, fullName, contactNumber);
    }

    public Parcel[] getParcels() {
        return parcels;
    }

    public void setParcels(Parcel[] parcels) {
        this.parcels = parcels;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Customer[] getRecipients() {
        return recipients;
    }

    public void setRecipients(Customer[] recipients) {
        this.recipients = recipients;
    }

}
