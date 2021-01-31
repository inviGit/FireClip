package com.example.fireclip.logic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fireclip.R;
import com.example.fireclip.database.DatabaseHelper;
import com.example.fireclip.database.FireBaseRTD;
import com.example.fireclip.interfaces.FireDbInterface;
import com.example.fireclip.interfaces.SqLiteDbInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {

    private SqLiteDbInterface sqLiteDbInterface;
    private FireDbInterface fireDbInterface;
    private ClipboardManager clipboardManager;
    private String txtFromDevice, username;
    private Button dialogSendButton, dialogStopButton, closeDialog;
    private TextView dialogDeviceClipText,dialogWindowClipText;
    private Cursor res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        sqLiteDbInterface = new DatabaseHelper(this);
        fireDbInterface = new FireBaseRTD();
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
                String value = fireDbInterface.readValueFromFBDB(username, "winClipboard");
                dialogWindowClipText.setText(value);
            }
        });

        dialogSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b;
                b = fireDbInterface.writeValueToFBDB(username, "androidClipboard", getClipFromDevice());
                b = fireDbInterface.writeValueToFBDB(username, "android", "1");
            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
