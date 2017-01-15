package giovannilenguito.co.uk.parceldelivery;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Models.SQLiteDatabaseController;

public class NotificationService extends Service {
    private Boolean loop = true;

    private SQLiteDatabaseController database = new SQLiteDatabaseController(this, null, null, 0);

    private Looper serviceLooper;
    private HandleService handlerService;


    private final class HandleService extends Handler {
        public HandleService(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println("Here");
            System.out.println(msg);
            Context context = getApplicationContext();
            while(loop){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        handlerService = new HandleService(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Starting service");

        Message msg = handlerService.obtainMessage();
        msg.arg1 = startId;
        handlerService.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        loop = false;
        stopSelf();
        System.out.println("Service complete");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
