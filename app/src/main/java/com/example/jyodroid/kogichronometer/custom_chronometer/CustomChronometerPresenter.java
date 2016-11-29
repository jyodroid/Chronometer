package com.example.jyodroid.kogichronometer.custom_chronometer;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by jyodroid on 11/27/16.
 */

class CustomChronometerPresenter {

    private static final String THREAD_NAME = "CHRONOMETER_THREAD";

    private CustomChronometer mCustomChronometer;
    private HandlerThread mChronometerThreadHandler;
    private Handler mThreadChronometer;
    private CustomChronometerView mView;
    private Handler mUIHandler;

    CustomChronometerPresenter(CustomChronometerView view, Context context) {
        this.mCustomChronometer = new CustomChronometer(this);
        this.mView = view;
        this.mUIHandler = new Handler(context.getMainLooper());
    }

    void starChronometer() {
        if (mCustomChronometer.getState() != CustomChronometer.STATE_RUNNING) {
            mCustomChronometer.start();
            startThread();
        }
    }

    void pauseChronometer() {

        if (mCustomChronometer.getState() != CustomChronometer.STATE_PAUSED) {
            mCustomChronometer.pause();
            startThread();
        }
    }

    void stopChronometer() {
        if (mCustomChronometer.getState() != CustomChronometer.STATE_STOPPED) {
            mCustomChronometer.stop();
            clearChronometer();
        }
    }

    public void clearChronometer() {
        if (null != mChronometerThreadHandler) {
            mChronometerThreadHandler.quit();
            mChronometerThreadHandler = null;
            mThreadChronometer.removeCallbacksAndMessages(null);
            mThreadChronometer = null;
            mCustomChronometer.stop();
        }
    }

    private void startThread() {
        if (mChronometerThreadHandler == null) {
            mChronometerThreadHandler = new HandlerThread(THREAD_NAME);
            mChronometerThreadHandler.start();
            mThreadChronometer = new Handler(mChronometerThreadHandler.getLooper());
            mThreadChronometer.post(mCustomChronometer);
        }
    }

    void updateChronometer(final String time) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                mView.updateChronometer(time);
            }
        });
    }

    long getStartTime() {
        return mCustomChronometer.getStartTime();
    }

    void setStartTime(long timeInMillis) {
        mCustomChronometer.setStartTime(timeInMillis);
    }

    String getStringElapseTime() {
        return mCustomChronometer.getStringElapsedTime();
    }

    void setStringElapseTime(String stringElapseTime) {
        mCustomChronometer.setStringElapsedTime(stringElapseTime);
    }

    long getPausedElapsedTime() {
        return mCustomChronometer.getPausedTimeLapse();
    }

    long getPausedTime() {
        return mCustomChronometer.getPausedTime();
    }

    void setPausedElapsedTime(long pausedElapsedTime) {
        mCustomChronometer.setPausedTimeLapse(pausedElapsedTime);
    }

    void setPausedTime(long pausedTime) {
        mCustomChronometer.setPausedTime(pausedTime);
    }

}
