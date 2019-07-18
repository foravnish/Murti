package com.gcm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.foodapp.murti.Activity.MainActivity;
import com.foodapp.murti.Activity.Navigation;
import com.foodapp.murti.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


/**
 * Created by Andriod Avnish on 06-Mar-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    //  private NotificationUtils notificationUtils;

    Bitmap image,bitmap;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        //Log.e(TAG, "Fromgdfhgdfghfgjf: " + remoteMessage.getMessageType());
//        Log.e(TAG, "Fromgdfhgdfghfgjf: " + remoteMessage.getData());
////        Log.e(TAG, "Fromgdfhgdfghfgjf: " + remoteMessage.getNotification().getBody());
//
//
//        String data= String.valueOf(remoteMessage.getData());
//        String org=data.replace("message=","");
//
//        //org.substring(1, org.length()-1);
//        //Log.d("ffdgdfgdfgd",  org.substring(1, org.length()-1).toString());
//
        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
//
        try {
            URL url = new URL(jsonObject.optString("image").toString());
            Log.d("fgdgdfgd",jsonObject.optString("image").toString());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }
//
//        bitmap = getBitmapfromUrl(jsonObject.optString("image").toString());
//
//        //Todo notification
//        String channelId ="5346436";
//            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//            inboxStyle.addLine(jsonObject.optString("body"));
//            Notification notification;
//            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
//
//            Intent notificationIntent = new Intent(getApplicationContext(), Navigation.class);
//           // notificationIntent.putExtra("userType","");
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//
//
//            notification = mBuilder.setSmallIcon(R.mipmap.new_logo_noti).setTicker(jsonObject.optString("title")).setWhen(0)
//                    .setAutoCancel(true)
//                    .setContentTitle(jsonObject.optString("title"))
//                    .setTicker(jsonObject.optString("title"))
//                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                    .setLargeIcon(image)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null))
//                    .setSmallIcon(R.mipmap.new_logo_noti)
//                    .setContentIntent(contentIntent)
//                    .setContentText(jsonObject.optString("body"))
//                    .build();
//
//        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(channelId,"Channel human readable title", importance);
//            mChannel.setDescription("");
//            mChannel.enableLights(true);
//            mChannel.setLightColor(ContextCompat.getColor(getApplicationContext(), R.color
//                    .colorPrimary));
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                mBuilder.setPriority(android.app.Notification.PRIORITY_HIGH);
//            }
//
//        Long notificationId = SystemClock.currentThreadTimeMillis();
//            notificationManager.notify(notificationId.intValue(), notification);

        sendNotification(jsonObject.optString("body"),jsonObject.optString("title"));

    }

    private void sendNotification(String content,String title) {
        Intent intent = new Intent(this, Navigation.class);
        // Set the Activity to start in a new, empty task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId ="5346436";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.new_logo_noti)
                .setContentTitle(title)
                .setLargeIcon(image)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, "Channel human readable title", importance);
            mChannel.setDescription("");
            mChannel.enableLights(true);
            mChannel.setLightColor(ContextCompat.getColor(getApplicationContext(), R.color
                    .colorPrimary));
            notificationManager.createNotificationChannel(mChannel);
        }

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(android.app.Notification.PRIORITY_HIGH);
        }

        Long notificationId = SystemClock.currentThreadTimeMillis();

        notificationManager.notify(notificationId.intValue(), notificationBuilder.build());

    }


    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

//    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        }else{
//            // If the app is in background, firebase itself handles the notification
//        }
//    }

//    private void handleDataMessage(JSONObject json) {
//        Log.e(TAG, "push json: " + json.toString());
//
//        try {
//            JSONObject data = json.getJSONObject("data");
//
//            String title = data.getString("title");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);
//
//
//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//            }
//        } catch (JSONException e) {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
//    }

    /**
     * Showing notification with text only
     */
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }

    /**
     * Showing notification with text and image
     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
}