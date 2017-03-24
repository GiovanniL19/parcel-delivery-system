package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.DataProvider;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.ParserFactory;
import giovannilenguito.co.uk.parceldelivery.R;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class ParcelHTTPManager extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String returnType = (String) params[3];
        String mainUrl = (String) params[5];
        switch(method){
            case "GET":
                if(returnType.equals("ARRAY")){
                    try {
                        String json = DataProvider.get(url);
                        return ParserFactory.parcelList(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            case "POST":
                try {
                    //Save location, get the id and set it to location object
                    Parcel parcel = (Parcel) params[4];
                    String locationResponse = DataProvider.post(new URL(mainUrl + "/location/new"), ParserFactory.locationToJSON(parcel.getLocationId()));
                    String addressResponse = DataProvider.post(new URL(mainUrl + "/address/new"), ParserFactory.addressToJSON(parcel.getAddressId()));

                    int locationId = Integer.parseInt(locationResponse.trim().replaceAll("\n ", ""));
                    int addressId = Integer.parseInt(addressResponse.trim().replaceAll("\n ", ""));

                    parcel.getLocationId().setLocationId(locationId);
                    parcel.getAddressId().setAddressId(addressId);


                    String response = DataProvider.post(url, ParserFactory.parcelToJSON(parcel));
                    if (response == null) {
                        return false;
                    }else{
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            case "PUT":
                try {
                    String response = DataProvider.put(url, ParserFactory.parcelToJSON((Parcel) params[4]));
                    if (response == null) {
                        return false;
                    }else{
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            case "DELETE":
                Parcel parcel = (Parcel) params[4];
                try {
                    String parcelResponse = DataProvider.delete(url);
                    String locationResponse = DataProvider.delete(new URL(mainUrl + "/location/delete/" + parcel.getLocationId().getLocationId()));
                    String addressResponse = DataProvider.delete(new URL(mainUrl + "/address/delete/" + parcel.getAddressId().getAddressId()));

                    if (parcelResponse == null && locationResponse == null && addressResponse == null) {
                        return false;
                    }else{
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

        }
        return null;
    }
}

