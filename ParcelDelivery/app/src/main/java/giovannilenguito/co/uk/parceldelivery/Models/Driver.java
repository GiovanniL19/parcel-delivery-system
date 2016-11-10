package giovannilenguito.co.uk.parceldelivery.Models;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Driver extends User {
    private Parcel[] shipments;
    private Customer[] recipients;
    private String[] rocations;
    private String type = "Customer";

    public String getType() {
        return type;
    }

    public Driver(String email, String username, String password, String fullName, int contactNumber, Parcel[] shipments, Customer[] recipients, String[] rocations) {
        super(email, username, password, fullName, contactNumber);
        this.shipments = shipments;
        this.recipients = recipients;
        this.rocations = rocations;
    }

    public Parcel[] getShipments() {
        return shipments;
    }

    public void setShipments(Parcel[] shipments) {
        this.shipments = shipments;
    }

    public Customer[] getRecipients() {
        return recipients;
    }

    public void setRecipients(Customer[] recipients) {
        this.recipients = recipients;
    }

    public String[] getRocations() {
        return rocations;
    }

    public void setRocations(String[] rocations) {
        this.rocations = rocations;
    }
}
