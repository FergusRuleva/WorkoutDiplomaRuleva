package com.example.workoutroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.exercises.AllExercisesActivity;
import com.example.workoutroom.history.HistoryTrainingActivity;
import com.example.workoutroom.training.StartTrainingActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTraining(View view) {
        //Log.d(TAG, "selectiveExMode: selected");
        //переход на активность
        Intent intent = new Intent(this, StartTrainingActivity.class);
        startActivity(intent); //переход
    }

    public void goTrainingHistory(View view) {
        //Log.d(TAG, "selectiveExMode: selected");
        //переход на активность
        Intent intent = new Intent(this, HistoryTrainingActivity.class);
        startActivity(intent); //переход
    }

    public void goAllExercises(View view) {
        //Log.d(TAG, "selectiveExMode: selected");
        //переход на активность
        Intent intent = new Intent(this, AllExercisesActivity.class);
        startActivity(intent); //переход
    }

    public void goSettings(View view) {
        //Log.d(TAG, "selectiveExMode: selected");
        //переход на активность
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent); //переход
    }
}