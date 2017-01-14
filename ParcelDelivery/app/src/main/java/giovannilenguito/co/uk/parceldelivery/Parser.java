package giovannilenguito.co.uk.parceldelivery;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Location;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;

/**
 * Created by giovannilenguito on 22/11/2016.
 */

public class Parser {
    public static String buildString(BufferedReader buffRead) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = buffRead.readLine()) != null) {
            stringBuilder.append(line+"\n");
        }
        buffRead.close();

        return stringBuilder.toString();
    }

    public static List<Parcel> parcelList(String json) throws JSONException {
        if(json != null) {
            //Create json array
            List<Parcel> listOfParcels = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);

                Map jsonMap = new Gson().fromJson(object.toString(), Map.class);

                String id = jsonMap.get("id").toString();
                String customerID = jsonMap.get("customerID").toString();
                String recipientName = jsonMap.get("recipientName").toString();
                String serviceType = jsonMap.get("serviceType").toString();
                String contents = jsonMap.get("contents").toString();

                String collectionPostcode = jsonMap.get("collectionPostcode").toString();
                String collectionLineOne = jsonMap.get("collectionLineOne").toString();

                String dateBooked = jsonMap.get("dateBooked").toString();
                String deliveryDate = jsonMap.get("deliveryDate").toString();
                String driverID = jsonMap.get("driverID").toString();

                boolean isDelivered = Boolean.parseBoolean(jsonMap.get("isDelivered").toString());
                boolean isOutForDelivery = Boolean.parseBoolean(jsonMap.get("isOutForDelivery").toString());
                boolean isProcessing = Boolean.parseBoolean(jsonMap.get("isProcessing").toString());

                String image = jsonMap.get("image").toString();

                Map address = (Map) jsonMap.get("address");

                String lineOne = address.get("lineOne").toString();
                String lineTwo = address.get("lineTwo").toString();
                String city = address.get("city").toString();
                String postcode = address.get("postcode").toString();
                String country = address.get("country").toString();


                Map location = (Map) jsonMap.get("location");

                String parcelID = location.get("parcelID").toString();
                double longitude = Double.parseDouble(location.get("longitude").toString());
                double latitude = Double.parseDouble(location.get("latitude").toString());

                Location loc = new Location(parcelID, longitude, latitude);
                loc.setLocationID(location.get("locationID").toString());

                Parcel parcel = new Parcel(id, customerID, recipientName, serviceType, contents, dateBooked, deliveryDate, driverID, isDelivered, isOutForDelivery, isProcessing);
                parcel.setAddressLineOne(lineOne);
                parcel.setAddressLineTwo(lineTwo);
                parcel.setCity(city);
                parcel.setPostcode(postcode);
                parcel.setCountry(country);
                parcel.setImage(image);
                parcel.setLocation(loc);

                parcel.setCollectionLineOne(collectionLineOne);
                parcel.setCollectionPostCode(collectionPostcode);

                listOfParcels.add(parcel);
            }
            return listOfParcels;
        }
        return null;
    }

    public static List<Customer> customerList(String json) throws JSONException {
        if(json != null) {
            //Create json array
            List<Customer> listOfCustomers = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);

                Map jsonMap = new Gson().fromJson(object.toString(), Map.class);

                String id = jsonMap.get("id").toString();
                String username = jsonMap.get("username").toString();
                String password = jsonMap.get("password").toString();
                String email = jsonMap.get("email").toString();
                String fullName = jsonMap.get("fullName").toString();
                int contactNumber = Integer.parseInt(jsonMap.get("contactNumber").toString());


                Map address = (Map) jsonMap.get("address");
                String lineOne = address.get("lineOne").toString();
                String lineTwo = address.get("lineTwo").toString();
                String city = address.get("city").toString();
                String postcode = address.get("postcode").toString();
                String country = address.get("country").toString();
                Customer customer = new Customer(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country, null);
                customer.setId(id);

                listOfCustomers.add(customer);
            }
            return listOfCustomers;
        }
        return null;
    }

    public static <T> T JSONtoUser(String json, String type) {
        if(json != null) {
            Map jsonMap = new Gson().fromJson(json, Map.class);

            String id = jsonMap.get("id").toString();
            String username = jsonMap.get("username").toString();
            String password = jsonMap.get("password").toString();
            String email = jsonMap.get("email").toString();
            String fullName = jsonMap.get("fullName").toString();
            int contactNumber = Integer.parseInt(jsonMap.get("contactNumber").toString());


            Map address = (Map) jsonMap.get("address");
            String lineOne = address.get("lineOne").toString();
            String lineTwo = address.get("lineTwo").toString();
            String city = address.get("city").toString();
            String postcode = address.get("postcode").toString();
            String country = address.get("country").toString();


            if(type.equals("driver")){
                Driver driver = new Driver(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country);
                driver.setId(id);

                return (T) driver;
            }else{
                Customer customer = new Customer(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country, null);
                customer.setId(id);

                return (T) customer;
            }
        }
        return null;
    }

    public static JSONObject driverToJSON(Driver driver) throws JSONException {

        JSONObject address = new JSONObject();
        address.put("lineOne", driver.getAddressLineTwo());
        address.put("lineTwo", driver.getAddressLineTwo());
        address.put("city", driver.getCity());
        address.put("postcode", driver.getPostcode());
        address.put("country", driver.getCountry());

        JSONObject user = new JSONObject();
        user.put("id", driver.getId());
        user.put("type", driver.getType());
        user.put("username", driver.getUsername());
        user.put("password", driver.getPassword());
        user.put("email", driver.getEmail());
        user.put("fullName", driver.getFullName());
        user.put("contactNumber", driver.getContactNumber());
        user.put("address", address);


        return user;
    }

    public static JSONObject customerToJSON(Customer customer) throws JSONException {

        JSONObject address = new JSONObject();
        address.put("lineOne", customer.getAddressLineTwo());
        address.put("lineTwo", customer.getAddressLineTwo());
        address.put("city", customer.getCity());
        address.put("postcode", customer.getPostcode());
        address.put("country", customer.getCountry());

        JSONObject user = new JSONObject();
        user.put("id", customer.getId());
        user.put("type", customer.getType());
        user.put("username", customer.getUsername());
        user.put("password", customer.getPassword());
        user.put("email", customer.getEmail());
        user.put("fullName", customer.getFullName());
        user.put("contactNumber", customer.getContactNumber());
        user.put("address", address);


        return user;
    }

    public static JSONObject parcelToJSON(Parcel parcel) throws JSONException {

        JSONObject address = new JSONObject();
        address.put("lineOne", parcel.getAddressLineOne());
        address.put("lineTwo", parcel.getAddressLineTwo());
        address.put("city", parcel.getCity());
        address.put("postcode", parcel.getPostcode());
        address.put("country", parcel.getCountry());

        JSONObject location = new JSONObject();
        location.put("longitude", parcel.getLocation().getLongitude());
        location.put("latitude", parcel.getLocation().getLatitude());
        location.put("parcelID", parcel.getId());
        location.put("locationID", parcel.getLocation().getLocationID());

        JSONObject json = new JSONObject();
        json.put("id", parcel.getId());
        json.put("customerID", parcel.getCustomerID());
        json.put("recipientName", parcel.getRecipientName());
        json.put("serviceType", parcel.getServiceType());
        json.put("contents", parcel.getContents());
        json.put("dateBooked", parcel.getDateBooked());
        json.put("deliveryDate", parcel.getDeliveryDate());
        json.put("driverID", parcel.getDriverID());
        json.put("isDelivered", parcel.isDelivered());
        json.put("isOutForDelivery", parcel.isOutForDelivery());
        json.put("isProcessing", parcel.isProcessing());
        json.put("image", parcel.getImage());
        json.put("address", address);
        json.put("location", location);
        json.put("collectionLineOne", parcel.getCollectionLineOne());
        json.put("collectionPostcode", parcel.getCollectionPostCode());

        return json;
    }
}
