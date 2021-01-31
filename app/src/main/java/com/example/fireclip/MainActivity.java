package com.example.fireclip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.interfaces.SqLiteDbInterface;
import com.example.fireclip.logic.ControlActivity;
import com.example.fireclip.logic.CreateUser;

public class MainActivity extends AppCompatActivity {

    private Button buttonCreateUser;
    private Intent intent;
    private SqLiteDbInterface sqLiteDbInterface;
    private Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDbInterface = new DatabaseHelper(this);
        res = sqLiteDbInterface.getAllData();

        initView();
        onClick();
    }

    private void onClick() {
        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CreateUser.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        buttonCreateUser = findViewById(R.id.buttonCreateUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(res.getCount() != 0) {
            intent = new Intent(MainActivity.this, ControlActivity.class);
            startActivity(intent);
        }
    }



}
