package com.ark.android.weatherapp.data.network;

import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BaseModel;

/**
 * A network listener that connect {@link com.ark.android.weatherapp.manager.BaseManager} and it's childes
 * Created by Ark on 6/24/2017.
 */

public interface NetworkListener<T extends BaseModel> {
    void onSuccess(T model);
    void onFail(AppException ex);
}
