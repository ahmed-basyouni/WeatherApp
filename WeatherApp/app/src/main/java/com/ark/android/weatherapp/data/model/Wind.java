package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class Wind implements Parcelable {
    @SerializedName("speed")
    @Expose
    private Double speed;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.speed);
    }

    public Wind() {
    }

    protected Wind(Parcel in) {
        this.speed = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel source) {
            return new Wind(source);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };
}
