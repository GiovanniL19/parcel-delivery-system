package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Customer extends User implements Serializable {

    private Parcel[] parcels;
    private String type = "Customer";

    public String getType() {
        return type;
    }

    public Customer(String email, String username, String password, String fullName, int contactNumber, String addressLineOne, String addressLineTwo, String city, String postcode, String country, Parcel[] parcels) {
        super(email, username, password, fullName, contactNumber);
        this.setAddressLineOne(addressLineOne);
        this.setAddressLineTwo(addressLineTwo);
        this.setCity(city);
        this.setPostcode(postcode);
        this.setCountry(country);
        this.parcels = parcels;
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
}
