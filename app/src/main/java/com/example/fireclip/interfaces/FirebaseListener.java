package com.example.fireclip.interfaces;

public interface FirebaseListener {

    void onComplete();
    void onFailure();
    void onSuccess();
    void onDataChanged(Object value);

}
