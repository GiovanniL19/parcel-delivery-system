package giovannilenguito.co.uk.parceldelivery;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.data.DataHolder;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Controllers.DashboardActivity;
import giovannilenguito.co.uk.parceldelivery.Controllers.MainActivity;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Models.SQLiteDatabaseController;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NotificationService extends IntentService {
    private static final String TAG = "giovannilenguito.co.uk.parceldelivery.action.notifications";
    private Boolean loop = true;

    private SQLiteDatabaseController database = new SQLiteDatabaseController(this, null, null, 0);
    private Runnable runnable;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Check for changes in number of parcels

        final Context context = this;
        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Service checking for changes in number of parcel");
                //Get local data and compare to database
                if(database.getAllDrivers().size() > 0) {
                    Driver driver = database.getAllDrivers().get(0);

                    int numberOfLocalParcles = database.getNumberOfParcels(driver.getId());

                    URL url = null;
                    try {
                        url = new URL(getString(R.string.WS_IP) + "/parcels/byDriver/" + driver.getId());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    String json = DataProvider.get(url);

                    try {
                        List<Parcel> parcelList = (List) Parser.parcelList(json);
                        int numberOfActualParcels = 0;

                        if (parcelList != null) {
                            for (Parcel parcel : parcelList) {
                                if (parcel.isOutForDelivery()) {
                                    numberOfActualParcels++;
                                } else if (parcel.isProcessing()) {
                                    numberOfActualParcels++;
                                }
                            }

                            //Update local storage
                            database.updateNumberOfParcels(numberOfActualParcels, driver.getId());
                        }

                        if (numberOfLocalParcles != numberOfActualParcels) {
                            //Update view via local broadcaster
                            Intent intent = new Intent("updateUI");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }

                        if (numberOfLocalParcles < numberOfActualParcels) {
                            //Send notification
                            System.out.println("Sending notification");
                            Notification notification = new Notification();
                            notification.create(context, "You have new parcels awaiting collection", "New Parcels");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    stopSelf();
                }
            }
        };

        while(loop){
            try {
                Thread.sleep(5000);
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        loop = false;
        stopSelf();
        System.out.println("Service complete");
    }

}
