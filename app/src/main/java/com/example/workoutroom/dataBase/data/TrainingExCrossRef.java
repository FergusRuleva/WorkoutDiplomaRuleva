package com.example.workoutroom.dataBase.data;

import androidx.room.Entity;

@Entity(tableName = "trainingandex", primaryKeys = {"idT", "idEx"})
public class TrainingExCrossRef {
    public long idT;

    public long idEx;
}