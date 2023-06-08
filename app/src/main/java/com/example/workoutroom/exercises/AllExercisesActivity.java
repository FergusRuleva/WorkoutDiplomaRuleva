package com.example.workoutroom.exercises;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.Converters;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.google.android.material.card.MaterialCardView;

public class AllExercisesActivity extends AppCompatActivity {

    private ExViewModel exViewModel;
    private Button addNewExButton;
    private String textSec;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_exercises);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.pink_color), PorterDuff.Mode.SRC_ATOP);
        setTitle(R.string.btn_all_exs);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textSec = this.getResources().getString(R.string.text_holder_sec);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllEx);
        ExAdapter.OnExClickListener onClickListener = new ExAdapter.OnExClickListener(){
            @Override
            public void onExClick(ExEntity exEntity, int position, View v) {
                showMenu(v, exEntity);
            }
        };
        final ExAdapter adapter = new ExAdapter(new ExAdapter.WorkoutDiff(), onClickListener, textSec); //?????
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exViewModel = new ViewModelProvider(this).get(ExViewModel.class);
        exViewModel.getAllEx().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });

        addNewExButton = findViewById(R.id.addNewEx);
        addNewExButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AllExercisesActivity.this, AddNewExerciseActivity.class), 100);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK){

        }else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showMenu(View v, ExEntity exEntity) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_ex);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.option_1){
                            changeEx(exEntity);
                            return true;
                        }else if(item.getItemId() == R.id.option_2){
                            deleteEx(exEntity);
                            return true;
                        }else{
                            return false;
                        }
                    }
                });
        popup.show();
    }

    private void changeEx(ExEntity exEntity){
        //переход на активность с Выполнением упражнения
        Intent intent = new Intent(this, AddNewExerciseActivity.class);
        intent.putExtra("id", exEntity.idEx);
        intent.putExtra("name", exEntity.nameEx);
        intent.putExtra("descr", exEntity.descriptionEx);
        intent.putExtra("time", exEntity.timeEx);
        intent.putExtra("image", Converters.fromBitmap(exEntity.imageEx));
        //startActivity(intent); //переход
        startActivityForResult(intent, 100);
    }

    private void deleteEx(ExEntity exEntity){
        exViewModel.delete(exEntity);
    }
}