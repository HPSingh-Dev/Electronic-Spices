package com.conductor.apni.littleleappwa;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "checkLogFcm";

    @Override
    public void onNewToken(@NonNull String s) {
//        Toast.makeText(this, "Token: " + s , Toast.LENGTH_LONG).show();
        Log.d("NEW_TOKEN",s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

//        String title = "Little Leap";
//        String body = "Notification Content";
//        String intentUri = "https://littleleap.co.in/technology.php";
//        String intentID = "0";
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
////            if (/* Check if data needs to be processed by long running job */ true)
////                // For long-running tasks (10 seconds or more) use WorkManager.
////                scheduleJob();
////            else
////                // Handle message within 10 seconds
////                handleNow();
//            intentUri = remoteMessage.getData().get("link");
//            intentID = remoteMessage.getData().get("id");
//        }
//
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification: " + remoteMessage.getNotification().getTitle() + " : " + remoteMessage.getNotification().getBody());
//            title = remoteMessage.getNotification().getTitle();
//            body = remoteMessage.getNotification().getBody();
////            String click_action=remoteMessage.getNotification().getClickAction();
//            sendNotification(intentUri , title , body, intentID);
//        }
    }

    private void sendNotification(String url, String title, String body, String intentID){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (url != null) {
            intent.putExtra("URL", url);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_MUTABLE);

        String channelId = getString(R.string.CHANNEL_ID);
        String channelName = getString(R.string.CHANNEL_NAME);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
//        notificationManager.notify(Integer.parseInt(intentID) , notificationBuilder.build());
        notificationManager.notify(random , notificationBuilder.build());
    }
}
