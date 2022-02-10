package com.webnotics.swipee.room_database;


import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface Location_room_interface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   public void insertData(Location_model location_model);

    @Query("DELETE  FROM locationList where id = :id")
    public void deleteData(String id);


   @Query("SELECT * FROM locationList ")
    public Cursor getAllData();

}
