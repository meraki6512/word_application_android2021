package com.example.choisquidgame;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;


public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    private String channelId="alarm_channel";
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";
    private static int count = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Intent busRouteIntent = new Intent(context, AlarmQuizMain.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(busRouteIntent);
        PendingIntent busRoutePendingIntent = stackBuilder.getPendingIntent(count++, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle("NOTIFICATION")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("click noti and solve quiz!")
                .setFullScreenIntent(busRoutePendingIntent,true);

        final NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context,notification);
        rt.play();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int id=(int)System.currentTimeMillis();

        notificationManager.notify(id,notificationBuilder.build());

    }
}

/*
public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    private String channelId="alarm_channel";
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";
    private static int count = 0;
    private PendingIntent ServicePending;
    private AlarmManager AM;

    @Override
    public void onReceive(Context context, Intent intent) {
*/


        /*
        try {
            intent = new Intent(context, AlarmQuizMain.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            pi.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        this.context = context;
        Intent busRouteIntent = new Intent(context, AlarmQuizMain.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(busRouteIntent);
        PendingIntent busRoutePendingIntent = stackBuilder.getPendingIntent(count++, PendingIntent.FLAG_UPDATE_CURRENT);


        final NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle("알람")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("울림")
                .setFullScreenIntent(busRoutePendingIntent,true);


        Notification notification = notificationBuilder.build();
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.wakeup);
        final NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int id=(int)System.currentTimeMillis();

        notificationManager.notify(id,notificationBuilder.build());

         */

        /*
        final NotificationCompat.Builder notification_Ringring=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentTitle("알람")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("울림")
                .setFullScreenIntent(busRoutePendingIntent,true);
        final NotificationManager notificationManager_Ringring=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channelA=new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager_Ringring.createNotificationChannel(channelA);
        }

        int id_1=(int)System.currentTimeMillis();
        notificationManager_Ringring.notify(id_1,notification_Ringring.build());
         */


/*
        final NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle("알람")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("울림")
                .setFullScreenIntent(busRoutePendingIntent,true);

        Notification notification = notificationBuilder.build();

        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.wakeup);
        final NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        final NotificationCompat.Builder notification_Ringring=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentTitle("알람")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("울림")
                .setFullScreenIntent(busRoutePendingIntent,true);

        final NotificationManager notificationManager_Ringring=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int id=(int)System.currentTimeMillis();
        notificationManager.notify(id,notificationBuilder.build());

        //notificationManager.notify(id, notificationManager_Ringring.build());
 */
/*
    }
}

 */
