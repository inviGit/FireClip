package com.example.fireclip.logic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fireclip.R;
import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.interfaces.SqLiteDbInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private Button buttonConnect, buttonsign;
    private EditText editTextUsername, editTextPassword;
    private String user_name, email, password;
    private TextView editTempText;
    private SqLiteDbInterface sqLiteDbInterface;

    private FirebaseDatabase database;
    private DatabaseReference myref;
    private FirebaseAuth mAuth;

    private AlertDialog.Builder builder;
    private AlertDialog alert;

    private Intent intent;
    private String input;

    private final String TAG = "FBDB";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        sqLiteDbInterface = new DatabaseHelper(this);
        database = FirebaseDatabase.getInstance();
        myref = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        intent = getIntent();
        initView();

        input = intent.getStringExtra("maintocreate");
        editTempText.setText(input);
        onClick();
    }

    private void onClick() {
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = editTextUsername.getText().toString();
                if(checkUsernameInputText(user_name)){
                    checkInFbDb(user_name);
                }
            }
        });

        buttonsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                if(checkSignInput(email, password)){
                    if(input.equals("create")){
                        //create user with email and pass
                        createUserinFBDB(email, password);
                    }else if(input.equals("signin")){
                        //signin user with email and pass
                        signinUserinFBDB(email, password);
                    }
                }
            }
        });
    }

    private void signinUserinFBDB(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            regiterUserinFBDB(user.getUid());
                            insertInSqLiteDB(user.getUid());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void createUserinFBDB(String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            regiterUserinFBDB(user.getUid());
                            insertInSqLiteDB(user.getUid());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private boolean checkSignInput(String email, String password) {
        if (email.isEmpty()) {
            editTextUsername.setError("username required");
            editTextUsername.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
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
                            .setMessage("PLEASE DO NOT SHARE USERNAME\n" +
                                    "username is temporary and is not saved for future use.\n" +
                                    "If app is reinstalled then new username would have to be created")
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

    private boolean checkUsernameInputText(String username) {
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
        if (username.length()<6) {
            editTextUsername.setError("Username length should be at least 6");
            editTextUsername.requestFocus();
            return false;
        }
        return true;
    }

    private void initView() {
        editTextUsername = findViewById(R.id.editUserName);
        editTextPassword = findViewById(R.id.editPassword);
        buttonConnect = findViewById(R.id.buttonConnect);
        buttonsign = findViewById(R.id.buttonsign);
        editTempText = findViewById(R.id.tempText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(input.equals("signin")){
            buttonsign.setText("Sign In");
            buttonConnect.setEnabled(false);
            buttonConnect.setVisibility(View.INVISIBLE);
        }else if(input.equals("create")){
            buttonsign.setText("Sign Up");
        }
    }
}
