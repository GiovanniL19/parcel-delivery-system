package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Parcel extends Address implements Serializable {
    private String id;

    private String customerID;
    private String recipientName;
    private String serviceType;
    private String contents;

    private String dateBooked;
    private String deliveryDate;

    private String createdByID; //is also the drive id

    private boolean isDelivered;
    private boolean isOutForDelivery;
    private boolean isProcessing;

    private String image;

    private Location location;

    public String getStatus() {
        if (isDelivered) {
            return "Delivered";
        } else if (isOutForDelivery) {
            return "Out For Delivery";
        } else if (isProcessing) {
            return "Processing";
        } else {
            return "Pending";
        }
    }

    public Parcel() {
    }

    public Parcel(String id, String customerID, String recipientName, String serviceType, String contents, String dateBooked, String deliveryDate, String createdByID, boolean isDelivered, boolean isOutForDelivery, boolean isProcessing) {
        this.id = id;
        this.customerID = customerID;
        this.recipientName = recipientName;
        this.serviceType = serviceType;
        this.contents = contents;
        this.dateBooked = dateBooked;
        this.deliveryDate = deliveryDate;
        this.createdByID = createdByID;
        this.isDelivered = isDelivered;
        this.isOutForDelivery = isOutForDelivery;
        this.isProcessing = isProcessing;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getTitle() {
        return this.id + " - " + this.recipientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(String createdByID) {
        this.createdByID = createdByID;
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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(String dateBooked) {
        this.dateBooked = dateBooked;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public boolean isOutForDelivery() {
        return isOutForDelivery;
    }

    public void setOutForDelivery(boolean outForDelivery) {
        isOutForDelivery = outForDelivery;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
