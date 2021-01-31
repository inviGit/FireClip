package com.example.fireclip.logic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.interfaces.SqLiteDbInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateUser extends AppCompatActivity {

    private Button buttonConnect;
    private EditText editTextUsername;
    private String user_name;
    private TextView editTempText;
    private SqLiteDbInterface sqLiteDbInterface;

    private FirebaseDatabase database;
    private DatabaseReference myref;

    private AlertDialog.Builder builder;
    private AlertDialog alert;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        sqLiteDbInterface = new DatabaseHelper(this);
        database = FirebaseDatabase.getInstance();
        myref = database.getReference("users");

        initView();
        onClick();
    }

    private void onClick() {
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = editTextUsername.getText().toString();
                if(checkInputText(user_name)){
                    checkInFbDb(user_name);
                }
            }
        });
    }

    private void checkInFbDb(final String username) {
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(username)){
                    editTempText.setText("user present in db");
                }else{
                    editTempText.setText("user not present in db");
                    builder = new AlertDialog.Builder(CreateUser.this);
                    builder
                            .setMessage("PLEASE DO NOT SHARE USERNAME")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    regiterUserinFBDB(username);
                                    insertInSqLiteDB(user_name);
                                }
                            })
                            .setTitle("Alert");
                    alert =builder.create();
                    alert.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                editTempText.setText("on cancelled fbdb userCheck");
            }
        });
    }

    private void regiterUserinFBDB(String username) {

        HashMap<String, Object> userDetails = new HashMap<>();
        userDetails.put("android", 1);
        userDetails.put("androidClipboard", 1);
        userDetails.put("winClipboard", 1);

        myref
                .child(username).setValue(userDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editTempText.setText("registered");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        editTempText.setText("register failed");
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                editTempText.setText("register successfully");
            }
        });
    }

    private void insertInSqLiteDB(String user_name) {
        if(sqLiteDbInterface.insertData(user_name, 0)){
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
