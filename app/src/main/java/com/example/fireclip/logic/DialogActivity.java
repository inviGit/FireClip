package com.example.fireclip.logic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.interfaces.SqLiteDbInterface;
import com.example.fireclip.service.ClipService;
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
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myref;
    private SqLiteDbInterface sqLiteDbInterface;
    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private String txtFromDevice, username;
    private Button dialogSendButton, dialogStopButton, closeDialog;
    private TextView dialogDeviceClipText,dialogWindowClipText;
    private Cursor res;
    private String winClip = "hi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        database = FirebaseDatabase.getInstance();
        myref = database.getReference("users");

        sqLiteDbInterface = new DatabaseHelper(this);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        res = sqLiteDbInterface.getAllData();
        getUsername();

        initView();
        onClick();
    }

    private void onClick() {

        dialogDeviceClipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClipFromDevice();
            }
        });

        dialogWindowClipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.child(username)
                                .child("android").getValue().toString();
                        int v = Integer.parseInt(value);
                        if(v == 0){
                            String txt = dataSnapshot
                                    .child(username)
                                    .child("winClipboard").getValue().toString();
                            if(!winClip.equals(txt) && !txt.equals("")){
                                winClip = txt;
                                clipData = ClipData.newPlainText("text",txt);
                                clipboardManager.setPrimaryClip(clipData);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        dialogSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeValueToFBDB();
            }
        });

        dialogStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(DialogActivity.this, ClipService.class);
                stopService(serviceIntent);
                finish();
            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void writeValueToFBDB() {
        HashMap<String, Object> clipDetail = new HashMap<>();
        clipDetail.put("androidClipboard", getClipFromDevice());
        clipDetail.put("android", "1");
        myref
                .child(username).setValue(clipDetail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("jfbvkj", "onComplete: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //Toast.makeText(Firebase.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(Control.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getClipFromDevice() {
        ClipData pData = clipboardManager.getPrimaryClip();
        ClipData.Item item = pData.getItemAt(0);
        txtFromDevice = item.getText().toString();
        dialogDeviceClipText.setText(txtFromDevice);
        return txtFromDevice;
    }

    private void initView() {
        dialogWindowClipText = findViewById(R.id.dialogWindowClipText);
        dialogDeviceClipText = findViewById(R.id.dialogDeviceClipText);
        dialogSendButton = findViewById(R.id.dialogSendButton);
        dialogStopButton = findViewById(R.id.dialogStopButton);
        closeDialog = findViewById(R.id.closeDialog);
    }

    private void getUsername() {
        if(res.getCount() != 0) {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                username = res.getString(1);
            }
        }else{
            //please login again
        }
    }
}
