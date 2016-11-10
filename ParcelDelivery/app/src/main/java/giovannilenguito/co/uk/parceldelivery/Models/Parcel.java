package giovannilenguito.co.uk.parceldelivery.Models;

import java.util.Date;

/**
 * Created by Giovanni on 19/10/2016.
 */

public class Parcel {
    private int ParcelID;
    private Driver Driver;
    private String Recipient;
    private String ServiceType;
    private Date DateBooked;
    private Date DeliveryDate;


    public int getParcelID() {
        return ParcelID;
    }

    public void setParcelID(int parcelID) {
        ParcelID = parcelID;
    }

    public giovannilenguito.co.uk.parceldelivery.Models.Driver getDriver() {
        return Driver;
    }

    public void setDriver(giovannilenguito.co.uk.parceldelivery.Models.Driver driver) {
        Driver = driver;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public Date getDateBooked() {
        return DateBooked;
    }

    public void setDateBooked(Date dateBooked) {
        DateBooked = dateBooked;
    }

    public Date getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        DeliveryDate = deliveryDate;
    }
}
