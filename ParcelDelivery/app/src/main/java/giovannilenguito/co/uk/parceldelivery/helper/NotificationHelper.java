package giovannilenguito.co.uk.parceldelivery.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import giovannilenguito.co.uk.parceldelivery.R;
import giovannilenguito.co.uk.parceldelivery.controller.MainActivity;
/**
 * Created by giovannilenguito on 15/01/2017.
 */

public class NotificationHelper {
    public void create(Context context, String message, String title){
        //Create notification
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        notificationBuilder.setSmallIcon(android.R.drawable.ic_menu_send);

        notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSound(sound);
        notificationBuilder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create intent to make notification clickable
        Intent resultIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent );

        //Random number for id
        int id = 1 + (int) (Math.random() * ((100 - 1) + 1));

        notificationManager.notify(id, notificationBuilder.build());
    }
}
