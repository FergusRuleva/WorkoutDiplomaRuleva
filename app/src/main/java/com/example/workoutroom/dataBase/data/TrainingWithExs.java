package com.example.workoutroom.dataBase.data;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class TrainingWithExs {
    @Embedded
    public HistoryEntity historyEntity;
    @Relation(
            parentColumn = "idT",
            entityColumn = "idEx",
            associateBy = @Junction(TrainingExCrossRef.class)
    )
    public List<ExEntity> exEntityList;
}
