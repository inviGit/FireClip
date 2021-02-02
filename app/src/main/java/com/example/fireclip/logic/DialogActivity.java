package com.example.fireclip.logic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.database.FirebaseDbImpli;
import com.example.fireclip.interfaces.FirebaseDBinterface;
import com.example.fireclip.interfaces.FirebaseListener;
import com.example.fireclip.service.ClipService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {

    private ClipboardManager clipboardManager;
    private ClipData clipData;
    private String txtFromDevice;
    private Button dialogSendButton, dialogStopButton, closeDialog;
    private TextView dialogDeviceClipText,dialogWindowClipText;
    private String winClip = "hi";

    private FirebaseDBinterface firebaseDBinterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        firebaseDBinterface = new FirebaseDbImpli();

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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
                readvalueFromFBDB();
            }
        });

        dialogSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeValueToFBDB("androidClipboard",getClipFromDevice());
                writeValueToFBDB("android", "1");
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

    private void readvalueFromFBDB() {
        firebaseDBinterface.readDataFromFBDB("winClipboard", new FirebaseListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onDataChanged(Object res) {
                String value = res.toString();
                if(!winClip.equals(value) && !value.equals("")){
                    winClip = value;
                    clipData = ClipData.newPlainText("text",value);
                    clipboardManager.setPrimaryClip(clipData);
                    dialogWindowClipText.setText(winClip);
                }
            }
        });

    }

    private void writeValueToFBDB(String childName, String value) {

        firebaseDBinterface.writeDataToFBDB(childName, value,  new FirebaseListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess() {
                //written
            }

            @Override
            public void onDataChanged(Object value) {

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
}
