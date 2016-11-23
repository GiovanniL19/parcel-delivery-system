package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.ClientClass;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Parser;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class ParcelContentProvider extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String returnType = (String) params[3];

        switch(method){
            case "GET":
                if(returnType.equals("ARRAY")){
                    try {
                        String json = ClientClass.getJSONByURL(url, 6000);
                        return Parser.parcelList(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            case "POST":

            case "PUT":
                try {
                    return ClientClass.putParcel(url, 6000, Parser.parcelToJSON((Parcel) params[4]));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case "DELETE":

                break;
        }
        return null;
    }
}

