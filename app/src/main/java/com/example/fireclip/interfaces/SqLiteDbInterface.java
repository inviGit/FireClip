package com.example.fireclip.interfaces;

import android.database.Cursor;

public interface SqLiteDbInterface {

    boolean insertData(String name);

    Cursor getAllData();

    boolean updateData(String name);

    Integer deleteData (String id);
}
