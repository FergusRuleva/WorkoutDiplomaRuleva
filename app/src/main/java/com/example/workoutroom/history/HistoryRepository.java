package com.example.workoutroom.history;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExDao;
import com.example.workoutroom.dataBase.data.ExDatabase;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryDao;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingExCrossRefDao;
import com.example.workoutroom.dataBase.data.TrainingWithExs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryRepository {
    public HistoryDao historyDao;
    private TrainingExCrossRefDao trainingExCrossRefDao;
    private LiveData<List<TrainingWithExs>> mAllHist;

    private List<TrainingWithExs> mAllHistWithoutLiveData;

    private Map<HistoryEntity, List<ExEntity>> mapTr;
    private List<TrainingExCrossRef> trainingExCrossRefList = new ArrayList<>();

    @SuppressLint("NewApi")
    HistoryRepository(Application application){
        ExDatabase db = ExDatabase.getDbInstance(application);
        historyDao = db.historyDao();
        mAllHistWithoutLiveData = historyDao.getTrainingWithExsWithoutLiveData();
        mAllHist = historyDao.getTrainingWithExs();
        mapTr = historyDao.loadTrainingAndEx();
        trainingExCrossRefList = historyDao.getTrainingExCrossRef();
        trainingExCrossRefDao = db.trainingExCrossRefDao();
//        if (historyEntityFirst == null){
//            historyEntityFirst = new HistoryEntity(LocalDate.now().toString(),0, 0);
//            historyDao.insert(historyEntityFirst);
//        }
    }

    //Room выполняет все запросы в отдельном потоке. LiveData будут уведомлять наблюдателя об изменении данных
    LiveData<List<TrainingWithExs>> getAllHistories(){
        return mAllHist;
    }

    Map<HistoryEntity, List<ExEntity>> getMapTr(){
        return mapTr;
    }

    public void deleteTraining(long id){
        ExDatabase.databaseWriteExecutor.execute(()->{
            historyDao.deleteTraining(id);
        });
    }

    public void delete(long id){
        ExDatabase.databaseWriteExecutor.execute(()->{
            historyDao.delete(id);
        });
    }

    void insert(TrainingExCrossRef trainingExCrossRef){
        //ExDatabase.databaseWriteExecutor.execute(()->{
         historyDao.insert(trainingExCrossRef);
    }

    void insertHistory(HistoryEntity historyEntity){
        historyDao.insert(historyEntity);
    }

    LiveData<List<TrainingWithExs>> getTrainingWithEx(){
        return historyDao.getTrainingWithExs();
    }

    List<ExEntity> getListExs(long id){
        return trainingExCrossRefDao.getExsByIdT(id);
    }

    List<TrainingExCrossRef> getTrainingExCrossRefList(){
        return trainingExCrossRefList;
    }

    void update(HistoryEntity historyEntity){
        ExDatabase.databaseWriteExecutor.execute(()->{
            historyDao.update(historyEntity);
        });
    }
}
