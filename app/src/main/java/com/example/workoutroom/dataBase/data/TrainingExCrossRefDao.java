package com.example.workoutroom.dataBase.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrainingExCrossRefDao {
    @Query("SELECT a.idEx, a.name_ex, a.description_ex, a.time_ex, a.image_ex, a.is_select FROM trainingandex AS t JOIN allexercises AS a ON a.idEx = t.idEx WHERE t.idT = :id")
    List<ExEntity> getExsByIdT(long id);
}
