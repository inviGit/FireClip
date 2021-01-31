package com.example.fireclip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fireclip.logic.ControlActivity;
import com.example.fireclip.logic.CreateUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button buttonCreateUser, buttonLogin;
    private Intent intent;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initView();
        onClick();
    }

    private void onClick() {
        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CreateUser.class);
                intent.putExtra("maintocreate", "create");
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CreateUser.class);
                intent.putExtra("maintocreate", "signin");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        buttonCreateUser = findViewById(R.id.buttonCreateUser);
        buttonLogin = findViewById(R.id.buttonLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {
            intent = new Intent(MainActivity.this, ControlActivity.class);
            startActivity(intent);
        }
    }
}
