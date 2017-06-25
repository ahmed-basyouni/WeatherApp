package com.ark.android.weatherapp.ui.presenter;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.data.model.ForcastObj;
import com.ark.android.weatherapp.manager.ForecastManager;
import com.ark.android.weatherapp.mvpContract.DetailsScreenContract;
import com.ark.android.weatherapp.ui.adapter.ForecastAdapter;
import com.ark.android.weatherapp.ui.fragment.DetailsFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public class DetailsScreenPresenter implements DetailsScreenContract.IDetailsPresenter {

    private static final String LIST_KEY = "forecastList";

    private final DetailsScreenContract.IDetailsScreenView detailsView;
    private ForcastObj forecastObj;
    private BookMarksObject bookmarkObj;

    public DetailsScreenPresenter(DetailsScreenContract.IDetailsScreenView iDetailsScreenView){
        this.detailsView = iDetailsScreenView;
    }

    @Override
    public void getDataFromBundle(Bundle bundle) {
        bookmarkObj = bundle.getParcelable(DetailsFragment.BOOKMARK_OBJ);
        int imageRes = bundle.getInt(DetailsFragment.IMAGE_RES);

        if(bookmarkObj != null){
            detailsView.setTemp(String.format(detailsView.getActivityContext().getString(R.string.tempWithUnit),String.valueOf(bookmarkObj.getWeatherObj().getWeatherInfoObject().getTemp())));
            detailsView.setMaxTemp(String.format(detailsView.getActivityContext().getString(R.string.tempWithUnit),String.valueOf(bookmarkObj.getWeatherObj().getWeatherInfoObject().getTempMax())));
            detailsView.setMinTemp(String.format(detailsView.getActivityContext().getString(R.string.tempWithUnit),String.valueOf(bookmarkObj.getWeatherObj().getWeatherInfoObject().getTempMin())));
            if(bookmarkObj.getWeatherObj() != null && !bookmarkObj.getWeatherObj().getWeather().isEmpty())
                detailsView.setWeatherTitle(bookmarkObj.getWeatherObj().getWeather().get(0).getWeatherTitle());
            detailsView.setRainVolume(String.format(detailsView.getActivityContext().getString(R.string.rainVolume),bookmarkObj.getWeatherObj().getRain().getRainVolume()));
            detailsView.setWindSpeed(String.format(detailsView.getActivityContext().getString(R.string.rainVolume),bookmarkObj.getWeatherObj().getWind().getSpeed()));
            detailsView.setHumidity(String.format(detailsView.getActivityContext().getString(R.string.humidity), bookmarkObj.getWeatherObj().getWeatherInfoObject().getHumidity()));
            detailsView.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
            detailsView.setweatherImage(imageRes);
            detailsView.setWeatherIcon(getDrawableForName(bookmarkObj.getWeatherObj().getWeather().get(0).getIcon()));
            detailsView.setToolbarTitle(bookmarkObj.getGeoAddress());
        }
    }

    private Drawable getDrawableForName(String name){
        Resources resources = detailsView.getActivityContext().getResources();
        final int resourceId = resources.getIdentifier("weather"+name, "drawable",
                detailsView.getActivityContext().getPackageName());
        return ContextCompat.getDrawable(detailsView.getActivityContext(), resourceId);
    }

    @Override
    public void getForecastForBookmarkObj() {
        detailsView.showProgressBar();
        new ForecastManager().getForecastForBookmarkObj(bookmarkObj, this);
    }

    @Override
    public void onSaveInstance(Bundle bundle) {
        bundle.putParcelable(LIST_KEY, forecastObj);
    }

    @Override
    public void onRestoreBundle(Bundle bundle) {
        forecastObj = bundle.getParcelable(LIST_KEY);
        if (forecastObj != null && !forecastObj.getWeatherObjs().isEmpty())
            setupAdapter(forecastObj);
        else
            getForecastForBookmarkObj();
    }

    @Override
    public void onSuccess(ForcastObj forcastObj) {
        this.forecastObj = forcastObj;
        detailsView.hideProgressBar();
        detailsView.hideErrorMsg();
        setupAdapter(forcastObj);
    }

    private void setupAdapter(ForcastObj forcastObj) {
        ForecastAdapter forecastAdapter = new ForecastAdapter(detailsView.getActivityContext(), forcastObj);
        detailsView.getForecastList().setLayoutManager(new LinearLayoutManager(detailsView.getActivityContext()));
        detailsView.getForecastList().setAdapter(forecastAdapter);
    }

    @Override
    public void onFail(AppException ex) {
        detailsView.hideProgressBar();
        detailsView.showErrorMsg(ex.getMessage());
    }
}
