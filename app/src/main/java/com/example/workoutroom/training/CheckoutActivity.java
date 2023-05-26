package com.example.workoutroom.training;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class CheckoutActivity extends AppCompatActivity implements TrainingAdapter.OnWorkoutListener {

    private static final String TAG = "CheckoutActivity";
    private int sets; //кол-во повторений
    private int exs; //кол-во повторений
    private TextView totalTimeText; //время выполнения
    private TextView setsText;
    private int setsInt = 1;
    private int totalTime = 0; //переменная для времени выполнения упр

    private MaterialButton materialButton;
    public TrainingViewModel trainingViewModel;

    public HistoryViewModel historyViewModel;

    private List<ExEntity> exEntityList;

    private List<TrainingExCrossRef> crossList = new ArrayList<>();

    TrainingWithExs trainingWithExs;

    private Long longT;

    @SuppressLint({"SourceLockedOrientationActivity", "MissingInflatedId", "SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //createHistory();
        exEntityList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTraining);
        TrainingAdapter.OnExClickListener onClickListener = new TrainingAdapter.OnExClickListener(){
            @Override
            public void onExClick(ExEntity exEntity, int position) {
                if (!trainingViewModel.getAllExs().getValue().get(position).isSelect){
                    trainingViewModel.getAllExs().getValue().get(position).isSelect = true;
                    addExsInTraining(position);
                }
                else if(trainingViewModel.getAllExs().getValue().get(position).isSelect){
                    trainingViewModel.getAllExs().getValue().get(position).isSelect = false;
                    addExsInTraining(position);
                }
            }
        };

        final TrainingAdapter adapter = new TrainingAdapter(new TrainingAdapter.WorkoutDiff(), onClickListener); //?????

        materialButton = findViewById(R.id.materialButton);
        totalTimeText = findViewById(R.id.totalTimeText);
        setsText = findViewById(R.id.setsText);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise(v);
            }
        });


        trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra("exs") && intent.hasExtra("sets")) { //если передали

            sets = intent.getIntExtra("sets", 1); //получаем кол-во подходов
            exs = intent.getIntExtra("exs", 1); //получаем кол-во упр

            trainingViewModel.getAllExs().observe(this, words -> {
                // Update the cached copy of the words in the adapter.
                adapter.submitList(words);
            });

            //totalTime = trainingViewModel.getTimeEx(exs);
            totalTimeText.setText("Total Time: " + totalTime / 60 * sets + " Mins"); //устанавливаем текст

            setsText.setText(sets + " Sets");

        } else {
            Log.d(TAG, "getIntent: error");
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onWorkoutClick(int position) {
        Log.d(TAG, "onWorkoutClick: clicked");

    }

    //кнопка Start
    public void startExercise(View view) {
        createTraining();
        //переход на активность с Таймером отсчета
        Intent intent = new Intent(this, CountdownTimerActivity.class); // создание объекта Intent для запуска CountdownTimerActivity

        for (TrainingExCrossRef exsT: crossList){
            longT = historyViewModel.insert(exsT);
        }

        intent.putExtra("exs", exs); //передача кол-ва упражнений с ключом sets
        intent.putExtra("sets", sets); //передача кол-ва
        intent.putExtra("totalTime", totalTime);
        intent.putExtra("longList", longT);

        startActivity(intent); //переход, запуск
    }

    public void addExsInTraining(int position){

        if (trainingViewModel.getAllExs().getValue().get(position).isSelect){
            exEntityList.add(trainingViewModel.getAllExs().getValue().get(position));
        }
        else {
            exEntityList.remove(trainingViewModel.getAllExs().getValue().get(position));
        }
    }

    public void createTraining (){
        for (ExEntity exsT: exEntityList){
            TrainingExCrossRef trainingExCrossRef = new TrainingExCrossRef();
            trainingExCrossRef.idT = trainingViewModel.historyEntityFirst.idT;
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
