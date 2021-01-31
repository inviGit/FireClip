package com.example.fireclip.database;

import android.util.Log;

import com.example.fireclip.interfaces.FirebaseDBinterface;
import com.example.fireclip.interfaces.FirebaseListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class FirebaseDbImpli implements FirebaseDBinterface {

    private FirebaseDatabase database;
    private DatabaseReference myref;
    private FirebaseAuth mAuth;

    private final String TAG = "FBDB";

    public FirebaseDbImpli(){
        this.database = FirebaseDatabase.getInstance();
        this.myref = database.getReference("users");
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void regiterUserinFBDB(HashMap userDetails, final FirebaseListener listener) {
        myref
                .child(mAuth.getCurrentUser().getUid()).setValue(userDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        });

    }

    @Override
    public void readDataFromFBDB(final String childName, final FirebaseListener listener) {

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onDataChanged(dataSnapshot.child(mAuth.getCurrentUser().getUid())
                        .child(childName).getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    @Override
    public void writeDataToFBDB(String childName, String value, final FirebaseListener listener) {
        myref
                .child(mAuth.getCurrentUser().getUid()).child(childName).setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "onComplete: ");
                        listener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailed: ");
                        listener.onFailure();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "onSuccess: ");
                listener.onSuccess();
            }
        });
    }
}
