package com.example.workoutroom.dataBase.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExDao {

    @Query("SELECT * FROM allexercises")
    LiveData<List<ExEntity>> getAllExLiveData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ExEntity exEntity);

    @Delete
    void delete(ExEntity... exEntity);

    @Update
    void update(ExEntity... exEntity);
}
