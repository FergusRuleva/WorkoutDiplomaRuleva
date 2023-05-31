package com.example.workoutroom.dataBase.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "historytraining")
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    public long idT;

    @NonNull
    @ColumnInfo(name = "date_t")
    public String dateT;

    @NonNull
    @ColumnInfo(name = "time_t")
    public int timeT;

    @NonNull
    @ColumnInfo(name = "sets_t")
    public int setsT = 1;

    @NonNull
    @ColumnInfo(name = "is_done_t")
    public boolean isDone;

    public HistoryEntity(@NonNull String dateT, int timeT, int setsT){
        this.dateT = dateT;
        this.timeT = timeT;
        this.setsT = setsT;
    }

    public long getIdT() {
        return idT;
    }
    @NonNull
    public String getDateT() {
        return dateT;
    }

    public int getTimeT() {
        return timeT;
    }

    public boolean getDone() {
        return isDone;
    }

    public int getSetsT() {
        return setsT;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
