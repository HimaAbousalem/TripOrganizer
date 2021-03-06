package com.iti.mobile.triporganizer.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcel;

import androidx.core.app.NotificationCompat;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.alarm.AlarmBroadCastReceiver;
import com.iti.mobile.triporganizer.data.entities.Trip;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class NotificationsUtils {
    private static final String channelName ="tripsChannel";
    private static final String notificationDescription ="shows notification on trip due date";
    private static final String channelId = "tripsNotification";
    private static final String notificationTitle = "you have a trip coming";
    private static final String notificationTitleForStartedTrip = "trip in progress";
    public static final String Action_Snooze = "snooze";
    public static final String Action_Start = "start";
    public static final String Action_End = "end";
    public static final String Action_Cancel = "cancel";
    private static final int notificationId = 1;
    public static Notification makeStatusNotification(String message, Context context, Trip trip) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(notificationDescription);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notificationTitle)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_arrow_bk_white_48dp, context.getResources().getString(R.string.start),
                        createStartIntent(context, trip))
                .addAction(R.drawable.ic_arrow_bk_white_48dp, context.getResources().getString(R.string.snooze),
                        createSnoozeIntent(context, trip))
                .addAction(R.drawable.ic_arrow_bk_white_48dp, context.getResources().getString(R.string.cancel),
                        createCancelIntent(context, trip))

                .setVibrate(new long[0]);

       // NotificationManagerCompat.from(context).notify(notificationId, builder.build());
        return builder.build();
    }
    public static Notification makeStatusNotificationForStartedTrip(String message, Context context, Trip trip) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(notificationDescription);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notificationTitleForStartedTrip)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_arrow_bk_white_48dp, context.getResources().getString(R.string.end),
                        createEndIntent(context, trip))
                .addAction(R.drawable.ic_arrow_bk_white_48dp, context.getResources().getString(R.string.cancel),
                        createCancelIntent(context, trip))

                .setVibrate(new long[0]);

        // NotificationManagerCompat.from(context).notify(notificationId, builder.build());
        return builder.build();
    }
    private static PendingIntent createSnoozeIntent(Context context, Trip trip){
        Intent snoozeIntent = new Intent(context, AlarmBroadCastReceiver.class);
        long tripId = trip.getId();
        Parcel parcel = Parcel.obtain();
        trip.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        snoozeIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());
        snoozeIntent.setAction(Action_Snooze);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        }
        return PendingIntent.getBroadcast(context, (int) tripId, snoozeIntent, 0);
    }
    private static PendingIntent createStartIntent(Context context, Trip trip){
        Intent startIntent = new Intent(context, AlarmBroadCastReceiver.class);
        startIntent.setAction(Action_Start);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        }
        long tripId = trip.getId();
        Parcel parcel = Parcel.obtain();
        trip.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        startIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());

        return PendingIntent.getBroadcast(context, (int) tripId, startIntent, 0);
    }
    private static PendingIntent createEndIntent(Context context, Trip trip){
        Intent startIntent = new Intent(context, AlarmBroadCastReceiver.class);
        startIntent.setAction(Action_End);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);

        }
        long tripId = trip.getId();
        Parcel parcel = Parcel.obtain();
        trip.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        startIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());
        return PendingIntent.getBroadcast(context, (int) tripId, startIntent, 0);
    }
    private static PendingIntent createCancelIntent(Context context, Trip trip){
        Intent cancelIntent = new Intent(context, AlarmBroadCastReceiver.class);
        cancelIntent.setAction(Action_Cancel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cancelIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        }
        long tripId = trip.getId();
        Parcel parcel = Parcel.obtain();
        trip.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        cancelIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());
        cancelIntent.putExtra("tripId", tripId);
        return PendingIntent.getBroadcast(context, (int) tripId, cancelIntent, 0);
    }
}
