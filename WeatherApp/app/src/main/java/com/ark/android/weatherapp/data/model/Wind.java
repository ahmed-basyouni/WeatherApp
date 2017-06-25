package com.ark.android.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class Wind {
    @SerializedName("speed")
    @Expose
    private Double speed;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}
