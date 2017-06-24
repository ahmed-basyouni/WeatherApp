package com.ark.android.weatherapp.threading;

import android.os.AsyncTask;
import android.util.Pair;

import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BaseModel;
import com.ark.android.weatherapp.data.network.NetworkDispatcher;
import com.ark.android.weatherapp.data.network.NetworkListener;
import com.ark.android.weatherapp.data.parsing.ParsingManager;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class WeatherAppDataTask<T extends BaseModel> extends AsyncTask<String, Void, Pair<AppException, T>> {

    private final NetworkListener<T> networkListener;
    private Class<T> mClass;

    public WeatherAppDataTask(NetworkListener<T> networkListener, Class<T> clazz){
        this.mClass = clazz;
        this.networkListener = networkListener;
    }

    @Override
    protected Pair<AppException, T> doInBackground(String... params) {

        try {
            String jsonResponse = NetworkDispatcher.callWebService(params[0]);
            return Pair.create(null, ParsingManager.getInstance().parseModel(jsonResponse, mClass));
        }catch(JsonSyntaxException e){
            e.printStackTrace();
            return Pair.create(AppException.getAppException(e), null);
        }catch (IOException e) {
            e.printStackTrace();
            return Pair.create(AppException.getAppException(e), null);
        }
    }

    @Override
    protected void onPostExecute(Pair<AppException, T> exceptionTPair) {
        super.onPostExecute(exceptionTPair);
        if(exceptionTPair.first != null)
            networkListener.onFail(exceptionTPair.first);
        else if(exceptionTPair.second != null)
            networkListener.onSuccess(exceptionTPair.second);
        else
            networkListener.onFail(new AppException(AppException.UNKNOWN_EXCEPTION));
    }
}
