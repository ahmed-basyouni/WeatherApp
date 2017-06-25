package com.ark.android.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class Weather {
    @SerializedName("main")
    @Expose
    private String weatherTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getWeatherTitle() {
        return weatherTitle;
    }

    public void setWeatherTitle(String weatherTitle) {
        this.weatherTitle = weatherTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
