package com.example.fireclip.interfaces;

import android.database.Cursor;

public interface SqLiteDbInterface {

    boolean insertData(String name, int setupDone);

    Cursor getAllData();

    boolean updateData(String name, int setupDone);

    Integer deleteData (String id);
}
