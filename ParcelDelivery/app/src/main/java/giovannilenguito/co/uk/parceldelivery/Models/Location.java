package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;

/**
 * Created by giovannilenguito on 24/11/2016.
 */

public class Location implements Serializable {
    private int locationId;
    private String dateTime;
    private String status;
    private double longitude;
    private double latitude;

    private boolean isDelivered;
    private boolean isOutForDelivery;
    private boolean isProcessing;
    private boolean isCollecting;

    public Location(String dateTime, String status, double longitude, double latitude, boolean isDelivered, boolean isOutForDelivery, boolean isProcessing, boolean isCollecting) {
        this.dateTime = dateTime;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isDelivered = isDelivered;
        this.isOutForDelivery = isOutForDelivery;
        this.isProcessing = isProcessing;
        this.isCollecting = isCollecting;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public boolean isCollecting() {
        return isCollecting;
    }

    public void setCollecting(boolean collecting) {
        isCollecting = collecting;
    }
}
