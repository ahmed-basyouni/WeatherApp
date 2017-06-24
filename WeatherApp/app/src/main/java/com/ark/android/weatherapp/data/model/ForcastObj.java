package com.ark.android.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ark on 6/24/2017.
 */

public class ForcastObj extends BaseModel{

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

}
