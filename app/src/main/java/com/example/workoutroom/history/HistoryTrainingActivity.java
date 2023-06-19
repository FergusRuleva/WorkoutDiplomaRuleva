package com.example.workoutroom.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.Converters;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.dataBase.data.TrainingWithExs;
import com.example.workoutroom.exercises.AddNewExerciseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryTrainingActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    private String textMin, textSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.pink_color), PorterDuff.Mode.SRC_ATOP);
        setTitle(R.string.btn_history);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textMin = this.getResources().getString(R.string.text_holder_min);
        textSets = this.getResources().getString(R.string.text_holder_sets);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        HistoryAdapter.OnExClickListener onClickListener = new HistoryAdapter.OnExClickListener(){
            @Override
            public void onExClick(TrainingWithExs trainingWithExs, int position, View v) {
                showMenu(v, trainingWithExs);
            }
        };
        final HistoryAdapter adapter = new HistoryAdapter(new HistoryAdapter.WorkoutDiff(), onClickListener, historyViewModel.historyRepository.historyDao, textMin, textSets); //?????
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyViewModel.getAllHist().observe(this, trainingWithExs -> {
            //обновление кэшированной копии trainingWithExs в адаптере
            adapter.submitList(trainingWithExs);
        });

    }

    private void showMenu(View v, TrainingWithExs trainingWithExs) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_tr);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.option_1){
                    try {
                        sendTr(trainingWithExs); //отправка
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }else if(item.getItemId() == R.id.option_2){
                    deleteTr(trainingWithExs.historyEntity.idT); //удаление
                    return true;
                }else{
                    return false;
                }
            }
        });
        popup.show();
    }

    private void sendTr(TrainingWithExs trainingWithExs) throws IOException {

        //создание файла для записи необходимых данных
        Context context = getApplicationContext();
        File file = new File(context.getExternalFilesDir(null), "report.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(this.getResources().getString(R.string.text_report_date) + "\t" + trainingWithExs.historyEntity.getDateT() + "\n");
            writer.write(this.getResources().getString(R.string.text_report_time) + "\t" + trainingWithExs.historyEntity.getTimeT() + " Min\n");
            writer.write(this.getResources().getString(R.string.text_report_sets) + "\t" + trainingWithExs.historyEntity.getSetsT() + "\n");
            if (trainingWithExs.historyEntity.getDone()) {
                writer.write(this.getResources().getString(R.string.text_report_status_yes) + "\n");
            }else{
                writer.write(this.getResources().getString(R.string.text_report_status_no) + "\n");
            }
            writer.write(this.getResources().getString(R.string.text_report_list) + "\n");
            for (ExEntity exsT: trainingWithExs.exEntityList){
                writer.write(exsT.nameEx + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Получение содержимого файла
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[(int)file.length()];
        fis.read(byteArray);
        fis.close();
        String content = new String(byteArray);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND); //действие отправки данных
        sendIntent.putExtra(Intent.EXTRA_TEXT, content); //передача сформированного отчета
        sendIntent.setType("text/plain"); //данные и тип

        //отображение Android Sharesheet
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void deleteTr(long id){
        historyViewModel.deleteTraining(id);
        Context context = this; // если вызывается метод внутри самой активности
        // вызов метода recreate()
        if (context != null) {
            ((Activity) context).recreate();
        }
    }
}
