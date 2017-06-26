package com.ark.android.weatherapp.data.network;

import com.ark.android.weatherapp.BuildConfig;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * Created by Ark on 6/24/2017.
 */
public class NetworkDispatcherTest {

    @Test
    public void testNetworkDispatcher() throws IOException, InterruptedException {
        NetworkDispatcher networkDispatcher = new NetworkDispatcher();
        String response = networkDispatcher.callWebService("http://api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&appid="+ BuildConfig.API_KEY);
        Thread.sleep(5000);
        Assert.assertNotNull(response);
    }
}