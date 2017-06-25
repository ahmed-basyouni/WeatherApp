package com.ark.android.weatherapp;

import android.app.Application;

import com.ark.android.weatherapp.data.cache.BookMarksUtils;

/**
 * Weather app application class
 * Created by Ark on 6/24/2017.
 */

public class WeatherApp extends Application {

    private static WeatherApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BookMarksUtils.init();
    }

    /**
     * A method to return the static context
     *
     * @return WeatherAppClass
     */
    public static WeatherApp getInstance() {
        return instance;
    }

}
