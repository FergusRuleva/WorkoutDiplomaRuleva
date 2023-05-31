package com.example.workoutroom.training.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;
import com.example.workoutroom.history.HistoryViewModel;
import com.example.workoutroom.training.TrainingViewModel;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CountdownTimerActivity  extends AppCompatActivity {

    private CountDownTimer countDownTimer; //таймер обратного отсчета
    private TextView setsRemainingText, countdownTimerText, currentExText, nextExText, restText; //TV с активности
    private ImageView currentImg, nextImg; //пред. и след. упр
    private int sets; //кол-во подходов
    private int setsHistory;
    private boolean isTimerRunning, isBreak;
    private long timeLeftInMillis; //осталось времени
    int pos = 0; //для отслеживания смены упр
    private int totalTime = 0; //время тренировки
    private MaterialButton pauseBtn;
    public HistoryViewModel historyViewModel = null;
    public List<ExEntity> trainingWithExsList = new ArrayList<>();
    private int sizeListHistories;

    private String textCurrent, textNext, textSetsRemain, textDone;

    MediaPlayer mediaPlayer;

    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textCurrent = this.getResources().getString(R.string.text_current_set);
        textNext = this.getResources().getString(R.string.text_next_set);
        textSetsRemain = this.getResources().getString(R.string.text_sets_remaining_set);
        textDone = this.getResources().getString(R.string.text_done);

        //находим все объекты на активности
        setsRemainingText = findViewById(R.id.setsRemaining);
        countdownTimerText = findViewById(R.id.countdownTimerText);
        currentExText = findViewById(R.id.currentExName);
        nextExText = findViewById(R.id.nextExName);
        restText = findViewById(R.id.restText);
        pauseBtn = findViewById(R.id.pauseBtn);
        currentImg = findViewById(R.id.currentExImg);
        nextImg = findViewById(R.id.nextExImg);

        //модель взаимодействия с историей тренировки
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);


        //если передали
        if (getIntent().hasExtra("sets")
                && getIntent().hasExtra("totalTime")
                && getIntent().hasExtra("idTr")) {

            //получаем список и кол-во упр.и подходов
            setsHistory = sets;
            this.sets = getIntent().getIntExtra("sets", 2) - 1;
            this.totalTime = getIntent().getIntExtra("totalTime", 2);
            //получаем список выбранных упражнений
            trainingWithExsList = historyViewModel.getListExs(getIntent().getLongExtra("idTr", 0));

            setsRemainingText.setText(textSetsRemain + String.valueOf(sets)); //установка текста оставшихся повторов

            pos = 0; //при создании только начали

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                public void run() {
                    changeExercises(); //смена упр
                }
            });

            startTimer(); //запускаем таймер
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //обработка нажатия на Pause
                pauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pauseBtn.getText().toString().equals(textDone)) { //если завершено
                            finish(); //закрытие активности
                            //Intent intent = new Intent(CountdownTimerActivity.this, MainActivity.class);
                            //startActivity(intent); //переход
                        } else {
                            if (isTimerRunning) { //если таймер работает
                                pauseTimer(); //остановка таймера
                                pauseBtn.setText(R.string.btn_restart); //установка текста Повтора
                            } else {
                                startTimer(); //запуск таймера
                                pauseBtn.setText(R.string.btn_pause); //установка текста Пауза
                            }
                        }
                    }
                });
            }
        });
    }

    //метод для паузы таймера
    private void pauseTimer() {
        countDownTimer.cancel(); //остановка объекта таймера
        isTimerRunning = false; //таймер не работает

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    //метод запуска таймера
    private void startTimer() {
        pauseBtn.setVisibility(View.VISIBLE);
        restText.setText(R.string.text_keep_going); //установка текста Продолжать
        timeLeftInMillis = trainingWithExsList.get(pos).getTimeEx() * 1000; //получаем оставшееся время упражнения

        try {
            Thread.sleep(1000); //каждую секунду отсчет
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_sec);
        mediaPlayer.start();
        mediaPlayer.setScreenOnWhilePlaying(true);

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
                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                if (pos + 2 > trainingWithExsList.size()) { //если упр закончились
                    if (sets > 0) { //если кол-во повторений не закончилось
                        sets--; //-повтор
                        setsRemainingText.setText(textSetsRemain + String.valueOf(sets)); //устанавливаем текст оставшихся повторов
                        isTimerRunning = true; //таймер работает
                        pos = 0; //обнуление
                        try {
                            Thread.sleep(500); //на 0,5 с
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        breakTimer(); //приостановка таймера
                    } else {
                        sizeListHistories = historyViewModel.historyRepository.historyDao.getHistoryEntity().size();
                        @SuppressLint({"NewApi", "LocalSuppress"}) HistoryEntity historyEntity = new HistoryEntity(LocalDate.now().toString(), totalTime, sets);
                        historyEntity.idT = historyViewModel.historyRepository.historyDao.getHistoryEntity().get(sizeListHistories - 1).idT;
                        historyEntity.isDone = true;
                        historyViewModel.update(historyEntity);
                        isTimerRunning = false; //таймер не работает
                        //установка необходимого на активности для пользователя
                        countdownTimerText.setText(R.string.text_finished);
                        mediaPlayer.stop();
                        pauseBtn.setVisibility(View.VISIBLE);
                        pauseBtn.setText(R.string.text_done);
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

        mediaPlayer.stop();

        pauseBtn.setVisibility(View.INVISIBLE); //убираем кнопку паузы
        restText.setText(R.string.text_take_rest); //установка Отдохни

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

            //после окончания паузы = запуск таймера
            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
    }

    //смена упр
    private void changeExercises() {
        //для пред упр (текст + изоб)

        currentExText.setText(textCurrent + trainingWithExsList.get(pos).getNameEx());
        currentImg.setImageBitmap(trainingWithExsList.get(pos).getImageEx());

        if (pos + 1 < trainingWithExsList.size()) { //если еще остались упр
            //для след упр (текст + изоб)
            nextExText.setText(textNext + trainingWithExsList.get(pos + 1).getNameEx());
            nextImg.setImageBitmap(trainingWithExsList.get(pos + 1).getImageEx());
        } else { //если не осталось упр
            if (sets > 0) { //остались повторения
                //для след упр (текст + изоб), начало с первого упр в наборе
                nextExText.setText(textNext + trainingWithExsList.get(0).getNameEx());
                nextImg.setImageBitmap(trainingWithExsList.get(0).getImageEx());
            } else { //нет повторений
                //для след (упр нет, убираем изоб)
                nextExText.setText(R.string.text_no_ex);
                nextImg.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onStop(){
        super.onStop();
        mediaPlayer.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}