package com.example.workoutroom.exercises;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.workoutroom.dataBase.data.ExEntity;

public class ExAdapter extends ListAdapter<ExEntity, ExViewHolder> {

    private String textSec;
    interface OnExClickListener{
        void onExClick(ExEntity exEntity, int position, View v);
    }

    private final OnExClickListener onClickListener;

    public ExAdapter(@NonNull DiffUtil.ItemCallback<ExEntity> diffCallback, OnExClickListener onClickListener, String textSec) {
        super(diffCallback);
        this.onClickListener = onClickListener;
        this.textSec = textSec;
    }

    @NonNull
    @Override
    public ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ExViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ExViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ExEntity current = getItem(position);
        holder.bind(current.getNameEx(), current.getDescriptionEx(), String.valueOf(current.getTimeEx()) + textSec, current.getImageEx());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onExClick(current, position, v);
            }
        });
    }

    public static class WorkoutDiff extends DiffUtil.ItemCallback<ExEntity>{

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
