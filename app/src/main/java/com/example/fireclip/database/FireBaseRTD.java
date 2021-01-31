package com.example.fireclip.database;

import android.util.Log;

import com.example.fireclip.interfaces.FireDbInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class FireBaseRTD implements FireDbInterface {

    private final String TAG = "FIreDB";
    private FirebaseDatabase database;
    private DatabaseReference myref;
    private boolean returnBool = false;
    private String value;

    public FireBaseRTD(){
        this.database = FirebaseDatabase.getInstance();
        this.myref = database.getReference("users");
    }

    //check if the child(username) is present or not
    @Override
    public synchronized boolean checkUserInFBDB(final String username) {
        myref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username)){
                    returnBool = true;
                }
                Log.d(TAG, "Username in FIRE DB is present: " );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "checkUserInDb: onCancelled ");
                throw databaseError.toException();
            }
        });
        return returnBool;
    }

    //create user as a child
    @Override
    public synchronized void registerInFBDB(String username) {
        myref
                .child(username).child("android").setValue("1")
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
        myref
                .child(username).child("androidClipboard").setValue("1")
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
        myref
                .child(username).child("winClipboard").setValue("1")
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

    //read value from database
    @Override
    public synchronized String readValueFromFBDB(String username, String childName) {
        myref.child(username).child(childName)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        value = dataSnapshot.getValue().toString();
                        Log.d(TAG, "Username in FIRE DB is present(verify pass): ");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "checkUserInDb: onCancelled ");
                    }
                });
        return value;
    }

    //write value to database
    @Override
    public synchronized boolean writeValueToFBDB(String username, String childName, String value) {
        myref
                .child(username).child(childName)
                .setValue(value)
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
                returnBool = true;
                //Toast.makeText(Control.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });

        return returnBool;
    }
}
