package com.example.jyodroid.kogichronometer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jyodroid on 11/28/16.
 */

public class Lap implements Parcelable{
    private int lapNumber;
    private String lapTime;

    public Lap(){}

    protected Lap(Parcel in) {
        lapNumber = in.readInt();
        lapTime = in.readString();
    }

    public static final Creator<Lap> CREATOR = new Creator<Lap>() {
        @Override
        public Lap createFromParcel(Parcel in) {
            return new Lap(in);
        }

        @Override
        public Lap[] newArray(int size) {
            return new Lap[size];
        }
    };

    public int getLapNumber() {
        return lapNumber;
    }

    public void setLapNumber(int lapNumber) {
        this.lapNumber = lapNumber;
    }

    public String getLapTime() {
        return lapTime;
    }

    public void setLapTime(String lapTime) {
        this.lapTime = lapTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lapNumber);
        dest.writeString(lapTime);
    }
}
