package com.example.jyodroid.kogichronometer;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.example.jyodroid.kogichronometer.custom_chronometer.CustomChronometerLayout;
import com.example.jyodroid.kogichronometer.model.Lap;

import java.util.ArrayList;

import static com.example.jyodroid.kogichronometer.ChronometerActivity.CHRONOMETER_LAPSE_TIME_KEY;
import static com.example.jyodroid.kogichronometer.ChronometerActivity.CHRONOMETER_PAUSED_LAPSE_TIME_KEY;
import static com.example.jyodroid.kogichronometer.ChronometerActivity.CHRONOMETER_PAUSED_TIME_KEY;
import static com.example.jyodroid.kogichronometer.ChronometerActivity.CHRONOMETER_START_TIME_KEY;
import static com.example.jyodroid.kogichronometer.ChronometerActivity.CHRONOMETER_STATE_KEY;
import static com.example.jyodroid.kogichronometer.ChronometerActivity.LAPS_LIST_KEY;

/**
 * Created by jyodroid on 11/28/16.
 */

class ChronometerPresenter {

    private ChronometerView mView;
    private Context mContext;

    ChronometerPresenter(ChronometerView view, Context context) {
        mView = view;
        mContext = context;
    }

    void runChronometer(int state) {
        if (state == CustomChronometerLayout.STATE_RUNNING) {
            mView.pauseChronometer();
        } else {
            mView.startChronometer();
        }
    }

    void stopChronometer() {
        mView.stopChronometer();
    }

    void addLap(int state, String elapsedTime, int lapsSize) {
        if (state == CustomChronometerLayout.STATE_RUNNING) {
            Lap lap = new Lap();
            lap.setLapTime(elapsedTime);
            lap.setLapNumber(lapsSize + 1);
            mView.addLap(lap);
        }
    }

    void resumeView(Bundle savedInstanceState) {

        ArrayList<Lap> lapsList = savedInstanceState.getParcelableArrayList(LAPS_LIST_KEY);
        mView.resumeLapList(lapsList);

        int chronometerState = savedInstanceState.getInt(CHRONOMETER_STATE_KEY);

        if (chronometerState == CustomChronometerLayout.STATE_RUNNING) {
            long startTime = savedInstanceState.getLong(CHRONOMETER_START_TIME_KEY);
            mView.resumeStart(startTime);
        }

        if (chronometerState == CustomChronometerLayout.STATE_PAUSED) {
            long pausedTime = savedInstanceState.getLong(CHRONOMETER_PAUSED_TIME_KEY);
            long startedTime = savedInstanceState.getLong(CHRONOMETER_START_TIME_KEY);
            long pausedLapseTime = savedInstanceState.getLong(CHRONOMETER_PAUSED_LAPSE_TIME_KEY);
            String stringTimeElapsed = savedInstanceState.getString(CHRONOMETER_LAPSE_TIME_KEY);
            mView.resumePause(pausedTime, startedTime, pausedLapseTime, stringTimeElapsed);
        }
    }

    void animateLapsList(View view, int position, int previousSize, int actualSize) {
        if (position == 0 && previousSize < actualSize) {
            view.setTranslationX(getScreenWidth() * -1);
            view.animate()
                    .translationX(0)
                    .setInterpolator(new DecelerateInterpolator(1.8f))
                    .setDuration(500)
                    .start();
        }
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.x;
    }
}
