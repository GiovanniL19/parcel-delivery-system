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


import giovannilenguito.co.uk.parceldelivery.Models.Address;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Location;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;

/**
 * Created by giovannilenguito on 22/11/2016.
 */

public class ParserFactory {

    public static int getId(String item){
        return Integer.parseInt(item.indexOf(".") < 0 ? item : item.replaceAll("0*$", "").replaceAll("\\.$", ""));
    }

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

                String recipientName = jsonMap.get("recipientName").toString();
                String serviceType = jsonMap.get("serviceType").toString();
                String contents = jsonMap.get("contents").toString();
                String collectionPostcode = jsonMap.get("collectionPostcode").toString();
                String collectionLineOne = jsonMap.get("collectionLineOne").toString();
                String dateBooked = jsonMap.get("dateBooked").toString();
                String deliveryDate = jsonMap.get("deliveryDate").toString();
                String image = jsonMap.get("image").toString();


                Map addressObject = (Map) jsonMap.get("addressId");
                String lineOne = addressObject.get("lineOne").toString();
                String lineTwo = addressObject.get("lineTwo").toString();
                String city = addressObject.get("city").toString();
                String postcode = addressObject.get("postcode").toString();
                String country = addressObject.get("country").toString();
                int addressId =  getId(addressObject.get("addressId").toString());

                Address address = new Address();
                address.setAddressLineOne(lineOne);
                address.setAddressLineTwo(lineTwo);
                address.setCity(city);
                address.setPostcode(postcode);
                address.setPostcode(country);
                address.setAddressId(addressId);


                Map location = (Map) jsonMap.get("locationId");
                int locationId = getId(location.get("locationId").toString());
                int parcelId = getId(location.get("locationId").toString());
                String dateTime = location.get("dateTime").toString();
                String status = location.get("status").toString();
                double longitude = Double.parseDouble(location.get("longitude").toString());
                double latitude = Double.parseDouble(location.get("latitude").toString());
                boolean isDelivered = Boolean.parseBoolean(location.get("isDelivered").toString());
                boolean isOutForDelivery = Boolean.parseBoolean(location.get("isOutForDelivery").toString());
                boolean isProcessing = Boolean.parseBoolean(location.get("isProcessing").toString());
                boolean isCollecting = Boolean.parseBoolean(location.get("isCollecting").toString());

                Location locationObject = new Location(dateTime, status, longitude, latitude, isDelivered, isOutForDelivery, isProcessing, isCollecting);
                locationObject.setLocationId(locationId);

                Map customerObject = (Map) jsonMap.get("customerId");
                int customerId = getId(customerObject.get("email").toString());
                String customerEmail = customerObject.get("customerId").toString();
                Long customerContactNumber = Long.parseLong(customerObject.get("contactNumber").toString());
                String customerUsername = customerObject.get("username").toString();
                String customerPassword = customerObject.get("password").toString();
                String customerFullName = customerObject.get("fullName").toString();

                Map customerAddressObject = (Map) customerObject.get("addressId");
                String customerLineOne = customerAddressObject.get("lineOne").toString();
                String customerLineTwo = customerAddressObject.get("lineTwo").toString();
                String customerCity = customerAddressObject.get("city").toString();
                String customerPostcode = customerAddressObject.get("postcode").toString();
                String customerCountry = customerAddressObject.get("country").toString();
                int customerAddressId = getId(customerAddressObject.get("addressId").toString());

