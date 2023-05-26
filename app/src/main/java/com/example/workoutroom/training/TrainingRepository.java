package com.example.workoutroom.training;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExDao;
import com.example.workoutroom.dataBase.data.ExDatabase;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;

import java.time.LocalDate;
import java.util.List;

public class TrainingRepository {
    private ExDao exDao;
    private LiveData<List<ExEntity>> mAllEx;

    HistoryEntity historyEntityFirst;
    TrainingWithExs trainingWithExs;
    TrainingExCrossRef trainingExCrossRef;
    List<ExEntity> exEntityList;

    @SuppressLint("NewApi")
    TrainingRepository(Application application){
        ExDatabase db = ExDatabase.getDbInstance(application);
        exDao = db.exDao();
        mAllEx = exDao.getAllExLiveData();
        historyEntityFirst = new HistoryEntity(LocalDate.now().toString(), 0);
        trainingWithExs = new TrainingWithExs();
        trainingWithExs.historyEntity = historyEntityFirst;
    }

    //Room выполняет все запросы в отдельном потоке. LiveData будут уведомлять наблюдателя об изменении данных
    LiveData<List<ExEntity>> getExs(){
        return mAllEx;
    }
    LiveData<List<ExEntity>> getExsLimited(){
        return exDao.getExsLimitedLiveData();
    }
    ExEntity getEx(int offset){
        return exDao.getExLiveData(offset);
    }

//    int getTimeEx(int limit){
//        return exDaoLimit.getTimeExLiveData(limit);
//    }
//    LiveData<List<ExEntity>> getExs(int limit){
//        return exDaoLimit.getExsLiveData(limit);
//    }
    public void getIdTraining(){
        return ;
    }
}
