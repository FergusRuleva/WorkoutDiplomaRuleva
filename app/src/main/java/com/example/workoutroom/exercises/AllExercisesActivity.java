package com.example.workoutroom.exercises;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.google.android.material.card.MaterialCardView;

public class AllExercisesActivity extends AppCompatActivity {

    public ExViewModel exViewModel;
    private Button addNewExButton;

    private MaterialCardView materialCardView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_exercises);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllEx);
        ExAdapter.OnExClickListener onClickListener = new ExAdapter.OnExClickListener(){
            @Override
            public void onExClick(ExEntity exEntity, int position) {
                Toast.makeText(getApplicationContext(), "Был выбран пункт " + exEntity.getNameEx(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        final ExAdapter adapter = new ExAdapter(new ExAdapter.WorkoutDiff(), onClickListener); //?????
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

//        materialCardView = findViewById(R.id.materialCardViewEx);
//        materialCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMenu(v);
//            }
//        });
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

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_ex);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 0:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали PopupMenu 1",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case 1:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали PopupMenu 2",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popup.show();
    }

}