package com.example.workoutroom.training;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutroom.R;
import com.example.workoutroom.exercises.ExViewHolder;

public class TrainingViewHolder extends RecyclerView.ViewHolder {

    TextView set, time, name, description;
    ImageView image;
    static TrainingAdapter.OnWorkoutListener onWorkoutListener;


    public TrainingViewHolder(@NonNull View itemView) {
        super(itemView);
        //this.onWorkoutListener = onWorkoutListener;
        time = itemView.findViewById(R.id.cardExTime);
        name = itemView.findViewById(R.id.cardExName);
        description = itemView.findViewById(R.id.cardExDescription);
        image = itemView.findViewById(R.id.cardExImg);
        //itemView.setOnClickListener((View.OnClickListener) this);
    }

    public void bind(String nameEx, String descriptionEx, int timeEx, Bitmap imageEx) {
        name.setText(nameEx);
        description.setText(descriptionEx);
        String s = String.valueOf(timeEx);
        time.setText(s + " Sec");
        image.setImageBitmap(imageEx);
    }

    static TrainingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_training_layout, parent, false);
        return new TrainingViewHolder(view);
    }

    public void delete(){

    }
}