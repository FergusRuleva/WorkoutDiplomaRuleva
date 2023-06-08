package com.example.workoutroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private int maxVolume;
    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Switch switchSound;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.pink_color), PorterDuff.Mode.SRC_ATOP);
        setTitle(R.string.btn_settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switchSound = findViewById(R.id.switchSound);

        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        SharedPreferences sharedPreferencesVolume = getSharedPreferences("volume", MODE_PRIVATE);

        switchSound.setChecked(sharedPreferences.getBoolean("value",true));

        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sharedPreferencesVolume.getInt("volume", maxVolume),0);

        switchSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchSound.isChecked())
                {
                    // When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchSound.setChecked(true);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sharedPreferencesVolume.getInt("volume", maxVolume), 0);
                }
                else
                {
                    // When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    switchSound.setChecked(false);

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
