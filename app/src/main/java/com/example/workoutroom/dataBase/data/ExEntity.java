package com.example.workoutroom.dataBase.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "allexercises")
public class ExEntity {

    @PrimaryKey(autoGenerate = true)
    public long idEx;

    @NonNull
    @ColumnInfo(name = "name_ex")
    public String nameEx;

    @ColumnInfo(name = "description_ex")
    public String descriptionEx;

    @NonNull
    @ColumnInfo(name = "time_ex")
    public int timeEx;

    @NonNull
    @ColumnInfo(name = "image_ex", typeAffinity = ColumnInfo.BLOB)
    public Bitmap imageEx;

    @ColumnInfo(name = "is_select")
    public boolean isSelect;

    public ExEntity(@NonNull String nameEx, String descriptionEx, int timeEx, Bitmap imageEx, boolean isSelect){
        this.nameEx = nameEx;
        this.descriptionEx = descriptionEx;
        this.timeEx = timeEx;
        this.imageEx = imageEx;
        this.isSelect = isSelect;
    }

    public long getIdEx() {
        return idEx;
    }

    @NonNull
    public String getNameEx() {
        return nameEx;
    }

    public String getDescriptionEx() {
        return descriptionEx;
    }

    @NonNull
    public int getTimeEx() {
        return timeEx;
    }

    @NonNull
    public Bitmap getImageEx() {
        return imageEx;
    }

    public boolean getIsSelect() {
        return isSelect;
    }
}