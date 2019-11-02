package com.example.customermain;

import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static void displayNotificatio(Context context, String title, String body) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNLE_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Log.d("notific",title);
        notificationManagerCompat.notify(1, mBuilder.build());
    }


}
