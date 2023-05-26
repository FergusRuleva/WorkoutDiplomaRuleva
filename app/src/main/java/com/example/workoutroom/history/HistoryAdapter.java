package com.example.workoutroom.history;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryEntity;

public class HistoryAdapter extends ListAdapter<HistoryEntity, HistoryViewHolder> {

    protected HistoryAdapter(@NonNull DiffUtil.ItemCallback<HistoryEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return HistoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryEntity current = getItem(position);
        //holder.bind(current.getDateT(), current.timeT, current.exsT);
    }

    static class WorkoutDiff extends DiffUtil.ItemCallback<HistoryEntity>{

        @Override
        public boolean areItemsTheSame(@NonNull HistoryEntity oldItem, @NonNull HistoryEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull HistoryEntity oldItem, @NonNull HistoryEntity newItem) {
            return oldItem.getDateT().equals(newItem.getDateT()); //?????
        }
    }
}
