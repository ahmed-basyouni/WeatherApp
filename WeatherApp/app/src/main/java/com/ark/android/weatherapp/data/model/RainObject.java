package com.ark.android.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ark on 6/24/2017.
 */

public class RainObject implements Serializable {
    @SerializedName("3h")
    @Expose
    private Double rainVolume;

    public Double getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(Double rainVolume) {
        this.rainVolume = rainVolume;
    }

}
