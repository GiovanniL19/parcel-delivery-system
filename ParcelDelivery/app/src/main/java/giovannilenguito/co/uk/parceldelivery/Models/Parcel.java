package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Parcel extends Address implements Serializable{
    private int id; //used for SQL Lite

    private int driverID;
    private String recipientName;
    private String serviceType;
    private String contents;

    private String dateBooked;
    private String deliveryDate;
    private int createdByID;

    private boolean isDelivered;
    private boolean isOutForDelivery;
    private boolean isProcessing;

    public String getStatus(){
        if(isDelivered){
            return "Delivered";
        }else if(isOutForDelivery){
            return "Out For Delivery";
        }else if(isProcessing){
            return "Processing";
        }else{
            return "Pending";
        }
    }

    public String getTitle(){
        return this.id + " - " + this.recipientName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public int getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(int createdByID) {
        this.createdByID = createdByID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
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
}
