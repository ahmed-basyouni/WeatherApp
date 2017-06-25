package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ark on 6/24/2017.
 */

public class ForcastObj extends BaseModel implements Parcelable {

    @SerializedName("list")
    @Expose
    List<WeatherObj> weatherObjs;
    @SerializedName("city")
    @Expose
    private City city;

    public List<WeatherObj> getWeatherObjs() {
        return weatherObjs;
    }

    public void setWeatherObjs(List<WeatherObj> weatherObjs) {
        this.weatherObjs = weatherObjs;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.weatherObjs);
        dest.writeParcelable(this.city, flags);
    }

    public ForcastObj() {
    }

    protected ForcastObj(Parcel in) {
        this.weatherObjs = new ArrayList<WeatherObj>();
        in.readList(this.weatherObjs, WeatherObj.class.getClassLoader());
        this.city = in.readParcelable(City.class.getClassLoader());
    }

    public static final Parcelable.Creator<ForcastObj> CREATOR = new Parcelable.Creator<ForcastObj>() {
        @Override
        public ForcastObj createFromParcel(Parcel source) {
            return new ForcastObj(source);
        }

        @Override
        public ForcastObj[] newArray(int size) {
            return new ForcastObj[size];
        }
    };
}
