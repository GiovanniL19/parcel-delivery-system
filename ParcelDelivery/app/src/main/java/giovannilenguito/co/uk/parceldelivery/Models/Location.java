package giovannilenguito.co.uk.parceldelivery.Models;

import java.io.Serializable;

/**
 * Created by giovannilenguito on 24/11/2016.
 */

public class Location implements Serializable {
    private String locationID;
    private String parcelID;
    private double longitude;
    private double latitude;

    public Location(String parcelID, double longitude, double latitude) {
        this.parcelID = parcelID;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
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
}
