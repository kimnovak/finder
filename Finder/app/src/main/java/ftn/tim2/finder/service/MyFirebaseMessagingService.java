package ftn.tim2.finder.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ftn.tim2.finder.MainActivity;
import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.MessageActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification(), remoteMessage.getData());
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        String title = notification.getTitle();
        String body  = notification.getBody();

        Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        intent.putExtra("MENU_FRAGMENT", data.get("FRAGMENT"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "messages")
                        .setSmallIcon(R.mipmap.ic_app_logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
