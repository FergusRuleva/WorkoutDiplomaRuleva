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

    @Query("SELECT SUM(time_ex) FROM allexercises")
    int getTimeExLiveData();

    @Query("SELECT * FROM allexercises LIMIT :limit")
    LiveData<List<ExEntity>> getExsLiveData(int limit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ExEntity exEntity);

    @Query("SELECT * FROM allexercises WHERE is_select = 1 LIMIT 1 OFFSET :offset")
    ExEntity getExLiveData(int offset);

    @Delete
    void delete(ExEntity... exEntity);

    @Update
    void update(ExEntity... exEntity);

    @Query("SELECT * FROM allexercises WHERE is_select = 1")
    LiveData<List<ExEntity>> getExsLimitedLiveData();

//    @Query("DELETE FROM allexercises WHERE idEx = :id")
//    void deleteById(int id);

//    @Query("SELECT COUNT(name_ex) FROM allexercises")
//    LiveData<Integer> getRowCount(); //with LiveData

    //    @Query("DELETE FROM allexercises")
//    void deleteAll();

    //    @Query("SELECT COUNT(name_ex) FROM allexercises")
//    int getRowCount();
}