                Address customerAddress = new Address();
                customerAddress.setAddressLineOne(customerLineOne);
                customerAddress.setAddressLineTwo(customerLineTwo);
                customerAddress.setCity(customerCity);
                customerAddress.setPostcode(customerPostcode);
                customerAddress.setCountry(customerCountry);
                customerAddress.setAddressId(customerAddressId);

                Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerFullName, customerContactNumber, customerAddress);
                customer.setCustomerId(customerId);


                Map driverObject = (Map) jsonMap.get("driverId");
                int driverId = getId(driverObject.get("driverId").toString());
                String driverEmail = driverObject.get("email").toString();
                Long driverContactNumber = Long.parseLong(driverObject.get("contactNumber").toString());
                String driverUsername = driverObject.get("username").toString();
                String driverPassword = driverObject.get("password").toString();
                String driverFullName = driverObject.get("fullName").toString();

                Driver driver = new Driver(driverEmail, driverUsername, driverPassword, driverFullName, driverContactNumber);
                driver.setDriverId(driverId);


                Parcel parcel = new Parcel();
                parcel.setParcelId(parcelId);
                parcel.setCustomerId(customer);
                parcel.setAddressId(address);
                parcel.setDriverId(driver);

                parcel.setCollectionLineOne(collectionLineOne);
                parcel.setCollectionPostCode(collectionPostcode);
                parcel.setLocationId(locationObject);
                parcel.setImage(image);
                parcel.setDateBooked(dateBooked);
                parcel.setDeliveryDate(deliveryDate);
                parcel.setRecipientName(recipientName);
                parcel.setServiceType(serviceType);
                parcel.setContents(contents);

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

                int customerId = getId(jsonMap.get("customerId").toString());
                String username = jsonMap.get("username").toString();
                String password = jsonMap.get("password").toString();
                String email = jsonMap.get("email").toString();
                String fullName = jsonMap.get("fullName").toString();
                Long contactNumber = Long.parseLong(jsonMap.get("contactNumber").toString());


                Map addressObject = (Map) jsonMap.get("addressId");
                int addressId = getId(addressObject.get("addressId").toString());
                String lineOne = addressObject.get("lineOne").toString();
                String lineTwo = addressObject.get("lineTwo").toString();
                String city = addressObject.get("city").toString();
                String postcode = addressObject.get("postcode").toString();
                String country = addressObject.get("country").toString();

                Address address = new Address();
                address.setAddressLineOne(lineOne);
                address.setAddressLineTwo(lineTwo);
                address.setCity(city);
                address.setPostcode(postcode);
                address.setCountry(country);
                address.setAddressId(addressId);

                Customer customer = new Customer(email, username, password, fullName, contactNumber, address);
                customer.setCustomerId(customerId);

                listOfCustomers.add(customer);
            }
            return listOfCustomers;
        }
        return null;
    }

    public static <T> T JSONtoUser(String json, String type) {
        if(json != null) {
            Map jsonMap = new Gson().fromJson(json, Map.class);

            String username, password, email, fullName, lineOne, lineTwo, city, postcode, country;
            int id, addressId;

            Long contactNumber;
            Map address;


            username = jsonMap.get("username").toString();
            password = jsonMap.get("password").toString();
            email = jsonMap.get("email").toString();
            fullName = jsonMap.get("fullName").toString();


            contactNumber = Long.parseLong(jsonMap.get("contactNumber").toString());

            if(type.equals("driver")){
                id = getId(jsonMap.get("driverId").toString());

                Driver driver = new Driver(email, username, password, fullName, contactNumber);
                driver.setDriverId(id);

                return (T) driver;
            }else{
                id = getId(jsonMap.get("customerId").toString());


                address = (Map) jsonMap.get("addressId");
                addressId = getId(address.get("addressId").toString());
                lineOne = address.get("lineOne").toString();
                lineTwo = address.get("lineTwo").toString();
                city = address.get("city").toString();
                postcode = address.get("postcode").toString();
                country = address.get("country").toString();

                Address addressIdObject = new Address();
                addressIdObject.setAddressLineOne(lineOne);
                addressIdObject.setAddressLineTwo(lineTwo);
                addressIdObject.setCity(city);
                addressIdObject.setPostcode(postcode);
                addressIdObject.setCountry(country);
                addressIdObject.setAddressId(addressId);

                Customer customer = new Customer(email, username, password, fullName, contactNumber, addressIdObject);
                customer.setCustomerId(id);

                return (T) customer;
            }
        }
        return null;
    }

    public static JSONObject driverToJSON(Driver driver) throws JSONException {
        JSONObject user = new JSONObject();
        user.put("driverId", driver.getDriverId());
        user.put("username", driver.getUsername());
        user.put("password", driver.getPassword());
        user.put("email", driver.getEmail());
        user.put("fullName", driver.getFullName());
        user.put("contactNumber", driver.getContactNumber());

        return user;
    }

    public static JSONObject customerToJSON(Customer customer) throws JSONException {

        JSONObject address = new JSONObject();
        address.put("lineOne", customer.getAddressId().getAddressLineOne());
        address.put("lineTwo", customer.getAddressId().getAddressLineTwo());
        address.put("city", customer.getAddressId().getCity());
        address.put("postcode", customer.getAddressId().getPostcode());
        address.put("country", customer.getAddressId().getCountry());

        JSONObject user = new JSONObject();
        user.put("customerId", customer.getCustomerId());
        user.put("username", customer.getUsername());
        user.put("password", customer.getPassword());
        user.put("email", customer.getEmail());
        user.put("fullName", customer.getFullName());
        user.put("contactNumber", customer.getContactNumber());
        user.put("addressId", address);

        return user;
    }

    public static JSONObject parcelToJSON(Parcel parcel) throws JSONException {

        //Create address object
        JSONObject address = new JSONObject();
        address.put("lineOne", parcel.getAddressId().getAddressLineOne());
        address.put("lineTwo", parcel.getAddressId().getAddressLineTwo());
        address.put("city", parcel.getAddressId().getCity());
        address.put("postcode", parcel.getAddressId().getPostcode());
        address.put("country", parcel.getAddressId().getCountry());
        address.put("addressId", parcel.getAddressId().getAddressId());


        //Create location object
        JSONObject location = new JSONObject();
        location.put("longitude", parcel.getLocationId().getLongitude());
        location.put("latitude", parcel.getLocationId().getLatitude());
        location.put("isDelivered", parcel.getLocationId().isDelivered());
        location.put("isOutForDelivery", parcel.getLocationId().isOutForDelivery());
        location.put("isCollecting", parcel.getLocationId().isCollecting());
        location.put("isProcessing", parcel.getLocationId().isProcessing());
        location.put("parcelId", parcel.getParcelId());
        location.put("locationId", parcel.getLocationId().getLocationId());

        //Create customer object
        JSONObject customer = new JSONObject();
        customer.put("customerId", parcel.getCustomerId().getCustomerId());
        customer.put("email", parcel.getCustomerId().getEmail());
        customer.put("contactNumber", parcel.getCustomerId().getContactNumber());
        customer.put("username", parcel.getCustomerId().getUsername());
        customer.put("password", parcel.getCustomerId().getPassword());
        customer.put("fullName", parcel.getCustomerId().getFullName());

        //Create customer address
        JSONObject customerAddress = new JSONObject();
        customerAddress.put("lineOne", parcel.getCustomerId().getAddressId().getAddressLineOne());
        customerAddress.put("lineTwo", parcel.getCustomerId().getAddressId().getAddressLineTwo());
        customerAddress.put("city", parcel.getCustomerId().getAddressId().getCity());
        customerAddress.put("postcode", parcel.getCustomerId().getAddressId().getPostcode());
        customerAddress.put("country", parcel.getCustomerId().getAddressId().getCountry());
        customerAddress.put("addressId", parcel.getCustomerId().getAddressId().getAddressId());

        customer.put("addressId", customerAddress);


        //Create customer object
        JSONObject driver = new JSONObject();
        driver.put("driverId", parcel.getDriverId().getDriverId());
        driver.put("email", parcel.getDriverId().getEmail());
        driver.put("contactNumber", parcel.getDriverId().getContactNumber());
        driver.put("username", parcel.getDriverId().getUsername());
        driver.put("password", parcel.getDriverId().getPassword());
        driver.put("fullName", parcel.getDriverId().getFullName());

        //Create JSON to send to web service
        JSONObject json = new JSONObject();
        json.put("parcelId", parcel.getParcelId());
        json.put("customerId", customer);
        json.put("addressId", address);
        json.put("locationId", location);
        json.put("driverId", driver);

        json.put("recipientName", parcel.getRecipientName());
        json.put("serviceType", parcel.getServiceType());
        json.put("contents", parcel.getContents());
        json.put("dateBooked", parcel.getDateBooked());
        json.put("deliveryDate", parcel.getDeliveryDate());
        json.put("isDelivered", parcel.getLocationId().isDelivered());
        json.put("isOutForDelivery", parcel.getLocationId().isOutForDelivery());
        json.put("isProcessing", parcel.getLocationId().isProcessing());
        json.put("image", parcel.getImage());
        json.put("collectionLineOne", parcel.getCollectionLineOne());
        json.put("collectionPostcode", parcel.getCollectionPostCode());

        return json;
    }

    public static JSONObject locationToJSON(Location location) throws JSONException{

        JSONObject locationObject = new JSONObject();
        locationObject.put("dateTime", location.getDateTime());
        locationObject.put("status", location.getStatus());
        locationObject.put("longitude", location.getLongitude());
        locationObject.put("latitude", location.getLatitude());
        locationObject.put("isDelivered", location.isDelivered());
        locationObject.put("isOutForDelivery", location.isOutForDelivery());
        locationObject.put("isProcessing", location.isProcessing());
        locationObject.put("isCollecting", location.isCollecting());

        return locationObject;
    }
}
