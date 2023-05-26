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
import java.util.List;

public class HistoryRepository {

    private HistoryDao historyDao;
    private TrainingExCrossRefDao trainingExCrossRefDao;
    private LiveData<List<HistoryEntity>> mAllHist;

    private List<TrainingExCrossRef> crossT;

    private HistoryEntity historyEntityFirst;

    @SuppressLint("NewApi")
    HistoryRepository(Application application){
        ExDatabase db = ExDatabase.getDbInstance(application);
        historyDao = db.historyDao();
        //mAllHist = historyDao.getAllHistoriesLiveData();
        historyEntityFirst = new HistoryEntity(LocalDate.now().toString(),0);
    }

    //Room выполняет все запросы в отдельном потоке. LiveData будут уведомлять наблюдателя об изменении данных
    LiveData<List<HistoryEntity>> getAllHistories(){
        return mAllHist;
    }

    public void delete(HistoryEntity historyEntity){
        ExDatabase.databaseWriteExecutor.execute(()->{
            //historyDao.delete(historyEntity);
        });
    }

    Long insert(TrainingExCrossRef trainingExCrossRef){
        //ExDatabase.databaseWriteExecutor.execute(()->{
        return historyDao.insert(trainingExCrossRef);
    }

    public List<TrainingExCrossRef> getCrossT(){
        return crossT;
    }

    LiveData<List<TrainingWithExs>> getTrainingWithEx(){
        return historyDao.getTrainingWithExs();
    }

    List<ExEntity> getListExs(int id){
        return trainingExCrossRefDao.getExsByIdT(id);
    }

    public HistoryEntity getHistoryEntityFirst() {
        return historyEntityFirst;
    }
}
