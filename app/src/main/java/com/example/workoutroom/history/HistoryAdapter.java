package com.example.workoutroom.history;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.workoutroom.dataBase.data.ExDatabase;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.HistoryDao;
import com.example.workoutroom.dataBase.data.HistoryEntity;
import com.example.workoutroom.dataBase.data.TrainingWithExs;
import com.example.workoutroom.exercises.ExAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends ListAdapter<TrainingWithExs, HistoryViewHolder> {


    private HistoryDao historyDao;

    private String textMin, textSets;


    interface OnExClickListener{
        void onExClick(TrainingWithExs trainingWithExs, int position, View v);
    }
    private final OnExClickListener onClickListener;

    //HistoryViewModel historyViewModel;
    protected HistoryAdapter(@NonNull DiffUtil.ItemCallback<TrainingWithExs> diffCallback, OnExClickListener onClickListener, HistoryDao historyDao, String textMin, String textSets) {
        super(diffCallback);
        this.onClickListener = onClickListener;
        this.historyDao = historyDao;
        this.textMin = textMin;
        this.textSets = textSets;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return HistoryViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TrainingWithExs current = getItem(position);
        HistoryEntity historyEntity = current.historyEntity;
        List<ExEntity> exEntityList = historyDao.getListExsByIdTr(historyEntity.idT);
        holder.bind(historyEntity.getDateT(), historyEntity.isDone, String.valueOf(historyEntity.timeT) + textMin, String.valueOf(historyEntity.setsT + 1) + textSets, exEntityList);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onExClick(current, position, v);
            }
        });
    }

    static class WorkoutDiff extends DiffUtil.ItemCallback<TrainingWithExs>{

        @Override
        public boolean areItemsTheSame(@NonNull TrainingWithExs oldItem, @NonNull TrainingWithExs newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TrainingWithExs oldItem, @NonNull TrainingWithExs newItem) {
            return oldItem.historyEntity.getDateT().equals(newItem.historyEntity.getDateT()); //?????
        }
    }
}
