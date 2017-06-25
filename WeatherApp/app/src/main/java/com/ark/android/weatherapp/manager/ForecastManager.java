package com.ark.android.weatherapp.manager;

import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.data.model.ForcastObj;
import com.ark.android.weatherapp.data.network.NetworkListener;
import com.ark.android.weatherapp.mvpContract.DetailsScreenContract;

import java.util.Locale;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public class ForecastManager extends BaseManager<ForcastObj> implements NetworkListener<ForcastObj>
                                , DetailsScreenContract.IDetailsModel{

    private DetailsScreenContract.IDetailsPresenter detailsPresenter;

    public ForecastManager() {
        super(ForcastObj.class);
    }

    @Override
    public void onSuccess(ForcastObj model) {
        detailsPresenter.onSuccess(model);
    }

    @Override
    public void onFail(AppException ex) {
        detailsPresenter.onFail(ex);
    }

    @Override
    public void getForecastForBookmarkObj(BookMarksObject bookMarksObject, DetailsScreenContract.IDetailsPresenter iDetailsPresenter) {
        this.detailsPresenter = iDetailsPresenter;
        if (isNetworkConnected())
            getObject(String.format(Locale.getDefault(), "/forecast?lat=%f&lon=%f", bookMarksObject.getLatitude(), bookMarksObject.getLongitude()), this);
        else
            onFail(new AppException(AppException.NETWORK_EXCEPTION));
    }
}
