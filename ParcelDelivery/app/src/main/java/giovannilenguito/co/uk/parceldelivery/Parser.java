package giovannilenguito.co.uk.parceldelivery;

import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;

/**
 * Created by giovannilenguito on 22/11/2016.
 */

public class Parser {
    public static <T> T JSONtoUser(String json, String type) {
        if(json != null) {
            Map jsonMap = new Gson().fromJson(json, Map.class);

            int id = Integer.parseInt(jsonMap.get("id").toString());
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

                    String email, fullName, lineOne, lineTwo, city, postcode, country;
                    int contactNumber, id;

                    email = user.getElementsByTagName("email").item(0).getTextContent();
                    fullName = user.getElementsByTagName("fullName").item(0).getTextContent();
                    contactNumber = (Integer.parseInt(user.getElementsByTagName("contactNumber").item(0).getTextContent()));
                    id = (Integer.parseInt(user.getElementsByTagName("id").item(0).getTextContent()));

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
}
