package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.os.AsyncTask;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.ClientClass;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Parser;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class UserContentProvider extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String userType = (String) params[2];

        switch(method){
            case "GET":
                return Parser.JSONtoUser(ClientClass.get(url, 60000), userType);
            case "GETALL":
                if(userType.equals("customer")) {
                    try {
                        return Parser.customerList(ClientClass.get(url, 60000));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case "POST":
                if(userType.equals("customer")){
                    try {
                        return ClientClass.post(url, 60000, Parser.customerToJSON((Customer) params[3]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        return ClientClass.post(url, 60000, Parser.driverToJSON((Driver) params[3]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case "PUT":

                break;
            case "DELETE":

                break;
            case "LOG":
                try {
                    ClientClass.post(url, 6000, (JSONObject) params[3]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        return null;
    }
}

