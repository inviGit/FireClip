package com.example.fireclip.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.service.ClipService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ControlActivity extends AppCompatActivity {

    TextView textTemp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        textTemp = findViewById(R.id.tempText);

    }

    public void startService(View v){
        Intent serviceIntent = new Intent(this, ClipService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, ClipService.class);
        stopService(serviceIntent);

    }
}
