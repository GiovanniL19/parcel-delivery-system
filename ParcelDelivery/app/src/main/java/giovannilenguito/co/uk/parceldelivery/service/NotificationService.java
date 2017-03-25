package giovannilenguito.co.uk.parceldelivery.service;

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

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.handler.SQLiteDatabaseHandler;
import giovannilenguito.co.uk.parceldelivery.provider.DataProvider;
import giovannilenguito.co.uk.parceldelivery.factory.ParserFactory;
import giovannilenguito.co.uk.parceldelivery.R;
import giovannilenguito.co.uk.parceldelivery.model.Driver;
import giovannilenguito.co.uk.parceldelivery.model.Parcel;


public class NotificationService extends Service {
    private Boolean loop = true;

    private SQLiteDatabaseHandler database = new SQLiteDatabaseHandler(this, null, null, 0);

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

                    int numberOfLocalParcles = database.getNumberOfParcels(driver.getDriverId());

                    URL url = null;
                    try {
                        url = new URL(getString(R.string.WS_IP) + "/parcel/findByDriver/" + driver.getDriverId());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    String json = DataProvider.get(url);

                    try {
                        List<Parcel> parcelList = (List) ParserFactory.parcelList(json);
                        int numberOfActualParcels = 0;

                        if (parcelList != null) {
                            for (Parcel parcel : parcelList) {
                                if (parcel.getLocationId().isOutForDelivery()) {
                                    numberOfActualParcels++;
                                } else if (parcel.getLocationId().isProcessing()) {
                                    numberOfActualParcels++;
                                }
                            }

                            //Update local storage
                            database.updateNumberOfParcels(numberOfActualParcels, driver.getDriverId());
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