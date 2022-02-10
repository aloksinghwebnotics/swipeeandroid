package com.webnotics.swipee.room_database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Location_model.class}, version = 1, exportSchema = false)
public abstract class Location_room_abstract extends RoomDatabase {
    private static final String DATABASE_NAME = "Swipee_Location_db";

    private static volatile Location_room_abstract instance;

    public abstract Location_room_interface location_room_interface();

    public static Location_room_abstract getDatabase(Context context){
        if (instance==null){
            synchronized (Location_room_abstract.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), Location_room_abstract.class,DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
