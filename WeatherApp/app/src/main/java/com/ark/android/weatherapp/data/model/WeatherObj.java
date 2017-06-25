package com.ark.android.weatherapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class WeatherObj extends BaseModel implements Parcelable {

    @SerializedName("coord")
    @Expose
    private CoordObj coord;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    private WeatherInfoObject weatherInfoObject;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("rain")
    @Expose
    private RainObject rain;
    @SerializedName("dt")
    @Expose
    private long weatherDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dt_txt")
    @Expose
    private String dateText;

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public CoordObj getCoord() {
        return coord;
    }

    public void setCoord(CoordObj coord) {
        this.coord = coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public WeatherInfoObject getWeatherInfoObject() {
        return weatherInfoObject;
    }

    public void setWeatherInfoObject(WeatherInfoObject weatherInfoObject) {
        this.weatherInfoObject = weatherInfoObject;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public RainObject getRain() {
        return rain;
    }

    public void setRain(RainObject rain) {
        this.rain = rain;
    }

    public long getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(long weatherDate) {
        this.weatherDate = weatherDate;
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
        dest.writeParcelable(this.coord, flags);
        dest.writeTypedList(this.weather);
        dest.writeParcelable(this.weatherInfoObject, flags);
        dest.writeParcelable(this.wind, flags);
        dest.writeParcelable(this.rain, flags);
        dest.writeLong(this.weatherDate);
        dest.writeString(this.name);
        dest.writeString(this.dateText);
    }

    public WeatherObj() {
    }

    protected WeatherObj(Parcel in) {
        this.coord = in.readParcelable(CoordObj.class.getClassLoader());
        this.weather = in.createTypedArrayList(Weather.CREATOR);
        this.weatherInfoObject = in.readParcelable(WeatherInfoObject.class.getClassLoader());
        this.wind = in.readParcelable(Wind.class.getClassLoader());
        this.rain = in.readParcelable(RainObject.class.getClassLoader());
        this.weatherDate = in.readLong();
        this.name = in.readString();
        this.dateText = in.readString();
    }

    public static final Creator<WeatherObj> CREATOR = new Creator<WeatherObj>() {
        @Override
        public WeatherObj createFromParcel(Parcel source) {
            return new WeatherObj(source);
        }

        @Override
        public WeatherObj[] newArray(int size) {
            return new WeatherObj[size];
        }
    };
}
