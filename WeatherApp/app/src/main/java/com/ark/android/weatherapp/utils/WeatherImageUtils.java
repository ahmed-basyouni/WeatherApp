package com.ark.android.weatherapp.utils;

import com.ark.android.weatherapp.R;

/**
 *
 * Created by Ark on 6/26/2017.
 */

public class WeatherImageUtils {

    public static int getImageForWeather(String weatherTitle) {
        int drawableInt = R.drawable.day_clearsky;

        if(weatherTitle.contains("Rain") || weatherTitle.contains("Extreme")
                || weatherTitle.contains("Additional") || weatherTitle.contains("Thunderstorm") || weatherTitle.contains("Drizzle")){
            drawableInt = R.drawable.day_rain;
        } else if(weatherTitle.contains("Snow")){
            drawableInt = R.drawable.day_snow;
        }else if(weatherTitle.contains("Atmosphere")){
            drawableInt = R.drawable.day_fog;
        }else if(weatherTitle.contains("Clouds")){
            drawableInt = R.drawable.day_cloudy;
        }

        return drawableInt;
    }
}
