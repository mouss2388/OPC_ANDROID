package com.example.projet_7.notification;


import static com.example.projet_7.ui.MainActivity.restaurantBookedInfo;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.projet_7.R;
import com.example.projet_7.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("InlinedApi")

public class NotificationsService extends FirebaseMessagingService {

    private static final String TAG = "TAG";


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            // Get message sent by Firebase
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            launchNotification(notification);

        }
    }

    private void launchNotification(RemoteMessage.Notification notification) {

        if (restaurantBookedInfo.length() > 0) {

            Log.i(TAG, restaurantBookedInfo.toString());
            // Create an Intent that will be shown when user will click on the Notification
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Create a Channel (Android 8)
            String channelId = getString(R.string.default_notification_channel_id);


            // Build a Notification object
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.bowl_logo_menu)
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getBody())
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(restaurantBookedInfo))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Support Version >= Android 8
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence channelName = "Firebase Messages";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }


            // Show notification
            int NOTIFICATION_ID = 7;
            String NOTIFICATION_TAG = "FIREBASEOC";
            notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());

        }
    }

}