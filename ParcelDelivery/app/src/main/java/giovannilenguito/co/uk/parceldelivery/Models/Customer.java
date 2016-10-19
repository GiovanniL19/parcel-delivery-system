package giovannilenguito.co.uk.parceldelivery.Models;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Customer extends User {
    private int CustomerID;
    private String AddressLineOne;
    private String AddressLineTwo;
    private String City;
    private String Postcode;
    private String Country;
    private Parcel Parcels;

    public Customer(int userID, String email, String username, String password, String fullName, int contactNumber, int customerID, String addressLineOne, String addressLineTwo, String city, String postcode, String country, Parcel parcels) {
        super(userID, email, username, password, fullName, contactNumber);
        CustomerID = customerID;
        AddressLineOne = addressLineOne;
        AddressLineTwo = addressLineTwo;
        City = city;
        Postcode = postcode;
        Country = country;
        Parcels = parcels;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getAddressLineOne() {
        return AddressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        AddressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return AddressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        AddressLineTwo = addressLineTwo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Parcel getParcels() {
        return Parcels;
    }

    public void setParcels(Parcel parcels) {
        Parcels = parcels;
    }
}
