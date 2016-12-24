package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.DataProvider;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Parser;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class ParcelHTTPManager extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String returnType = (String) params[3];

        switch(method){
            case "GET":
                if(returnType.equals("ARRAY")){
                    try {
                        String json = DataProvider.get(url, 6000);
                        return Parser.parcelList(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            case "POST":
                try {
                    String response = DataProvider.post(url, 6000, Parser.parcelToJSON((Parcel) params[4]));
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
                    String response = DataProvider.put(url, 6000, Parser.parcelToJSON((Parcel) params[4]));
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
                String response = DataProvider.delete(url, 6000);
                if (response == null) {
                    return false;
                }else{
                    return true;
                }
        }
        return null;
    }
}

