package com.example.fireclip.interfaces;

public interface FireDbInterface {

    boolean checkUserInFBDB(String username);

    void registerInFBDB(String username);

    String readValueFromFBDB(String username, String childName);

    boolean writeValueToFBDB(String username, String childName, String value);

}
