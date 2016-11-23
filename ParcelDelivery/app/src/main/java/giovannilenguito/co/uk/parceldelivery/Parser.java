package giovannilenguito.co.uk.parceldelivery;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
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

                String dateBooked = jsonMap.get("dateBooked").toString();
                String deliveryDate = jsonMap.get("deliveryDate").toString();
                String createdByID = jsonMap.get("createdByID").toString();

                boolean isDelivered = Boolean.parseBoolean(jsonMap.get("isDelivered").toString());
                boolean isOutForDelivery = Boolean.parseBoolean(jsonMap.get("isOutForDelivery").toString());
                boolean isProcessing = Boolean.parseBoolean(jsonMap.get("isProcessing").toString());

                Parcel parcel = new Parcel(id, customerID, recipientName, serviceType, contents, dateBooked, deliveryDate, createdByID, isDelivered, isOutForDelivery, isProcessing);
                listOfParcels.add(parcel);
            }
            return listOfParcels;
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

    public static <T> T XMLtoUser(String body) {
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = builder.parse(new InputSource(new StringReader(body)));

            if(doc != null) {
                //get all user elements
                NodeList nodes = doc.getElementsByTagName("user");

                //loop over all nodes (users)
                for(int i = 0; i < nodes.getLength(); i++ ){
                    //get current user from iteration
                    Element user = (Element) nodes.item(i);
                    //Create the type of object
                    String type = user.getElementsByTagName("type").item(0).getTextContent();
                    String username = user.getElementsByTagName("username").item(0).getTextContent();
                    String password = user.getElementsByTagName("password").item(0).getTextContent();

                    String id, email, fullName, lineOne, lineTwo, city, postcode, country;
                    int contactNumber;

                    email = user.getElementsByTagName("email").item(0).getTextContent();
                    fullName = user.getElementsByTagName("fullName").item(0).getTextContent();
                    contactNumber = (Integer.parseInt(user.getElementsByTagName("contactNumber").item(0).getTextContent()));
                    id = user.getElementsByTagName("id").item(0).getTextContent();

                    Element address = (Element) user.getElementsByTagName("address").item(0);

                    lineOne = address.getElementsByTagName("lineOne").item(0).getTextContent();
                    lineTwo = address.getElementsByTagName("lineTwo").item(0).getTextContent();
                    city = address.getElementsByTagName("city").item(0).getTextContent();
                    postcode = address.getElementsByTagName("postcode").item(0).getTextContent();
                    country = address.getElementsByTagName("country").item(0).getTextContent();


                    if(type.equals("Customer")){
                        Customer customer = new Customer(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country, null);
                        customer.setId(id);

                        return (T) customer;
                    }else{
                        Driver driver = new Driver(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country);
                        driver.setId(id);

                        return (T) driver;
                    }
                }
                return null;
            }else{
                System.out.println("No Document");
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
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
}
