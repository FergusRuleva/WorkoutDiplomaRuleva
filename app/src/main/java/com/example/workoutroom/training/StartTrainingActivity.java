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

    private TextInputLayout setCountText; //слой кол-ва повторений
    private String setCount;
    private int exCountAll = 9;

    private String requiredText;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setCountText = findViewById(R.id.setCount);
        requiredText = this.getResources().getString(R.string.text_required);
    }

    //кнопка Выбора упражнения
    public void selectiveExMode(View view) {
        setCount = setCountText.getEditText().getText().toString();

        if (setCount.equals("")) { //если не введены ОБА значения
            setCountText.setError(requiredText);
        } else {
            setCountText.setError(null);

            //переход на активность с Выполнением упражнения
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("sets", Integer.parseInt(setCount)); //передача кол-ва подходов
            startActivity(intent); //переход
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setCountText.getEditText().clearFocus();
        setCountText.getEditText().setText("");
    }
}
