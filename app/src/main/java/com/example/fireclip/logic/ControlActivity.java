package com.example.fireclip.logic;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.interfaces.SqLiteDbInterface;
import com.example.fireclip.service.ClipService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ControlActivity extends AppCompatActivity {

    TextView textTemp;
    private Button buttonStartServ, buttonStopServ;
    private AlertDialog.Builder builder;
    private AlertDialog alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        initView();
    }

    public void startService(View v){
        alertBox();
    }

    private void startServ() {
        Intent serviceIntent = new Intent(ControlActivity.this, ClipService.class);
        ContextCompat.startForegroundService(ControlActivity.this, serviceIntent);
    }

    private void alertBox() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Start ClipNoti")
                .setMessage("ClipNoti will start a foreground service as a Notification.\n" +
                        "\n" +
                        "Press Accept to start syncing the clipboard.")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startServ();
                        builder = new AlertDialog.Builder(ControlActivity.this);
                        builder
                                .setMessage("CHECK THE NOTIFICATION\n")
                                .setPositiveButton("Instructions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        builder = new AlertDialog.Builder(ControlActivity.this);
                                        builder
                                                .setMessage(new StringBuilder().append("Instructions to sync clipboard:\n\n")
                                                        .append("1. Service can be STOPPED from the notification.\n\n")
                                                        .append("2. Desktop to Android clipboard is synced in realtime.\n\n")
                                                        .append("3. For security, the Android copied text is not send to desktop automatically.\n\n")
                                                        .append("4. To send the Android copied text to desktop PRESS SEND from the notification.\n\n").toString()
                                                )
                                                .setPositiveButton("Minimize FireClip app", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finishAffinity();
                                                    }
                                                })
                                                .setTitle("FireClip Service is Running");
                                        alert =builder.create();
                                        alert.show();
                                    }
                                })
                                .setTitle("FireClip Service is Running");
                        alert =builder.create();
                        alert.show();
                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alert =builder.create();
        alert.show();
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, ClipService.class);
        stopService(serviceIntent);
    }

    private void initView() {
        textTemp = findViewById(R.id.textTemp);
        buttonStartServ = findViewById(R.id.startServButton);
        buttonStopServ = findViewById(R.id.stopServButton);
    }

}
