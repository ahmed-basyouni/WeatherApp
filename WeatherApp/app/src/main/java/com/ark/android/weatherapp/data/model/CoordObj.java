package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

class CoordObj implements Parcelable {

    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("lat")
    @Expose
    private Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.lon);
        dest.writeValue(this.lat);
    }

    public CoordObj() {
    }

    protected CoordObj(Parcel in) {
        this.lon = (Double) in.readValue(Double.class.getClassLoader());
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<CoordObj> CREATOR = new Parcelable.Creator<CoordObj>() {
        @Override
        public CoordObj createFromParcel(Parcel source) {
            return new CoordObj(source);
        }

        @Override
        public CoordObj[] newArray(int size) {
            return new CoordObj[size];
        }
    };
}
