package giovannilenguito.co.uk.parceldelivery;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class ParcelDeliveryContentProvider extends ContentProvider {
    HttpURLConnection connection = null;
    Parser parser;

    public ParcelDeliveryContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        try {
            connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);

            //connect
            connection.connect();

            //get status code
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                    return 1;
                case 201:
                    return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

            out.write(String.valueOf(parser.contentValuesToJSON(values)));
            out.close();
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                    System.out.println("All okay");
                    return uri;
                case 201:
                    System.out.println("Created");
                    return uri;
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            //make the request
            connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);

            //connect
            connection.connect();

            //get status code
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                    System.out.println("All okay");
                case 201:
                    //TODO: Return cursor
                    BufferedReader buffRead = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try{
            connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setUseCaches(false);
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(String.valueOf(parser.contentValuesToJSON(values)));
            out.close();
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                    System.out.println("All okay");
                    return 1;
                case 201:
                    System.out.println("Created");
                    return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }


}
