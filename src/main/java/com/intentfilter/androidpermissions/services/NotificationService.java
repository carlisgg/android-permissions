package com.intentfilter.androidpermissions.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationService {

    private static final String CHANNEL_ID = "android-permissions";
    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationService(Context context) {
        this(context, (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE));
    }

    private NotificationService(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "Permission request notification channel",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public Notification buildNotification(String title, String message, Intent intent, PendingIntent deleteIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, message.hashCode(), intent, FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentIntent(pendingIntent);

        notificationBuilder.setDeleteIntent(deleteIntent);

        NotificationCompat.BigTextStyle bigTextNotification = new NotificationCompat.BigTextStyle(notificationBuilder)
                .bigText(message)
                .setBigContentTitle(title);

        return bigTextNotification.build();
    }

    public void notify(String tag, int id, Notification notification) {
        notificationManager.notify(tag, id, notification);
    }
}