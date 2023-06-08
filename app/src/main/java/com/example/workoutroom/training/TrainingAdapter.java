package com.example.workoutroom.training;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.exercises.ExAdapter;
import com.example.workoutroom.exercises.ExViewHolder;

public class TrainingAdapter extends ListAdapter<ExEntity, TrainingViewHolder> {

    private String textSec;
    interface OnExClickListener{
        void onExClick(ExEntity exEntity, int position);
    }

    private final OnExClickListener onClickListener;
    protected TrainingAdapter(@NonNull DiffUtil.ItemCallback<ExEntity> diffCallback, OnExClickListener onClickListener, String textSec) {
        super(diffCallback);
        this.onClickListener = onClickListener;
        this.textSec = textSec;
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TrainingViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ExEntity current = getItem(position);
        holder.bind(current.getNameEx(), String.valueOf(current.getTimeEx()) + textSec, current.getImageEx());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!current.getIsSelect()){
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFC8D5"));
                }else{
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                onClickListener.onExClick(current, position);
            }
        });
    }

    static class WorkoutDiff extends DiffUtil.ItemCallback<ExEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull ExEntity oldItem, @NonNull ExEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExEntity oldItem, @NonNull ExEntity newItem) {
            return oldItem.getNameEx().equals(newItem.getNameEx()); //?????
        }
    }
}
