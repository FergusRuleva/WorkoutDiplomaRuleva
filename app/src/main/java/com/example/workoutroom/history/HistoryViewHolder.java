package com.example.workoutroom.history;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.example.workoutroom.dataBase.data.TrainingExCrossRef;
import com.example.workoutroom.exercises.ExViewHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryViewHolder extends RecyclerView.ViewHolder{

    private final TextView tvDate;
    private final TextView tvDone;
    private final TextView tvTimeAll;
    private final TextView tvSets;
    private final TextView tvExs;
    private Context context;
    private List<String> exNameEntityList = new ArrayList<>();
    private int countExName = 0;
    private String tvExsS = "";

    public HistoryViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvDate = itemView.findViewById(R.id.tvDate);
        tvDone = itemView.findViewById(R.id.tvDone);
        tvTimeAll = itemView.findViewById(R.id.tvTimeAll);
        tvSets = itemView.findViewById(R.id.tvSets);
        tvExs = itemView.findViewById(R.id.tvExs);
    }

    public void bind(String date, boolean isDone, String timeAll, String sets, List<ExEntity> exEntityList) {
        tvDate.setText(date);
        if(isDone){
            tvDone.setText(R.string.text_done);
            tvDone.setTextColor(ContextCompat.getColor(context, R.color.done_color));
        }else{
            tvDone.setText(R.string.text_undone);
            tvDone.setTextColor(ContextCompat.getColor(context, R.color.undone_color));
        }
        tvTimeAll.setText(timeAll);
        tvSets.setText(sets);
        for (ExEntity exsT: exEntityList){
            this.exNameEntityList.add(exsT.nameEx);
            tvExsS += exNameEntityList.get(countExName);
            if (countExName < exEntityList.size()){
                tvExsS += "\n";
            }
            countExName++;
        }
        tvExs.setText(tvExsS);
    }

    static HistoryViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_layout, parent, false);
        return new HistoryViewHolder(view, view.getContext());
    }
}
