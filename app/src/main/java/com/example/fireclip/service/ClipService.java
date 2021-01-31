package com.example.fireclip.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import com.example.fireclip.R;
import com.example.fireclip.database.FirebaseDbImpli;
import com.example.fireclip.interfaces.FirebaseDBinterface;
import com.example.fireclip.interfaces.FirebaseListener;
import com.example.fireclip.logic.DialogActivity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


public class ClipService extends Service{

    private ClipData clipData;
    private ClipboardManager clipboardManager;
    private String winClip = "hi";

    private FirebaseDBinterface firebaseDBinterface;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDBinterface = new FirebaseDbImpli();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground();
        else startForeground(1, new Notification());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        firebaseDBinterface.readDataFromFBDB("winClipboard", new FirebaseListener() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onFailure() {
                //failed
            }
            @Override
            public void onSuccess() {

            }
            @Override
            public void onDataChanged(Object res) {
                String value = res.toString();
                if(!winClip.equals(value) && !value.equals("")){
                    winClip = value;
                    clipData = ClipData.newPlainText("text",value);
                    clipboardManager.setPrimaryClip(clipData);
                }
            }
        });

        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){

        String NOTIFICATION_CHANNEL_ID = "com.example.fireclip";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent notificationIntent = new Intent(this, DialogActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_android)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
