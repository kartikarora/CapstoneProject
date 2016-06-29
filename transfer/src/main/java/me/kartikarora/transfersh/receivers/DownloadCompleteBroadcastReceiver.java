package me.kartikarora.transfersh.receivers;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import me.kartikarora.transfersh.BuildConfig;
import me.kartikarora.transfersh.R;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.receivers
 * Project : ProjectSevenEight
 * Date : 29/6/16
 */
public class DownloadCompleteBroadcastReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = BuildConfig.VERSION_CODE;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.download_complete))
                .setSmallIcon(R.drawable.ic_offline_pin)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTicker(context.getString(R.string.download_complete))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
