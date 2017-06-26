package com.ark.android.weatherapp.threading;

import android.util.Pair;

import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.WeatherObj;
import com.ark.android.weatherapp.data.network.NetworkListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

/**
 *
 * Created by Ark on 6/24/2017.
 */
@RunWith(RobolectricTestRunner.class)
public class WeatherAppDataTaskTest {

    @Test
    public void testAsyncTaskSuccess() throws InterruptedException {
        NetworkListener networkListener = Mockito.mock(NetworkListener.class);
        WeatherAppDataTask<WeatherObj> weatherAppDataTask = new WeatherAppDataTask<>(networkListener,WeatherObj.class);
        WeatherObj weatherObj = new WeatherObj();
        Pair<AppException, WeatherObj> pair = Pair.create(null, weatherObj);
        weatherAppDataTask.onPostExecute(pair);
        Mockito.verify(networkListener, Mockito.times(1)).onSuccess(weatherObj);
    }

    @Test
    public void testAsyncTaskFail() throws InterruptedException {
        NetworkListener networkListener = Mockito.mock(NetworkListener.class);
        WeatherAppDataTask<WeatherObj> weatherAppDataTask = new WeatherAppDataTask<>(networkListener,WeatherObj.class);
        Pair<AppException, WeatherObj> pair = Pair.create(new AppException(AppException.JSON_PARSING_EXCEPTION), null);
        weatherAppDataTask.onPostExecute(pair);
        Mockito.verify(networkListener, Mockito.times(1)).onFail(ArgumentMatchers.any(AppException.class));
    }
}