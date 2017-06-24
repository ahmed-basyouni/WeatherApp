package com.ark.android.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class WeatherObj extends BaseModel{

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

}
