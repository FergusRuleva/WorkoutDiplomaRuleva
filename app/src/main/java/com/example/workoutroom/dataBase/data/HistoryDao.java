package com.example.workoutroom.dataBase.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

@Dao
public interface HistoryDao {

    @Transaction
    @Query("SELECT * FROM historytraining")
    LiveData<List<TrainingWithExs>> getTrainingWithExs();

    @Query("SELECT a.idEx, a.name_ex, a.description_ex, a.time_ex, a.image_ex, a.is_select FROM trainingandex AS t JOIN allexercises AS a ON a.idEx = t.idEx WHERE t.idT = :id")
    List<ExEntity> getListExsByIdTr(long id);

    @Query("SELECT * FROM historytraining")
    List<HistoryEntity> getHistoryEntity();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrainingExCrossRef trainingExCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryEntity historyEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExEntity exEntity);

    @Transaction
    @Query("DELETE FROM historytraining WHERE idT = :id")
    void deleteTraining(long id);

    @Transaction
    @Update
    void update(HistoryEntity historyEntity);
}
