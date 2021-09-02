package com.demo.contact.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.demo.contact.Dao.MainDao;
import com.demo.contact.model.ContactModel;

@Database(entities = {ContactModel.class}, version = 1, exportSchema = false)
public abstract class MyRoomDataBase extends RoomDatabase {

    public abstract MainDao mainDao();

    private static MyRoomDataBase myRoomInstance;
    public static String DATABASE_NAME = "Contact_info";

    public static MyRoomDataBase getMyRoomInstance(Context context) {
        if (myRoomInstance == null) {
            myRoomInstance = Room.databaseBuilder(context.getApplicationContext(), MyRoomDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return myRoomInstance;
    }
}