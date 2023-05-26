package com.example.workoutroom.training;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutroom.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class StartTrainingActivity extends AppCompatActivity {

    private TextInputLayout exCountText, //слой кол-ва упражнений
            setCountText; //слой кол-ва повторений
    private String setCount;
    private String exCount;
    private int exCountAll = 9;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        exCountText = findViewById(R.id.exerciseCount);
        setCountText = findViewById(R.id.setCount);
    }

    //кнопка Выбора упражнения
    public void selectiveExMode(View view) {
        //Log.d(TAG, "selectiveExMode: selected");

        exCount = exCountText.getEditText().getText().toString();
        setCount = setCountText.getEditText().getText().toString();

        if (exCount.equals("") && setCount.equals("")) { //если не введены ОБА значения
            exCountText.setError("Required"); //надпись Обязательно
            setCountText.setError("Required");

        } else if (exCount.equals("") || exCount.equals("0")) { //если не введено упражнение
            exCountText.setError("Required");
            setCountText.setError(null);

        } else if (setCount.equals("") || setCount.equals("0")) { //если не введено кол-во
            setCountText.setError("Required");
            exCountText.setError(null);
        } else {

            if (Integer.parseInt(exCount) > exCountAll) //если ввели больше
                exCount = "9";
            else if (Integer.parseInt(exCount) < 1) //если ввели меньше
                exCount = "1";

            setCountText.setError(null);
            exCountText.setError(null);



            //переход на активность с Выполнением упражнения
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("exs", Integer.parseInt(exCount)); //передача кол-ва упражнений
            intent.putExtra("sets", Integer.parseInt(setCount)); //передача кол-ва подходов
            startActivity(intent); //переход
        }
    }

    //кнопка Рандома
    public void randomExMode(View view) {
        //Log.d(TAG, "randomExMode: selected");

        setCount = setCountText.getEditText().getText().toString();

        if (setCount.equals("")) { //если не введено кол-во
            setCountText.setError("Required");
            exCountText.setError(null);
        } else {
            setCountText.setError(null);
            exCountText.setError(null);

            //ArrayList<Workout> randomExercises = new ArrayList<>(); //создание списка рандомных упражнений

            Random ran = new Random();
            //setWorkoutList(); //создание списка упражнений
            //randomExercises = workouts;
            //Collections.shuffle(randomExercises);

            //переход на активность с Выполнением упражнения
            Intent intent = new Intent(this, CheckoutActivity.class); // создание объекта Intent для запуска CheckoutActivity
            //intent.putParcelableArrayListExtra("workout", randomExercises); //передача списка с ключом workout
            //intent.putExtra("exs", Integer.parseInt(exCount)); //передача кол-ва упражнений
            //intent.putExtra("sets", Integer.parseInt(setCount)); //передача кол-ва подходов
            startActivity(intent); //переход, запуск
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        exCountText.getEditText().clearFocus(); //убрать фокус на слое
        exCountText.getEditText().setText(""); //очистка текста
        setCountText.getEditText().clearFocus();
        setCountText.getEditText().setText("");
    }
}
