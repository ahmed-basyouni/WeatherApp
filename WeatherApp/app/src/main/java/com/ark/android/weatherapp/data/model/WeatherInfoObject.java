package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class WeatherInfoObject implements Parcelable {
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temp_min")
    @Expose
    private Double tempMin;
    @SerializedName("temp_max")
    @Expose
    private Double tempMax;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.temp);
        dest.writeValue(this.humidity);
        dest.writeValue(this.tempMin);
        dest.writeValue(this.tempMax);
    }

    public WeatherInfoObject() {
    }

    protected WeatherInfoObject(Parcel in) {
        this.temp = (Double) in.readValue(Double.class.getClassLoader());
        this.humidity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tempMin = (Double) in.readValue(Double.class.getClassLoader());
        this.tempMax = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeatherInfoObject> CREATOR = new Parcelable.Creator<WeatherInfoObject>() {
        @Override
        public WeatherInfoObject createFromParcel(Parcel source) {
            return new WeatherInfoObject(source);
        }

        @Override
        public WeatherInfoObject[] newArray(int size) {
            return new WeatherInfoObject[size];
        }
    };
}
