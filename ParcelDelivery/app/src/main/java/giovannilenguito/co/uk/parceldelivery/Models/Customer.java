package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Customer extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String postcode;
    private String country;
    private Parcel parcels;

    public Customer(String email, String username, String password, String fullName, int contactNumber, String addressLineOne, String addressLineTwo, String city, String postcode, String country, Parcel parcels) {
        super(email, username, password, fullName, contactNumber);
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
        this.parcels = parcels;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Parcel getParcels() {
        return parcels;
    }

    public void setParcels(Parcel parcels) {
        this.parcels = parcels;
    }
}
