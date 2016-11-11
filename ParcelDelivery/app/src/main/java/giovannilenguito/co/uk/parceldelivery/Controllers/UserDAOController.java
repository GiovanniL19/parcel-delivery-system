package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.URLProcessor;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class UserDAOController  extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String username = (String) params[2];
        String password = (String) params[3];

        if(params[1] == "XML"){
            return parseXML(url, username, password);
        }

        return null;
    }


    private <T> T parseXML(URL url, String username, String password) {
        try{
            Document doc = URLProcessor.getDocument(url);
            if(doc != null) {
                //get all user elements
                NodeList nodes = doc.getElementsByTagName("user");

                //loop over all nodes (users)
                for(int i = 0; i < nodes.getLength(); i++ ){
                    //get current user from iteration
                    Element user = (Element) nodes.item(i);
                    String fUsername = user.getElementsByTagName("username").item(0).getTextContent();
                    String fPassword = user.getElementsByTagName("password").item(0).getTextContent();
                    //check is username and password match
                    if(username.equals(fUsername) && password.equals(fPassword)){
                        //Create the type of object
                        String type = user.getElementsByTagName("type").item(0).getTextContent();

                        if(type.equals("Customer")){
                            //create customer object and return it
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


                            Customer customer = new Customer(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country, null);
                            customer.setId(id);

                            return (T) customer;
                        }else{
                            //create driver object and return it
                            //create customer object and return it
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


                            Driver driver = new Driver(email, username, password, fullName, contactNumber, lineOne, lineTwo, city, postcode, country);
                            driver.setId(id);

                            return (T) driver;
                        }
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

