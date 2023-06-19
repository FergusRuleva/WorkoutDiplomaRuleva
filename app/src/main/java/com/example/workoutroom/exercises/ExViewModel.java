package com.example.workoutroom.exercises;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExEntity;

import java.util.List;

public class ExViewModel extends AndroidViewModel {
    private ExRepository mRepository;
    private final LiveData<List<ExEntity>> mAllEx;

    public ExViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ExRepository(application);
        mAllEx = mRepository.getAllEx();
    }

    public LiveData<List<ExEntity>> getAllEx() {
        return mAllEx; // mAllEx = mRepository.getAllEx();
    }

    public void insert(ExEntity exEntity) {
        mRepository.insert(exEntity);
    }

    public void update(ExEntity exEntity) {
        mRepository.update(exEntity);
    }

    public void delete(ExEntity exEntity) {
        mRepository.delete(exEntity);
    }
}
