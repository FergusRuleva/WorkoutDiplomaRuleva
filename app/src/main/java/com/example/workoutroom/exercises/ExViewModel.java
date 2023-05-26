package com.example.workoutroom.exercises;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutroom.dataBase.data.ExEntity;

import java.util.List;

public class ExViewModel extends AndroidViewModel {

    private ExRepository mRepository;
    // Использование LiveData и кэширование того, что возвращает getAllEx, имеет несколько преимуществ:
    // - Мы можем установить наблюдателя на данные (вместо опроса изменений) и обновлять пользовательский интерфейс только тогда, когда данные действительно изменяются.
    // - Хранилище полностью отделено от пользовательского интерфейса через ViewModel.
    private final LiveData<List<ExEntity>> mAllEx;

    public ExViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ExRepository(application);
        mAllEx = mRepository.getAllEx();
    }

    public LiveData<List<ExEntity>> getAllEx() {
        return mAllEx;
    }

    public void insert(ExEntity exEntity) {
        mRepository.insert(exEntity);
    }

    public void delete(ExEntity exEntity) {
        mRepository.delete(exEntity);
    }
}
