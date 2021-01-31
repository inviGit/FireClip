package com.example.fireclip.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.database.FireBaseRTD;
import com.example.fireclip.interfaces.FireDbInterface;
import com.example.fireclip.interfaces.SqLiteDbInterface;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class CreateUser extends AppCompatActivity {

    private FireDbInterface fireDbInterface;
    private Button buttonConnect;
    private EditText editTextUsername;
    private String user_name;
    private TextView editTempText;
    private SqLiteDbInterface sqLiteDbInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        fireDbInterface = new FireBaseRTD();
        sqLiteDbInterface = new DatabaseHelper(this);

        initView();
        onClick();
    }

    private void onClick() {
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = editTextUsername.getText().toString();
                if(checkInputText(user_name)){
                    fireDbInterface.registerInFBDB(user_name);
                    insertInSqLiteDB(user_name);
                }
            }
        });
    }

    private void insertInSqLiteDB(String user_name) {
        if(sqLiteDbInterface.insertData(user_name)){
            editTempText.setText("inserted in sqlite db");
            Intent intent = new Intent(CreateUser.this, ControlActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkInputText(String username) {
        if (username.isEmpty()) {
            editTextUsername.setError("username required");
            editTextUsername.requestFocus();
            return false;
        }
        if (username.contains(" ")) {
            editTextUsername.setError("Username should contain space");
            editTextUsername.requestFocus();
            return false;
        }
        return true;
    }

    private void initView() {
        editTextUsername = findViewById(R.id.editUserName);
        buttonConnect = findViewById(R.id.buttonConnect);
        editTempText = findViewById(R.id.tempText);
    }
}
