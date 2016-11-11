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

    public Driver(String email, String username, String password, String fullName, int contactNumber, String addressLineOne, String addressLineTwo, String city, String postcode, String country) {
        super(email, username, password, fullName, contactNumber);
        this.setAddressLineOne(addressLineOne);
        this.setAddressLineTwo(addressLineTwo);
        this.setCity(city);
        this.setPostcode(postcode);
        this.setCountry(country);
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
