package com.ark.android.weatherapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.mvpContract.DetailsScreenContract;
import com.ark.android.weatherapp.ui.presenter.DetailsScreenPresenter;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public class DetailsFragment extends Fragment implements DetailsScreenContract.IDetailsScreenView{

    public static final String BOOKMARK_OBJ = "bookmarkObj";
    public static final String IMAGE_RES = "imageRes";
    private CollapsingToolbarLayout collapseingToolbar;
    private Toolbar toolbar;
    private ImageView bookmarkBanner;
    private ImageView weatherIcon;
    private TextView tempDegree;
    private TextView weatherTitle;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView rain;
    private TextView wind;
    private TextView humidity;
    private RecyclerView forecastList;
    private ProgressBar progressView;
    private TextView errorView;
    private DetailsScreenPresenter detailsScreenPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_fragment, container, false);
        initUI(rootView);
        setToolBar();
        detailsScreenPresenter = new DetailsScreenPresenter(this);
        detailsScreenPresenter.getDataFromBundle(getArguments());
        if(savedInstanceState == null)
            detailsScreenPresenter.getForecastForBookmarkObj();
        else
            detailsScreenPresenter.onRestoreBundle(savedInstanceState);
        return rootView;
    }

    private void initUI(View rootView) {
        collapseingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        bookmarkBanner = (ImageView) rootView.findViewById(R.id.bookmarkBanner);
        weatherIcon = (ImageView) rootView.findViewById(R.id.weatherIcon);
        tempDegree = (TextView) rootView.findViewById(R.id.degree);
        weatherTitle = (TextView) rootView.findViewById(R.id.weatherTitle);
        maxTemp = (TextView) rootView.findViewById(R.id.maxTemp);
        minTemp = (TextView) rootView.findViewById(R.id.minTemp);
        rain = (TextView) rootView.findViewById(R.id.rain);
        wind = (TextView) rootView.findViewById(R.id.wind);
        humidity = (TextView) rootView.findViewById(R.id.hum);
        forecastList = (RecyclerView) rootView.findViewById(R.id.forecastList);
        progressView = (ProgressBar) rootView.findViewById(R.id.progressBar);
        errorView = (TextView) rootView.findViewById(R.id.errorView);
    }

    private void setToolBar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        detailsScreenPresenter.onSaveInstance(outState);
        super.onSaveInstanceState(outState);
    }



    @Override
    public RecyclerView getForecastList() {
        return forecastList;
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void setTemp(String temp) {
        tempDegree.setText(temp);
    }

    @Override
    public void setWeatherTitle(String title) {
        weatherTitle.setText(title);
    }

    @Override
    public void setMaxTemp(String maxTempString) {
        maxTemp.setText(maxTempString);
    }

    @Override
    public void setMinTemp(String minTempString) {
        minTemp.setText(minTempString);
    }

    @Override
    public void setRainVolume(String rainVolumeString) {
        rain.setText(rainVolumeString);
    }

    @Override
    public void setWindSpeed(String windSpeed) {
        wind.setText(windSpeed);
    }

    @Override
    public void setHumidity(String humidityString) {
        humidity.setText(humidityString);
    }

    @Override
    public void setWeatherIcon(Drawable imageRes) {
        weatherIcon.setImageDrawable(imageRes);
    }

    @Override
    public void showErrorMsg(String error) {
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(error);
    }

    @Override
    public void hideErrorMsg() {
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void setToolbarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle(title);
    }

    @Override
    public void setweatherImage(int imageRes) {
        bookmarkBanner.setImageResource(imageRes);
    }

    @Override
    public void setExpandedTitleColor(int color) {
        collapseingToolbar.setExpandedTitleColor(color);
    }
}
