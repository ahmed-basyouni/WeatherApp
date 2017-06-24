package com.ark.android.weatherapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class City {
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
}
