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
//    @Transaction
//    @Query("SELECT * FROM historytraining JOIN trainingandex ON historytraining.idT = trainingandex.idT JOIN allexercises ON trainingandex.idEx = allexercises.idEx")
//    LiveData<List<TrainingWithExs>> getTrainingWithExs();

    @Transaction
    @Query("SELECT * FROM historytraining")
    LiveData<List<TrainingWithExs>> getTrainingWithExs();

    @Transaction
    @Query("SELECT * FROM historytraining")
    List<TrainingWithExs> getTrainingWithExsWithoutLiveData();

    @Query("SELECT a.idEx, a.name_ex, a.description_ex, a.time_ex, a.image_ex, a.is_select FROM trainingandex AS t JOIN allexercises AS a ON a.idEx = t.idEx WHERE t.idT = :id")
    List<ExEntity> getListExsByIdTr(long id);


    @Query("SELECT * FROM historytraining")
    List<HistoryEntity> getHistoryEntity();

    @Query("SELECT * FROM trainingandex")
    List<TrainingExCrossRef> getTrainingExCrossRef();

    @Query("SELECT * FROM historytraining JOIN trainingandex ON historytraining.idT = trainingandex.idT JOIN allexercises ON trainingandex.idEx = allexercises.idEx")
    Map<HistoryEntity, List<ExEntity>> loadTrainingAndEx();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrainingExCrossRef trainingExCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryEntity historyEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExEntity exEntity);

//    @Transaction
//    @Delete
//    void deleteTraining(TrainingExCrossRef trainingExCrossRef);

    @Transaction
    @Query("DELETE FROM historytraining WHERE idT = :id")
    void deleteTraining(long id);

    @Transaction
    @Query("DELETE FROM trainingandex WHERE idT = :id")
    void delete(long id);

    @Transaction
    @Update
    void update(HistoryEntity historyEntity);

//    @Query("DELETE FROM trainingandex WHERE idT =:id1 AND idEx =:id2")
//    void delete(long id1, long id2);

//    @Transaction
//    @Query("SELECT * FROM historytraining LIMIT 1 OFFSET :offset")
//    ExEntity getExLiveData(int offset);

//    @Transaction
//    @Query("SELECT * FROM historytraining")
//    ExEntity getExLiveData(int offset);

    @Query("DELETE FROM allexercises WHERE idEx = :id")
    void deleteById(int id);



}
