package com.webnotics.swipee.room_database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {College_model.class}, version = 1, exportSchema = false)
public abstract class College_room_abstract extends RoomDatabase {
    private static final String DATABASE_NAME = "Swipee_College_db";

    private static volatile College_room_abstract instance;

    public abstract College_room_interface college_room_interface();

    public static College_room_abstract getDatabase(Context context){
        if (instance==null){
            synchronized (College_room_abstract.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), College_room_abstract.class,DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
