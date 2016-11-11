package giovannilenguito.co.uk.parceldelivery;

import org.w3c.dom.Document;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Giovanni on 11/11/2016.
 */

public class URLProcessor {
    //Attempt to generate document from URL provided
    public static Document getDocument(final URL url){
        try {
            URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
