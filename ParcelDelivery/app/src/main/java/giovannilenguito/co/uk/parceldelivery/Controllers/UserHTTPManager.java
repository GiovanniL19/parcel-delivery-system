package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.DataProvider;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Parser;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class UserHTTPManager extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String userType = (String) params[2];

        switch(method){
            case "GET":
                if(userType.equals("customers")) {
                    try {
                        return Parser.customerList(DataProvider.get(url));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    return Parser.JSONtoUser(DataProvider.get(url), userType);
                }
            case "POST":
                if(userType.equals("customer")){
                    try {
                        return DataProvider.post(url, Parser.customerToJSON((Customer) params[3]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        return DataProvider.post(url, Parser.driverToJSON((Driver) params[3]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case "LOG":
                try {
                    DataProvider.post(url, (JSONObject) params[3]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        return null;
    }
}

