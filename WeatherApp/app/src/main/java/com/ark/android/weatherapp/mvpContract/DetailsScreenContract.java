package com.ark.android.weatherapp.mvpContract;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.data.model.ForcastObj;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public interface DetailsScreenContract {

    interface IDetailsScreenView{
        RecyclerView getForecastList();
        Context getActivityContext();
        void setTemp(String temp);
        void setWeatherTitle(String title);
        void setMaxTemp(String maxTemp);
        void setMinTemp(String minTemp);
        void setRainVolume(String rainVolume);
        void setWindSpeed(String windSpeed);
        void setHumidity(String humidity);
        void setWeatherIcon(Drawable imageRes);
        void showErrorMsg(String error);
        void hideErrorMsg();
        void showProgressBar();
        void hideProgressBar();
        void setToolbarTitle(String title);
        void setweatherImage(int imageRes);
        void setExpandedTitleColor(int color);
    }

    interface IDetailsPresenter{
        void getDataFromBundle(Bundle bundle);
        void getForecastForBookmarkObj();
        void onSaveInstance(Bundle bundle);
        void onRestoreBundle(Bundle bundle);
        void onSuccess(ForcastObj forcastObj);
        void onFail(AppException ex);
    }

    interface IDetailsModel{
        void getForecastForBookmarkObj(BookMarksObject bookMarksObject, IDetailsPresenter iDetailsPresenter);
    }
}
