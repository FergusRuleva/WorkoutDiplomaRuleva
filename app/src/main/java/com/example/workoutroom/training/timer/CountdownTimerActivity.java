package com.example.workoutroom.training.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutroom.MainActivity;
import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;
import com.example.workoutroom.history.HistoryViewModel;
import com.example.workoutroom.training.TrainingViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountdownTimerActivity  extends AppCompatActivity {

    private CountDownTimer countDownTimer; //таймер обратного отсчета
    private TextView setsRemainingText, countdownTimerText, currentExText, nextExText, restText, currentDescription; //TV с активности
    private ImageView currentImg, nextImg; //пред. и след. упр
    private int sets; //кол-во подходов
    private boolean isTimerRunning, isBreak;
    private long timeLeftInMillis; //осталось времени
    int pos = 0; //для отслеживания упр (пред)
    private int exCountAll = 9;
    private int exs = 9; //кол-во упр
    private long totalTime = 0; //время тренировки
    private MaterialButton pauseBtn;
    public HistoryViewModel historyViewModel = null;
    public List<ExEntity> trainingWithExsList = new ArrayList<>();
    private Long longT;

    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //observeData();



        //находим все объекты на активности
        setsRemainingText = findViewById(R.id.setsRemaining);
        countdownTimerText = findViewById(R.id.countdownTimerText);
        currentExText = findViewById(R.id.currentExName);
        nextExText = findViewById(R.id.nextExName);
        restText = findViewById(R.id.restText);
        pauseBtn = findViewById(R.id.pauseBtn);
        currentImg = findViewById(R.id.currentExImg);
        nextImg = findViewById(R.id.nextExImg);
        currentDescription = findViewById(R.id.setsRemaining);

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        trainingWithExsList = historyViewModel.getTrainingWithEx().getValue().get(1).exEntityList;

        //если передали нужный список И кол-во упр
        if (getIntent().hasExtra("exs")
                && getIntent().hasExtra("sets")
                && getIntent().hasExtra("totalTime")
                && getIntent().hasExtra("longList")) {

            //historyViewModel.getListExs(historyViewModel.crossT.get());

            //получаем список и кол-во упр.и подходов
            this.sets = getIntent().getIntExtra("sets", 2) - 1;
            this.exs = getIntent().getIntExtra("exs", 2) - 1;
            this.totalTime = getIntent().getIntExtra("totalTime", 2) - 1;
            //crossList = historyViewModel.getCrossT();
            this.longT = getIntent().getLongExtra("longList", 2);

            setsRemainingText.setText("Sets Remaining: " + String.valueOf(sets)); //установка текста оставшихся упр

            pos = 0; //при создании только начали
            changeExercises(); //смена упр
            startTimer(); //запускаем таймер
        }

        //обработка нажатия на Pause
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pauseBtn.getText().toString().equals("Done")) { //если завершено
                    finish(); //закрытие активности
                    Intent intent = new Intent(CountdownTimerActivity.this, MainActivity.class);
                    startActivity(intent); //переход
                } else {
                    if (isTimerRunning) { //если таймер работает
                        pauseTimer(); //остановка таймера
                        pauseBtn.setText("Restart"); //установка текста Повтора
                    } else {
                        startTimer(); //запуск таймера
                        pauseBtn.setText("Pause"); //установка текста Пауза
                    }
                }
            }
        });
    }

    private void observeData(){
//        historyViewModel.getTrainingWithEx().observe(this, new Observer<List<TrainingWithExs>>() {
//            @Override
//            public void onChanged(List<TrainingWithExs> trainingWithExs) {
//                for (TrainingWithExs trainingWithExs1 : trainingWithExs) {
//                    trainingWithExsList.add(trainingWithExs1);
//                }
//            }
//        });
    }

    //метод для паузы таймера
    private void pauseTimer() {
        countDownTimer.cancel(); //остановка объекта таймера
        isTimerRunning = false; //таймер не работает
    }

    //метод запуска таймера
    private void startTimer() {
        pauseBtn.setVisibility(View.VISIBLE);
        restText.setText("Keep Going"); //установка текста Продолжать
        //timeLeftInMillis = trainingViewModel.getEx(pos).getTimeEx() * 1000; //получаем оставшееся время

        try {
            Thread.sleep(1000); //каждую секунду отсчет
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //запуск обратного отсчета
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            //обновление времени
            @Override
            public void onTick(long millisUntilFinished) { //мс до завершения
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            //после окончания отсчета
            @Override
            public void onFinish() {
                if (pos + 2 > exs) { //если упр закончились
                    if (sets > 0) { //если кол-во повторений не закончилось
                        sets--; //-повтор
                        setsRemainingText.setText("Sets Remaining: " + String.valueOf(sets)); //устанавливаем текст оставшихся повторов
                        isTimerRunning = true; //таймер работает
                        pos = 0; //обнуление
                        try {
                            Thread.sleep(500); //на 0,5 с
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        breakTimer(); //приостановка таймера
                    } else {
                        isTimerRunning = false; //таймер не работает
                        //установка необходимого на активности для пользователя
                        countdownTimerText.setText("Finished!");
                        pauseBtn.setVisibility(View.VISIBLE);
                        pauseBtn.setText("Done");
                        restText.setVisibility(View.INVISIBLE);
                        setsRemainingText.setVisibility(View.INVISIBLE);
                        currentImg.setVisibility(View.GONE);

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                } else {
                    isTimerRunning = true; //таймер работает
                    pos++; //+упр
                    try {
                        Thread.sleep(500); //на 0,5 с
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    breakTimer(); //приостановка таймера
                }
            }
        }.start();

        isTimerRunning = true; //таймер работает
    }

    //обновление обратного отсчета
    private void updateCountDownText() {
        //мин + сек
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds); //форматируем (целое число с 2 цифрами)
        countdownTimerText.setText(timeLeftFormatted); //установка оставшегося времени
    }

    //приостановка таймера
    private void breakTimer() {
        long breakTime = 10000; //время остановки 40000
        isBreak = true; //остановлено

        pauseBtn.setVisibility(View.INVISIBLE); //убираем кнопку паузы
        restText.setText("Take Rest"); //установка Отдохни

        changeExercises(); //смена упр

        try {
            Thread.sleep(1000); //каждую сек
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //запуск обратного отсчета
        CountDownTimer countDownTimer = new CountDownTimer(breakTime, 1000) {
            //обновление времени
            @Override
            public void onTick(long millisUntilFinished) { //мс до завершения
                //мин + сек
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds); //форматируем (целое число с 2 цифрами)
                countdownTimerText.setText(timeLeftFormatted); //установка оставшегося времени
            }

            //после окончания = запуск таймера
            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
    }

    //смена упр
    private void changeExercises() {
        //для пред упр (текст + изоб)
        //trainingWithExsList.get(pos).exEntityList.get()
        //historyViewModel.getListExs(historyViewModel.getTrainingWithEx().getValue().)

        //currentExText.setText("Current: " + trainingWithExsList.get(longT).getNameEx());
        currentImg.setImageBitmap(trainingWithExsList.get(pos).getImageEx());
        currentDescription.setText(trainingWithExsList.get(pos).getDescriptionEx());

        if (pos + 1 < exs + 1) { //если еще остались упр
            //для след упр (текст + изоб)
            nextExText.setText("Next: " + trainingWithExsList.get(pos + 1).getNameEx());
            nextImg.setImageBitmap(trainingWithExsList.get(pos + 1).getImageEx());
        } else { //если не осталось упр
            if (sets > 0) { //остались повторения
                //для след упр (текст + изоб), начало с первого упр в наборе
                nextExText.setText("Next: " + trainingWithExsList.get(0).getNameEx());
                nextImg.setImageBitmap(trainingWithExsList.get(0).getImageEx());
            } else { //нет повторений
                //для след (упр нет, убираем изоб)
                nextExText.setText("No Exercises Left");
                nextImg.setVisibility(View.GONE);
            }
        }
        //trainingViewModel.getExs(exs);
    }


    @Override
    protected void onStop(){
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}