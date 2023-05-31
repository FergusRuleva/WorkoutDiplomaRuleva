package com.example.workoutroom.exercises;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExDao;
import com.example.workoutroom.dataBase.data.ExDatabase;
import com.example.workoutroom.dataBase.data.ExEntity;

import java.util.List;

//Абстрактный репозиторий, как это предусмотрено в Руководстве по архитектуре
public class ExRepository {

    private ExDao exDao;
    private LiveData<List<ExEntity>> mAllEx;

    ExRepository(Application application){
        ExDatabase db = ExDatabase.getDbInstance(application);
        exDao = db.exDao();
        mAllEx = exDao.getAllExLiveData();
    }

    //Room выполняет все запросы в отдельном потоке. LiveData будут уведомлять наблюдателя об изменении данных
    LiveData<List<ExEntity>> getAllEx(){
        return mAllEx;
    }

    void insert(ExEntity exEntity){
        ExDatabase.databaseWriteExecutor.execute(()->{
            exDao.insert(exEntity);
        });
    }

    public void delete(ExEntity exEntity){
        ExDatabase.databaseWriteExecutor.execute(()->{
            exDao.delete(exEntity);
        });
    }

    void update(ExEntity exEntity){
        ExDatabase.databaseWriteExecutor.execute(()->{
            exDao.update(exEntity);
        });
    }
}
