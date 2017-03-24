package giovannilenguito.co.uk.parceldelivery.model;

import java.io.Serializable;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Parcel implements Serializable {
    private int parcelId;
    private Customer customerId;
    private Driver driverId;
    private Address addressId;
    private Location locationId;

    private String recipientName;
    private String serviceType;
    private String contents;

    private String dateBooked;
    private String deliveryDate;


    private String image;

    private String collectionLineOne;
    private String collectionPostCode;

    public Parcel() {
    }

    public String getTitle(){
        return this.addressId.getAddressLineOne() + " (" + this.parcelId + ")";
    }

    public int getParcelId() {
        return parcelId;
    }

    public void setParcelId(int parcelId) {
        this.parcelId = parcelId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Driver getDriverId() {
        return driverId;
    }

    public void setDriverId(Driver driverId) {
        this.driverId = driverId;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(String dateBooked) {
        this.dateBooked = dateBooked;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCollectionLineOne() {
        return collectionLineOne;
    }

    public void setCollectionLineOne(String collectionLineOne) {
        this.collectionLineOne = collectionLineOne;
    }

    public String getCollectionPostCode() {
        return collectionPostCode;
    }

    public void setCollectionPostCode(String collectionPostCode) {
        this.collectionPostCode = collectionPostCode;
    }
}
