package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class RainObject implements Parcelable {
    @SerializedName("3h")
    @Expose
    private Double rainVolume;

    public Double getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(Double rainVolume) {
        this.rainVolume = rainVolume;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.rainVolume);
    }

    public RainObject() {
    }

    protected RainObject(Parcel in) {
        this.rainVolume = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<RainObject> CREATOR = new Parcelable.Creator<RainObject>() {
        @Override
        public RainObject createFromParcel(Parcel source) {
            return new RainObject(source);
        }

        @Override
        public RainObject[] newArray(int size) {
            return new RainObject[size];
        }
    };
}
