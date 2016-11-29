package com.example.jyodroid.kogichronometer.custom_chronometer;

import com.example.jyodroid.kogichronometer.R;

import java.util.Locale;

/**
 * Created by jyodroid on 11/27/16.
 * <p>
 * Based on EMBEDONIX/AndroidChronometer
 * https://github.com/EMBEDONIX/AndroidChronometer
 */

class CustomChronometer implements Runnable {

    static final int STATE_STOPPED = R.attr.state_stopped;
    static final int STATE_RUNNING = R.attr.state_running;
    static final int STATE_PAUSED = R.attr.state_paused;

    private static final String EMPTY_TIME = "";
    private static final String TIME_FORMAT = "%02d:%02d:%03d";
    private static final String DEFAULT_TIME = "00:00:000";

    private static final long MILLIS_TO_MINUTES = 60000;
    private static final long MILLIS_TO_SECONDS = 1000;

    private static final long NO_STARTED_TIME = -1;

    private CustomChronometerPresenter mPresenter;
    private long mStartTime = NO_STARTED_TIME;
    private String mStringElapsedTime = EMPTY_TIME;
    private long mPausedTimeLapse;
    private long mPausedTime = NO_STARTED_TIME;
    private int mChronometerState;
    private boolean isPausedRestored;

    CustomChronometer(CustomChronometerPresenter presenter) {
        this.mPresenter = presenter;
    }

    void start() {
        if (mStartTime == NO_STARTED_TIME) {
            mStartTime = System.currentTimeMillis();
        } else {
            mStartTime = mStartTime + mPausedTimeLapse;
        }
        mChronometerState = STATE_RUNNING;
    }

    void stop() {
        mChronometerState = STATE_STOPPED;
    }

    void pause() {
        if (!isPausedRestored) {
            mPausedTime = System.currentTimeMillis();
        } else {
            isPausedRestored = false;
        }
        mChronometerState = STATE_PAUSED;
    }

    @Override
    public void run() {
        int minutes = 0;
        while (minutes <= 99 &&
                (mChronometerState == STATE_RUNNING || mChronometerState == STATE_PAUSED)) {
            if (mChronometerState == STATE_RUNNING) {
                long mElapsedTime = System.currentTimeMillis() - mStartTime;
                minutes = (int) ((mElapsedTime / MILLIS_TO_MINUTES) % 60);
                int seconds = (int) ((mElapsedTime / MILLIS_TO_SECONDS) % 60);
                int millis = (int) (mElapsedTime % 1000);
                mStringElapsedTime = String.format(TIME_FORMAT, minutes, seconds, millis, Locale.US);
                mPresenter.updateChronometer(mStringElapsedTime);
            } else {
                mPausedTimeLapse = System.currentTimeMillis() - mPausedTime;
                if ((System.currentTimeMillis() % 1000) < 500) {
                    mPresenter.updateChronometer(mStringElapsedTime);
                } else {
                    mPresenter.updateChronometer(EMPTY_TIME);
                }
            }
        }
        mPresenter.updateChronometer(DEFAULT_TIME);
        mStartTime = NO_STARTED_TIME;
    }

    long getStartTime() {
        return mStartTime;
    }

    void setStartTime(Long startTime) {
        mStartTime = startTime;
    }

    long getPausedTime() {
        return mPausedTime;
    }

    void setPausedTime(long pausedTime) {
        isPausedRestored = true;
        mPausedTime = pausedTime;
    }

    long getPausedTimeLapse() {
        return mPausedTimeLapse;
    }

    void setPausedTimeLapse(long pausedTimeLapse) {
        mPausedTimeLapse = pausedTimeLapse;
    }

    String getStringElapsedTime() {
        return mStringElapsedTime;
    }

    void setStringElapsedTime(String stringElapsedTime) {
        mStringElapsedTime = stringElapsedTime;
    }

    int getState() {
        return mChronometerState;
    }
}