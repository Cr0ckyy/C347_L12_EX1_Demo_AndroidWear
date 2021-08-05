package com.myapplicationdev.android.c347_l12_ex1_demo_androidwear;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

public class MainActivity extends AppCompatActivity {

    int notificationId = 001;
    Button btnNotif;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    Intent intent, intentReply;
    PendingIntent pendingIntent, pendingIntentReply;
    RemoteInput remoteInput;
    String text, title;
    Notification notification;

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotif = findViewById(R.id.btnNotif);

        btnNotif.setOnClickListener(view -> {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = new
                        NotificationChannel("default", "Default Channel",
                        NotificationManager.IMPORTANCE_DEFAULT);

                notificationChannel.setDescription("This is for default notifica-tion");
                notificationManager.createNotificationChannel(notificationChannel);
            }

            intent = new Intent(MainActivity.this, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action action = new
                    NotificationCompat.Action.Builder(
                    R.mipmap.ic_launcher,
                    "This is an Action",
                    pendingIntent).build();


            intentReply = new Intent(MainActivity.this,
                    ReplyActivity.class);
            pendingIntentReply = PendingIntent.getActivity
                    (MainActivity.this, 0, intentReply,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            remoteInput = new RemoteInput.Builder("status")
                    .setLabel("Status report")
                    .setChoices(new String[]{"Done", "Not yet"})
                    .build();

            NotificationCompat.Action action2 = new
                    NotificationCompat.Action.Builder(
                    R.mipmap.ic_launcher,
                    "Reply",
                    pendingIntentReply)
                    .addRemoteInput(remoteInput)
                    .build();

            NotificationCompat.WearableExtender extender = new
                    NotificationCompat.WearableExtender();
            extender.addAction(action);
            extender.addAction(action2);

            text = getString(R.string.basic_notify_msg);
            title = getString(R.string.notification_title);

            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(MainActivity.this, "default");
            builder.setContentText(text);
            builder.setContentTitle(title);
            builder.setSmallIcon(android.R.drawable.btn_star_big_off);

            // Attach the action for Wear notification created above
            builder.extend(extender);

            notification = builder.build();

            notificationManager.notify(notificationId, notification);

        });
    }
}
