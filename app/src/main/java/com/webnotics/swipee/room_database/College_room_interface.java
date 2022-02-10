package com.webnotics.swipee.room_database;


import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface College_room_interface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   public void insertData(College_model college_modelModel);

    @Query("DELETE  FROM collegeList where id = :id")
    public void deleteData(String id);


   @Query("SELECT * FROM collegeList ")
    public Cursor getAllData();

}
