package com.ark.android.weatherapp.manager;

import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.data.model.WeatherObj;

import java.util.Locale;

/**
 *
 * Created by Ark on 6/26/2017.
 */

public class BookmarkUpdateManager extends BookmarkManager {

    private BookMarksObject bookmarkObject;

    public void silentUpdate(BookMarksObject bookMarksObject) {
        this.bookmarkObject = bookMarksObject;
        if (isNetworkConnected()) {
            getObject(String.format(Locale.getDefault(), "/weather?lat=%f&lon=%f", bookMarksObject.getLatitude(), bookMarksObject.getLongitude()), this);
        }
    }

    /**
     * when retrieving of weather info succeed add them to bookmark object and then update database
     * @param model
     */
    @Override
    public void onSuccess(WeatherObj model) {
        bookmarkObject.setWeatherObj(model);
        updateBookmarkObject(bookmarkObject);
    }

    /**
     * on fail mark {@link BookMarksObject} updating state as fail so View would handle it
     * in our case it show retry button
     * @param ex
     */
    @Override
    public void onFail(AppException ex) {
        updateBookmarkObject(bookmarkObject);
    }
}
