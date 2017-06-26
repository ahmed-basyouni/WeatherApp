package com.ark.android.weatherapp.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import com.ark.android.weatherapp.BuildConfig;
import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.WeatherApp;
import com.ark.android.weatherapp.data.model.BaseModel;
import com.ark.android.weatherapp.data.network.NetworkListener;
import com.ark.android.weatherapp.threading.WeatherAppDataTask;

/**
 * The parent of all managers as of now it contain only two methods
 * {@link BaseManager#getObject(String, NetworkListener)} which make the networking calling
 * and {@link BaseManager#isNetworkConnected()} that indicate if there is internet connection
 * or no before make the network call
 * Created by Ark on 6/24/2017.
 */

public class BaseManager<T extends BaseModel> {

    private final Class<T> mClass;

    BaseManager(Class<T> clazz){
        this.mClass = clazz;
    }


    void getObject(String url, final NetworkListener<T> networkListener){
        final String fullUrl = getFullUrl(url);
        WeatherAppDataTask<T> weatherAppDataTask = new WeatherAppDataTask<>(networkListener, mClass);
        weatherAppDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fullUrl);
    }

    private String getFullUrl(String url) {
        return WeatherApp.getInstance().getString(R.string.baseUrl) + url + "&appid="+ BuildConfig.API_KEY + "&units=metric";
    }

    boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) WeatherApp.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
