package com.example.jyodroid.kogichronometer;

import com.example.jyodroid.kogichronometer.model.Lap;

import java.util.ArrayList;

/**
 * Created by jyodroid on 11/28/16.
 */

interface ChronometerView {
    void startChronometer();

    void stopChronometer();

    void pauseChronometer();

    void addLap(Lap lap);

    void resumeLapList(ArrayList<Lap> lapList);

    void resumeStart(long startedTime);

    void resumePause(long pausedTime, long startTime, Long pausedTimeLapse, String elapsedTime);
}
