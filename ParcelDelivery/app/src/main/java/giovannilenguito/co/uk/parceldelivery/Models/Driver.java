package giovannilenguito.co.uk.parceldelivery.Models;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Driver extends User {
    private int DriverID;
    private Parcel[] Shipments;
    private Customer[] Recipients;
    private String[] Locations;

    public Driver(int userID, String email, String username, String password, String fullName, int contactNumber, int driverID, Parcel[] shipments, Customer[] recipients, String[] locations) {
        super(userID, email, username, password, fullName, contactNumber);
        DriverID = driverID;
        Shipments = shipments;
        Recipients = recipients;
        Locations = locations;
    }

    public int getDriverID() {
        return DriverID;
    }

    public void setDriverID(int driverID) {
        DriverID = driverID;
    }

    public Parcel[] getShipments() {
        return Shipments;
    }

    public void setShipments(Parcel[] shipments) {
        Shipments = shipments;
    }

    public Customer[] getRecipients() {
        return Recipients;
    }

    public void setRecipients(Customer[] recipients) {
        Recipients = recipients;
    }

    public String[] getLocations() {
        return Locations;
    }

    public void setLocations(String[] locations) {
        Locations = locations;
    }
}
