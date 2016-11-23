package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.ClientClass;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Parser;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class UserDAOController  extends AsyncTask<Object, Object, Object> {
    protected Object doInBackground(Object... params) {
        URL url = (URL) params[0];
        String method = (String) params[1];
        String type = (String) params[2];

        if (method.equals("POST")) {
            //POST NEW USER (RETURNS RESPONSE)
            
        }else{
            //GET USER
            return Parser.JSONtoUser(ClientClass.getJSONByURL(url, 60000), type);
        }

        return null;
    }
}

