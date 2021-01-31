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
import com.example.fireclip.database.FirebaseDbImpli;
import com.example.fireclip.interfaces.FirebaseDBinterface;
import com.example.fireclip.interfaces.FirebaseListener;
import com.example.fireclip.interfaces.SqLiteDbInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateUser extends AppCompatActivity {

    private Button buttonsign;
    private EditText editTextUsername, editTextPassword;
    private String email, password;
    private TextView editTempText;
    private SqLiteDbInterface sqLiteDbInterface;

    private FirebaseDatabase database;
    private DatabaseReference myref;
    private FirebaseAuth mAuth;

    private AlertDialog.Builder builder;
    private AlertDialog alert;

    private Intent intent;
    private String input;

    private FirebaseDBinterface firebaseDBinterface;

    private final String TAG = "FBDB";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        firebaseDBinterface = new FirebaseDbImpli();

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
                            Intent intent = new Intent(CreateUser.this, ControlActivity.class);
                            startActivity(intent);
                        } else {
                            builder = new AlertDialog.Builder(CreateUser.this);
                            builder
                                    .setMessage("Wrong email or password")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    })
                                    .setTitle("Sign up ERROR");
                            alert =builder.create();
                            alert.show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserinFBDB(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            regiterUserinFBDB();
                        } else {
                            // If sign in fails, display a message to the user.
                            builder = new AlertDialog.Builder(CreateUser.this);
                            builder
                                    .setMessage("Enter correct email id")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    })
                                    .setTitle("Sign up ERROR");
                            alert =builder.create();
                            alert.show();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
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

    private void regiterUserinFBDB() {

        HashMap<String, Object> userDetails = new HashMap<>();
        userDetails.put("android", 1);
        userDetails.put("androidClipboard", 1);
        userDetails.put("winClipboard", 1);

        firebaseDBinterface.regiterUserinFBDB(userDetails, new FirebaseListener() {
            @Override
            public void onComplete() {
                //completed
                editTempText.setText("registered");
            }
            @Override
            public void onFailure() {
                builder = new AlertDialog.Builder(CreateUser.this);
                builder
                        .setMessage("Database Update error")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setTitle("Alert");
                alert =builder.create();
                alert.show();
            }
            @Override
            public void onSuccess() {
                editTempText.setText("registered");
                Intent intent = new Intent(CreateUser.this, ControlActivity.class);
                startActivity(intent);
            }

            @Override
            public void onDataChanged(Object value) {

            }

        });
    }

    private void initView() {
        editTextUsername = findViewById(R.id.editUserName);
        editTextPassword = findViewById(R.id.editPassword);
        buttonsign = findViewById(R.id.buttonsign);
        editTempText = findViewById(R.id.tempText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(input.equals("signin")){
            buttonsign.setText("Sign In");
        }else if(input.equals("create")){
            buttonsign.setText("Sign Up");
        }
    }
}
