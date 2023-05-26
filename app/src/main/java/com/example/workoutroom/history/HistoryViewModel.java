package com.example.workoutroom.history;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;

import java.time.LocalDate;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;

    public List<TrainingExCrossRef> crossT;

    public HistoryEntity historyEntityFirst;

    private final LiveData<List<HistoryEntity>> mAllHist;

    @SuppressLint("NewApi")
    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);
        mAllHist = historyRepository.getAllHistories();
        this.crossT = historyRepository.getCrossT();
        historyEntityFirst = historyRepository.getHistoryEntityFirst();
    }

    public LiveData<List<HistoryEntity>> getAllHist() {
        return mAllHist;
    }

    public Long insert(TrainingExCrossRef trainingExCrossRef) {
        return historyRepository.insert(trainingExCrossRef);
    }

    public void delete(HistoryEntity historyEntity) {
        historyRepository.delete(historyEntity);
    }

    public List<TrainingExCrossRef> getCrossT(){
        return historyRepository.getCrossT();
    }

    public LiveData<List<TrainingWithExs>> getTrainingWithEx(){
        return historyRepository.getTrainingWithEx();
    }

    public List<ExEntity> getListExs(int id){
        return historyRepository.getListExs(id);
    }

    public HistoryEntity getHistoryEntityFirst() {
        return historyEntityFirst;
    }
}
