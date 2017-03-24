package giovannilenguito.co.uk.parceldelivery.handler;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.provider.DataProvider;
import giovannilenguito.co.uk.parceldelivery.model.Customer;
import giovannilenguito.co.uk.parceldelivery.model.Driver;
import giovannilenguito.co.uk.parceldelivery.factory.ParserFactory;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class UserHTTPHandler extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String userType = (String) params[2];
        String mainUrl = (String) params[4];

        switch(method){
            case "GET":
                if(userType.equals("customers")) {
                    try {
                        return ParserFactory.customerList(DataProvider.get(url));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    return ParserFactory.JSONtoUser(DataProvider.get(url), userType);
                }
            case "POST":
                if(userType.equals("customer")){
                    try {
                        Customer customer = (Customer) params[3];
                        String addressResponse = DataProvider.post(new URL(mainUrl + "/address/new"), ParserFactory.addressToJSON(customer.getAddressId()));

                        int addressId = Integer.parseInt(addressResponse.trim().replaceAll("\n ", ""));
                        customer.getAddressId().setAddressId(addressId);

                        return DataProvider.post(url, ParserFactory.customerToJSON(customer));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        return DataProvider.post(url, ParserFactory.driverToJSON((Driver) params[3]));
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

