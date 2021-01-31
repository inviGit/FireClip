package com.example.fireclip.interfaces;

import java.util.HashMap;

public interface FirebaseDBinterface  {

    void regiterUserinFBDB(HashMap userDetails, final FirebaseListener listener);
    void readDataFromFBDB(String childName, final FirebaseListener listener);
    void writeDataToFBDB(String childName, String value, final FirebaseListener listener);

}
