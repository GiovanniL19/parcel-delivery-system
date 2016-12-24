package giovannilenguito.co.uk.parceldelivery;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by giovannilenguito on 22/11/2016.
 * Class controls of operations including GET, POST, PUT, AND DELETE
 * DataProvider contains operations for PARCEL, CUSTOMER, AND DRIVER
 */

public class DataProvider {

    public static String delete(URL url, int timeout) {
        HttpURLConnection connection = null;
        try {
            //make the request
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            //connect
            connection.connect();

            //get status code
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                    System.out.println("All okay");
                case 201:
                    BufferedReader buffRead = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    return Parser.buildString(buffRead);
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

    public static String put(URL url, int timeout, JSONObject JSONUser) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setUseCaches(false);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.connect();

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(String.valueOf(JSONUser));
        out.close();
        int statusCode = connection.getResponseCode();

        BufferedReader buffRead = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        switch (statusCode) {
            case 200:
                System.out.println("All okay");
                return Parser.buildString(buffRead);
            case 201:
                System.out.println("Created");
                return Parser.buildString(buffRead);
        }

        return null;
    }

    public static String post(URL url, int timeout, JSONObject JSONUser) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.connect();

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(String.valueOf(JSONUser));
        out.close();
        int statusCode = connection.getResponseCode();

        BufferedReader buffRead = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        switch (statusCode) {
            case 200:
                System.out.println("All okay");
                return Parser.buildString(buffRead);
            case 201:
                System.out.println("Created");
                return Parser.buildString(buffRead);
        }

        return null;
    }

    public static String get(URL url, int timeout) {
        HttpURLConnection connection = null;
        try {
            //make the request
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            //connect
            connection.connect();

            //get status code
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                    System.out.println("All okay");
                case 201:
                    BufferedReader buffRead = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    return Parser.buildString(buffRead);
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
}
