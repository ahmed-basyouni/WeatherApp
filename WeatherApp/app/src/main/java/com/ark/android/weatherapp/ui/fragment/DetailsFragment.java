package com.ark.android.weatherapp.ui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.model.BookMarksObject;

import java.text.DecimalFormat;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public class DetailsFragment extends Fragment {

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
    private BookMarksObject bookmarkObj;
    private int imageRes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_fragment, container, false);
        getObjectsFromBundle();
        initUI(rootView);
        setToolBar();
        setUIData();
        return rootView;
    }

    private void setUIData() {
        tempDegree.setText(String.format(getString(R.string.tempWithUnit),String.valueOf(bookmarkObj.getWeatherObj().getWeatherInfoObject().getTemp())));
        maxTemp.setText(String.format(getString(R.string.tempWithUnit),String.valueOf(bookmarkObj.getWeatherObj().getWeatherInfoObject().getTempMax())));
        minTemp.setText(String.format(getString(R.string.tempWithUnit),String.valueOf(bookmarkObj.getWeatherObj().getWeatherInfoObject().getTempMin())));
        if(bookmarkObj.getWeatherObj() != null && !bookmarkObj.getWeatherObj().getWeather().isEmpty())
           weatherTitle.setText(bookmarkObj.getWeatherObj().getWeather().get(0).getWeatherTitle());
        rain.setText(String.format(getString(R.string.rainVolume),bookmarkObj.getWeatherObj().getRain().getRainVolume()));
        wind.setText(String.format(getString(R.string.rainVolume),bookmarkObj.getWeatherObj().getWind().getSpeed()));
        humidity.setText(String.format(getString(R.string.humidity), bookmarkObj.getWeatherObj().getWeatherInfoObject().getHumidity()));
        collapseingToolbar.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
        bookmarkBanner.setImageResource(imageRes);
        weatherIcon.setImageDrawable(getDrawableForName(bookmarkObj.getWeatherObj().getWeather().get(0).getIcon()));
    }

    private Drawable getDrawableForName(String name){
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier("weather"+name, "drawable",
                getActivity().getPackageName());
        return ContextCompat.getDrawable(getActivity(), resourceId);
    }

    private void getObjectsFromBundle() {
        bookmarkObj = (BookMarksObject) getArguments().getSerializable(BOOKMARK_OBJ);
        imageRes = getArguments().getInt(IMAGE_RES);
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
    }

    private void setToolBar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(actionBar != null){

            actionBar.setTitle(bookmarkObj.getGeoAddress());
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
}
