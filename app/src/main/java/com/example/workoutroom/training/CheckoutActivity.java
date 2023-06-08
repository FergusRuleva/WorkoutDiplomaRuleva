package com.example.workoutroom.training;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;
import com.example.workoutroom.exercises.ExAdapter;
import com.example.workoutroom.history.HistoryViewModel;
import com.example.workoutroom.training.timer.CountdownTimerActivity;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";
    private int sets; //кол-во повторений
    private TextView totalTimeText; //время выполнения
    private TextView setsText;
    private int totalTime = 0; //переменная для времени выполнения упр
    private MaterialButton materialButton;
    private TrainingViewModel trainingViewModel;
    private HistoryViewModel historyViewModel;
    private List<ExEntity> exEntityList;
    private List<TrainingExCrossRef> crossList = new ArrayList<>();
    private HistoryEntity historyEntity;
    private int sizeListTr;
    private String textSets, textTotalTime, textMins, textSec;

    @SuppressLint({"SourceLockedOrientationActivity", "MissingInflatedId", "SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.pink_color), PorterDuff.Mode.SRC_ATOP);
        setTitle(R.string.button_show_ex);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textSets = this.getResources().getString(R.string.text_holder_sets);
        textTotalTime = this.getResources().getString(R.string.text_total_time);
        textMins = this.getResources().getString(R.string.text_holder_min);
        textSec = this.getResources().getString(R.string.text_holder_sec);

        //модели взаимодействия с тренировкой и с историей тренировки
        trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        //список для выбранных упражнений
        exEntityList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTraining);
        TrainingAdapter.OnExClickListener onClickListener = new TrainingAdapter.OnExClickListener(){
            @Override
            public void onExClick(ExEntity exEntity, int position) {
                //по клику на эл rV идет проверка - уже выбран элемент или нет
                if (!trainingViewModel.getAllExs().getValue().get(position).isSelect){
                    trainingViewModel.getAllExs().getValue().get(position).isSelect = true;
                    addExsInTraining(position); //метод добавления/удаления упр из списка
                }
                else if(trainingViewModel.getAllExs().getValue().get(position).isSelect){
                    trainingViewModel.getAllExs().getValue().get(position).isSelect = false;
                    addExsInTraining(position);
                }
            }
        };

        final TrainingAdapter adapter = new TrainingAdapter(new TrainingAdapter.WorkoutDiff(), onClickListener, textSec); //?????

        materialButton = findViewById(R.id.materialButton);
        totalTimeText = findViewById(R.id.totalTimeText);
        setsText = findViewById(R.id.setsText);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise(v);
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("sets")) { //если передали

            sets = intent.getIntExtra("sets", 1); //получаем кол-во подходов

            //получение списка всех упражнений и обновление
            trainingViewModel.getAllExs().observe(this, words -> {
                adapter.submitList(words);
            });

            setsText.setText(sets + "\t" + textSets);

        } else {
            Log.d(TAG, "getIntent: error");
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //кнопка Start
    @SuppressLint("NewApi")
    public void startExercise(View view) {
        if (!exEntityList.isEmpty()){
            //переход на активность с Таймером отсчета
            Intent intent = new Intent(this, CountdownTimerActivity.class); // создание объекта Intent для запуска CountdownTimerActivity

            int totalTimeHi = totalTime / 60 * sets;
            historyEntity = new HistoryEntity(LocalDate.now().toString(), totalTimeHi, sets - 1);
            historyViewModel.insertHistory(historyEntity);
            sizeListTr = historyViewModel.historyRepository.historyDao.getHistoryEntity().size();
            //метод создания записей выбранных упражнений (пперекр. таблица)
            createTraining();

            //добавление записей выбранных упражнений в таблицу many-to-many
            for (TrainingExCrossRef exsT: crossList){
                historyViewModel.insert(exsT);
            }

            //передача данных
            intent.putExtra("sets", sets);
            intent.putExtra("totalTime", totalTime);
            intent.putExtra("idTr", historyViewModel.historyRepository.historyDao.getHistoryEntity().get(sizeListTr - 1).idT);

            startActivity(intent); //переход, запуск
        }else{
            Toast.makeText(
                    getApplicationContext(),
                    R.string.exs_not_select,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void addExsInTraining(int position){

        //если упр выбрано -> добавление в список выбранных, подсчет итогового времени, установка текста времени
        if (trainingViewModel.getAllExs().getValue().get(position).isSelect){
            exEntityList.add(trainingViewModel.getAllExs().getValue().get(position));
            totalTime += trainingViewModel.getAllExs().getValue().get(position).getTimeEx();
            totalTimeText.setText(textTotalTime + "\n" + totalTime / 60 * sets + "\t" + textMins);
        }
        else { //удаление из списка выбранных, подсчет времени, установка текста времени
            exEntityList.remove(trainingViewModel.getAllExs().getValue().get(position));
            totalTime -= trainingViewModel.getAllExs().getValue().get(position).getTimeEx();
            totalTimeText.setText(textTotalTime + "\n" + totalTime / 60 * sets + "\t" + textMins);
        }
    }

    public void createTraining (){
        //создание записей перекр. таблицы (добавление id тренировки и id упражнения)
        for (ExEntity exsT: exEntityList){
            TrainingExCrossRef trainingExCrossRef = new TrainingExCrossRef();
            trainingExCrossRef.idT = historyViewModel.historyRepository.historyDao.getHistoryEntity().get(sizeListTr - 1).idT;
            trainingExCrossRef.idEx = exsT.idEx;
            crossList.add(trainingExCrossRef);
        }
    }

    //закрытие активности
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
