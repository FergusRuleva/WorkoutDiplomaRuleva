package com.example.workoutroom.history;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutroom.R;
import com.example.workoutroom.exercises.ExViewHolder;

import java.util.ArrayList;
import java.util.Date;

public class HistoryViewHolder extends RecyclerView.ViewHolder{

    private TextView tvDate;
    private TextView tvTimeAll;
    private TextView tvExs;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.tvDate);
        tvTimeAll = itemView.findViewById(R.id.tvTimeAll);
        tvExs = itemView.findViewById(R.id.tvExs);
    }

    public void bind(Date date, int timeAll, ArrayList<String> exs) {
        tvDate.setText((CharSequence) date);
        tvTimeAll.setText(timeAll);
        tvExs.setText((CharSequence) exs);
    }

    static HistoryViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_layout, parent, false);
        return new HistoryViewHolder(view);
    }

    public void delete(){

    }
}
