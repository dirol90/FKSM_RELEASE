package com.tsimbalyukstudio.fksm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PushService extends Service {
    Intent intent;

    public PushService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    String userID;
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        userID = intent.getStringExtra("UserID");
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
