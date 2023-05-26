package com.example.workoutroom.dataBase.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface HistoryDao {
    @Transaction
    @Query("SELECT * FROM historytraining")
    LiveData<List<TrainingWithExs>> getTrainingWithExs();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TrainingExCrossRef trainingExCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryEntity historyEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExEntity exEntity);

    @Delete
    void deleteTraining(TrainingExCrossRef trainingExCrossRef);

//    @Transaction
//    @Query("SELECT * FROM historytraining LIMIT 1 OFFSET :offset")
//    ExEntity getExLiveData(int offset);

//    @Transaction
//    @Query("SELECT * FROM historytraining")
//    ExEntity getExLiveData(int offset);

    @Query("DELETE FROM allexercises WHERE idEx = :id")
    void deleteById(int id);



}
