package giovannilenguito.co.uk.parceldelivery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by giovannilenguito on 22/11/2016.
 */

public class ClientClass {

    public static String getJSONByURL(URL url, int timeout) {
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
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = buffRead.readLine()) != null) {
                        stringBuilder.append(line+"\n");
                    }
                    buffRead.close();

                    return stringBuilder.toString();
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
    /*FOR USE WITH SOAP
    public static String result(URL urlWSDL, String xmlBody) {
        URL url = null;
        try {
            url = urlWSDL;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                //Set up connection
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "text/xml, application/xml");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Set the body content
                String body = xmlBody;

                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(body.getBytes());
                output.flush();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println("successful request");


                    //Create response
                    InputStream inputStream = connection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    //returns the response
                    //decode
                    return stringBuilder.toString();

                } else {
                    System.out.println("bad request " + connection.getResponseCode());
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}
