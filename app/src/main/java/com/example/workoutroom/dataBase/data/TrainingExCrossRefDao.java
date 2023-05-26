package com.example.workoutroom.dataBase.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrainingExCrossRefDao {

    @Query("SELECT * FROM trainingandex WHERE idEx = :id")
    List<ExEntity> getExsByIdT(int id);

    //    @Query("SELECT * FROM trainingandex WHERE idEx = :id")
//    void deleteById(int id);
}
