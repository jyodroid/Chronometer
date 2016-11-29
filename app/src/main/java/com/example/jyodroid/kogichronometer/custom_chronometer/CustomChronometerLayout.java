package com.example.jyodroid.kogichronometer.custom_chronometer;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.jyodroid.kogichronometer.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jyodroid on 11/27/16.
 */

public class CustomChronometerLayout extends AppCompatTextView implements CustomChronometerView {

    public static final int STATE_STOPPED = R.attr.state_stopped;
    public static final int STATE_RUNNING = R.attr.state_running;
    public static final int STATE_PAUSED = R.attr.state_paused;

    @IntDef({STATE_PAUSED, STATE_RUNNING, STATE_STOPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChronometerState {
    }

    private int mState;
    private CustomChronometerPresenter mPresenter;

    public CustomChronometerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mPresenter = new CustomChronometerPresenter(this, context);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        switch (mState) {
            case STATE_PAUSED:
                mPresenter.pauseChronometer();
                return mergeStates(extraSpace, STATE_PAUSED);
            case STATE_RUNNING:
                mPresenter.starChronometer();
                return mergeStates(extraSpace, STATE_RUNNING);
            case STATE_STOPPED:
                mPresenter.stopChronometer();
                return mergeStates(extraSpace, STATE_STOPPED);
            default:
                return super.onCreateDrawableState(extraSpace);
        }
    }

    private int[] mergeStates(int extraSpace, int state) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        int[] stateArray = {state};
        mergeDrawableStates(drawableState, stateArray);
        return drawableState;
    }

    /**
     * Allows change the chronometer specific state of the custom {@link CustomChronometerLayout}
     *
     * @param state {@link ChronometerState} for the view
     */
    public void setState(@ChronometerState int state) {
        mState = state;
        refreshDrawableState();
    }

    /**
     * Allows to obtain the chronometer specific state of the custom {@link CustomChronometerLayout}
     *
     * @return {@link ChronometerState} for the view
     */
    public int getState() {
        return mState;
    }

    /**
     * Return the start time of the chronometer
     *
     * @return chronometer start time in milliseconds
     */
    public long getStartTime() {
        return mPresenter.getStartTime();
    }

    /**
     * Allows an user defined start time for the chronometer
     *
     * @param timeInMillis chronometer start time in milliseconds
     */
    public void setStartTime(long timeInMillis) {
        mPresenter.setStartTime(timeInMillis);
    }

    /**
     * Return the actual time lapse of the chronometer
     *
     * @return chronometer time lapse in String representation "mm:ss:MMM"
     */
    public String getStringElapseTime() {
        return mPresenter.getStringElapseTime();
    }

    /**
     * Set the string to show in the chronometer in pased state
     *
     * @param stringElapseTime string with the previous shown time
     */
    public void setStringElapseTime(String stringElapseTime) {
        mPresenter.setStringElapseTime(stringElapseTime);
    }

    /**
     * Returns the time when the chronometer has been stopped
     *
     * @return time in millisecond
     */
    public long getPausedTime() {
        return mPresenter.getPausedTime();
    }

    /**
     * Set the time since when the chronometer has been paused
     *
     * @param pausedElapsedTime time in milliseconds
     */
    public void setPausedElapsedTime(long pausedElapsedTime) {
        mPresenter.setPausedElapsedTime(pausedElapsedTime);
    }

    /**
     * Returns the time lapse that the chronometer has been paused
     *
     * @return time in milliseconds
     */
    public long getPausedElapsedTime() {
        return mPresenter.getPausedElapsedTime();
    }

    /**
     * Set the time when the chronometer has been paused
     *
     * @param pausedTime time in milliseconds
     */
    public void setPausedTime(long pausedTime) {
        mPresenter.setPausedTime(pausedTime);
    }

    @Override
    public void updateChronometer(String time) {
        setText(time);
    }

    @Override
    protected void onDetachedFromWindow() {
        mPresenter.clearChronometer();
        super.onDetachedFromWindow();
    }
}
