package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class City implements Parcelable {
    @SerializedName("coord")
    @Expose
    CoordObj coordObj;
    @SerializedName("name")
    @Expose
    private String name;

    public CoordObj getCoordObj() {
        return coordObj;
    }

    public void setCoordObj(CoordObj coordObj) {
        this.coordObj = coordObj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.coordObj,flags);
        dest.writeString(this.name);
    }

    public City() {
    }

    protected City(Parcel in) {
        this.coordObj = (CoordObj) in.readSerializable();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
