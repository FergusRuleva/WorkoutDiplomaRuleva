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
    private final LiveData<List<ExEntity>> mExLimited;
    HistoryEntity historyEntityFirst;
    TrainingWithExs trainingWithExs;
    TrainingExCrossRef trainingExCrossRef;
    List<ExEntity> exEntityList;

    public TrainingViewModel(@NonNull Application application) {
        super(application);

        mRepository = new TrainingRepository(application);
        mAllEx = mRepository.getExs();
        mExLimited = mRepository.getExsLimited();
        historyEntityFirst = mRepository.historyEntityFirst;
        trainingExCrossRef = mRepository.trainingExCrossRef;
    }

    public LiveData<List<ExEntity>> getAllExs() {
        return mAllEx;
    }

//    public LiveData<List<ExEntity>> getEx(int offset){
//        return mRepository.getExs();
//    }

//    int getTimeEx(int limit){
//        return mRepository.getTimeEx(limit);
//    }
//

    public LiveData<List<ExEntity>> getExsLimited(){
        return mExLimited;
    }

    public ExEntity getEx(int offset){
        return mRepository.getEx(offset);
    }

}
