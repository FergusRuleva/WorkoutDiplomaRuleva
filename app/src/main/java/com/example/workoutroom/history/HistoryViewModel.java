package com.example.workoutroom.history;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExDatabase;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryViewModel extends AndroidViewModel {

    public HistoryRepository historyRepository;
    public HistoryEntity historyEntityFirst = null;
    private final LiveData<List<TrainingWithExs>> mAllHist;
    private Map<HistoryEntity, List<ExEntity>> mapTr;

    private List<TrainingExCrossRef> trainingExCrossRefList = new ArrayList<>();

    @SuppressLint("NewApi")
    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);
        mAllHist = historyRepository.getAllHistories();
        mapTr = historyRepository.getMapTr();
        trainingExCrossRefList = historyRepository.getTrainingExCrossRefList();
//        if (historyEntityFirst == null){
//            historyEntityFirst = historyRepository.getHistoryEntityFirst();
//        }
    }

    public LiveData<List<TrainingWithExs>> getAllHist() {
        return mAllHist;
    }

    public Map<HistoryEntity, List<ExEntity>> getMapTr(){
        return mapTr;
    }

    public void insert(TrainingExCrossRef trainingExCrossRef) {
        historyRepository.insert(trainingExCrossRef);
    }

    public void insertHistory(HistoryEntity historyEntity){
        historyRepository.insertHistory(historyEntity);
    }

    public void deleteTraining(long id) {
        historyRepository.deleteTraining(id);
    }

    public void delete(long id) {
        historyRepository.delete(id);
    }

    public LiveData<List<TrainingWithExs>> getTrainingWithEx(){
        return historyRepository.getTrainingWithEx();
    }

    public List<ExEntity> getListExs(long id){
        return historyRepository.getListExs(id);
    }

    List<TrainingExCrossRef> getTrainingExCrossRefList(){
        return trainingExCrossRefList;
    }

    public void update(HistoryEntity historyEntity){
        historyRepository.update(historyEntity);
    }

}
