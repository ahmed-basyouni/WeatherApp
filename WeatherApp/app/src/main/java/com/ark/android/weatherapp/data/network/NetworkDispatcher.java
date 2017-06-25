package com.ark.android.weatherapp.data.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A Network Dispatcher class that take a url and return json result
 * it doesn't know any information about our app and it through any exception to caller class
 * Created by ahmedb on 6/24/2017.
 */

public class NetworkDispatcher {

    public static String callWebService(String url) throws IOException {
        HttpURLConnection urlConnection = null;
        URL uri = new URL(url);
        urlConnection = (HttpURLConnection) uri.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
