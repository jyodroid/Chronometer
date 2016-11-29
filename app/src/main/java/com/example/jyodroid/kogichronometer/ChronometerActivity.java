package com.example.jyodroid.kogichronometer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jyodroid.kogichronometer.custom_chronometer.CustomChronometerLayout;
import com.example.jyodroid.kogichronometer.model.Lap;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChronometerActivity extends AppCompatActivity implements ChronometerView {

    @BindView(R.id.root_view)
    View mRootView;

    static final String LAPS_LIST_KEY = "laps_list";
    static final String CHRONOMETER_STATE_KEY = "chronometer_state";
    static final String CHRONOMETER_START_TIME_KEY = "chronometer_start_time";
    static final String CHRONOMETER_PAUSED_TIME_KEY = "chronometer_paused_time";
    static final String CHRONOMETER_PAUSED_LAPSE_TIME_KEY = "chronometer_paused_lapse_time";
    static final String CHRONOMETER_LAPSE_TIME_KEY = "chronometer_lapse_time";

    private ViewHolder mViewHolder;
    private ChronometerPresenter mPresenter;

    private LapsListAdapter mLapsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);

        ButterKnife.bind(this);
        mViewHolder = new ViewHolder(mRootView);
        mPresenter = new ChronometerPresenter(this, getBaseContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mViewHolder.lapListView.setLayoutManager(mLayoutManager);

        mLapsAdapter = new LapsListAdapter(mPresenter);
        if (savedInstanceState != null) {
            mPresenter.resumeView(savedInstanceState);
        }
        mViewHolder.lapListView.setAdapter(mLapsAdapter);
    }

    @Override
    public void startChronometer() {
        mViewHolder.chronometerView.setState(CustomChronometerLayout.STATE_RUNNING);
        mViewHolder.chronometerButton
                .setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause));
    }

    @Override
    public void pauseChronometer() {
        mViewHolder.chronometerView.setState(CustomChronometerLayout.STATE_PAUSED);
        mViewHolder.chronometerButton
                .setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play));
    }

    @Override
    public void addLap(Lap lap) {
        mLapsAdapter.addLap(lap);
    }

    @Override
    public void resumeLapList(ArrayList<Lap> lapList) {
        mLapsAdapter.setLapsList(lapList);
    }

    @Override
    public void resumeStart(long startedTime) {
        mViewHolder.chronometerView.setStartTime(startedTime);
        startChronometer();
    }

    @Override
    public void resumePause(long pausedTime, long startTime, Long pausedTimeLapse, String elapsedTime) {
        mViewHolder.chronometerView.setPausedTime(pausedTime);
        mViewHolder.chronometerView.setStartTime(startTime);
        mViewHolder.chronometerView.setPausedElapsedTime(pausedTime);
        mViewHolder.chronometerView.setStringElapseTime(elapsedTime);
        pauseChronometer();
    }

    @Override
    public void stopChronometer() {
        mViewHolder.chronometerView.setState(CustomChronometerLayout.STATE_STOPPED);
        mViewHolder.chronometerButton
                .setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play));
    }

    @OnClick(R.id.fab_start)
    void runChronometer() {
        mPresenter.runChronometer(mViewHolder.chronometerView.getState());
    }

    @OnClick(R.id.fab_stop)
    void finalizeChronometer() {
        mPresenter.stopChronometer();
        mLapsAdapter.clearLaps();
    }

    @OnClick(R.id.fab_add_lap_button)
    void addLapToList() {
        mPresenter.addLap(
                mViewHolder.chronometerView.getState(),
                mViewHolder.chronometerView.getStringElapseTime(),
                mLapsAdapter.getItemCount());
    }

    static class ViewHolder {

        @BindView(R.id.chronometer)
        CustomChronometerLayout chronometerView;

        @BindView(R.id.fab_start)
        FloatingActionButton chronometerButton;

        @BindView(R.id.laps_list)
        RecyclerView lapListView;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //Save Chronometer state
        outState.putInt(CHRONOMETER_STATE_KEY, mViewHolder.chronometerView.getState());
        outState.putLong(CHRONOMETER_START_TIME_KEY, mViewHolder.chronometerView.getStartTime());
        outState.putLong(CHRONOMETER_PAUSED_TIME_KEY, mViewHolder.chronometerView.getPausedTime());
        outState.putLong(CHRONOMETER_PAUSED_LAPSE_TIME_KEY, mViewHolder.chronometerView.getPausedElapsedTime());
        outState.putString(CHRONOMETER_LAPSE_TIME_KEY, mViewHolder.chronometerView.getStringElapseTime());

        //Save laps list
        outState.putParcelableArrayList(LAPS_LIST_KEY, mLapsAdapter.getLapsList());

        super.onSaveInstanceState(outState);
    }
}
