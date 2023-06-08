package com.example.workoutroom.training;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;

import java.util.List;

public class TrainingViewModel extends AndroidViewModel {
    public TrainingRepository mRepository;
    private final LiveData<List<ExEntity>> mAllEx;

    public TrainingViewModel(@NonNull Application application) {
        super(application);

        mRepository = new TrainingRepository(application);
        mAllEx = mRepository.getExs();
    }

    public LiveData<List<ExEntity>> getAllExs() {
        return mAllEx;
    }
}
