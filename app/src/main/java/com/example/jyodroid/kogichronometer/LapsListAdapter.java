package com.example.jyodroid.kogichronometer;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jyodroid.kogichronometer.model.Lap;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jyodroid on 11/28/16.
 */

class LapsListAdapter extends RecyclerView.Adapter<LapsListAdapter.ViewHolder> {

    private ArrayList<Lap> mLaps;

    private int mSize;
    private ChronometerPresenter mPresenter;

    LapsListAdapter(ChronometerPresenter presenter) {
        this.mLaps = new ArrayList<>();
        mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chronometer_lap, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lap lap = mLaps.get(position);
        holder.lapNumber.setText(String.valueOf(lap.getLapNumber()));
        holder.lapTime.setText(lap.getLapTime());

        mPresenter.animateLapsList(holder.container, position, mSize, mLaps.size());
        mSize = mLaps.size();
    }

    @Override
    public int getItemCount() {
        return mLaps.size();
    }

    void addLap(Lap lap) {
        mLaps.add(0, lap);
        notifyDataSetChanged();
    }

    void clearLaps() {
        mLaps.clear();
        mSize = 0;
        notifyDataSetChanged();
    }

    ArrayList<Lap> getLapsList(){
        return mLaps;
    }

    void setLapsList(ArrayList<Lap> laps){
        mSize = laps.size();
        mLaps = laps;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lap_number)
        AppCompatTextView lapNumber;

        @BindView(R.id.lap_time)
        AppCompatTextView lapTime;

        View container;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container = itemView;
        }
    }
}
