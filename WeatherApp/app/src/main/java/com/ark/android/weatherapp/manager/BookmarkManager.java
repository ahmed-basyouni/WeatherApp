package com.ark.android.weatherapp.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ark.android.weatherapp.WeatherApp;
import com.ark.android.weatherapp.data.cache.BookMarksContentProvider;
import com.ark.android.weatherapp.data.cache.BookMarksDataBaseHelper;
import com.ark.android.weatherapp.data.cache.BookMarksUtils;
import com.ark.android.weatherapp.data.exception.AppException;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.data.model.WeatherObj;
import com.ark.android.weatherapp.data.network.NetworkListener;
import com.ark.android.weatherapp.mvpContract.BookmarksListContract;

import java.util.Locale;

/**
 * The manager responsible for interacting with Contentprovider and also retrieve {@link BookMarksObject}
 * information from the Server Api
 * Created by Ark on 6/24/2017.
 */

public class BookmarkManager extends BaseManager<WeatherObj>
        implements NetworkListener<WeatherObj>
        , BookmarksListContract.IBookmarksContractModel {

    private BookMarksObject bookmarkObject;

    public BookmarkManager() {
        super(WeatherObj.class);
    }

    /**
     * method that take {@link BookMarksObject} retrieve it's weather information, update Updating state
     * @param bookMarksObject
     */
    public void updateBookmarkObjectData(BookMarksObject bookMarksObject) {
        this.bookmarkObject = bookMarksObject;
        bookMarksObject.setUpdating(true);
        bookMarksObject.setUpdateFailed(false);
        updateBookmarkObject(bookMarksObject);
        if (isNetworkConnected()) {
            getObject(String.format(Locale.getDefault(), "/weather?lat=%f&lon=%f", bookMarksObject.getLatitude(), bookMarksObject.getLongitude()), this);
        } else {
            onFail(new AppException(AppException.NETWORK_EXCEPTION));
        }
    }

    /**
     * method that add {@link BookMarksObject} to database and call {@link BookmarkManager#updateBookmarkObjectData(BookMarksObject)}
     * to retrieve weather info
     * @param bookMarksObject
     */
    public void addBookmarkObject(BookMarksObject bookMarksObject) {
        ContentValues contentValues = BookMarksUtils.getContentValueFromBookMark(bookMarksObject);
        Uri uri = WeatherApp.getInstance().getContentResolver().insert(BookMarksContentProvider.CONTENT_URI, contentValues);
        long id = Long.valueOf(uri.getLastPathSegment());
        bookMarksObject.setId(id);
        updateBookmarkObjectData(bookMarksObject);
    }

    /**
     * {@link BookMarksObject} update method
     * @param bookMarksObject
     */
    public void updateBookmarkObject(BookMarksObject bookMarksObject) {
        ContentValues contentValues = BookMarksUtils.getContentValueFromBookMark(bookMarksObject);
        WeatherApp.getInstance().getContentResolver().update(BookMarksContentProvider.CONTENT_URI
                , contentValues, BookMarksDataBaseHelper.BOOKMARK_ID + "=?"
                , new String[]{String.valueOf(bookMarksObject.getId())});
    }

    /**
     * delete a single bookmark
     * @param bookMarksObject
     */
    public void deleteBookmark(BookMarksObject bookMarksObject) {
        WeatherApp.getInstance().getContentResolver().delete(BookMarksContentProvider.CONTENT_URI
                , BookMarksDataBaseHelper.BOOKMARK_ID + "=?"
                , new String[]{String.valueOf(bookMarksObject.getId())});
    }

    /**
     * delete all bookmarks
     */
    public void deleteAllBookmarks() {
        WeatherApp.getInstance().getContentResolver().delete(BookMarksContentProvider.CONTENT_URI
                , null, null);
    }

    /**
     * when retrieving of weather info succeed add them to bookmark object and then update database
     * @param model
     */
    @Override
    public void onSuccess(WeatherObj model) {
        bookmarkObject.setWeatherObj(model);
        bookmarkObject.setUpdating(false);
        bookmarkObject.setUpdateFailed(false);
        updateBookmarkObject(bookmarkObject);
    }

    /**
     * on fail mark {@link BookMarksObject} updating state as fail so View would handle it
     * in our case it show retry button
     * @param ex
     */
    @Override
    public void onFail(AppException ex) {
        bookmarkObject.setUpdating(false);
        bookmarkObject.setUpdateFailed(true);
        updateBookmarkObject(bookmarkObject);
    }

    /**
     * check if database is empty
     * @return
     */
    public boolean isBookmarksNotEmpty() {
        Cursor cursor = WeatherApp.getInstance().getContentResolver().query(BookMarksContentProvider.CONTENT_URI, null, null, null, null);

        if (cursor != null) {

            boolean notEmpty = (cursor.getCount() > 0);
            cursor.close();
            return notEmpty;
        }

        return false;

    }

    /**
     * reset database state refer to {@link BookMarksUtils#resetBookmarksState()} for more info
     * @param contentValues
     */
    public void resetState(ContentValues contentValues) {
        WeatherApp.getInstance().getContentResolver().update(BookMarksContentProvider.CONTENT_URI
                , contentValues, null
                , null);
    }

    /**
     * is default bookmark(in our case the it's the user location) is available or not
     * @return
     */
    public boolean isDefaultAvailable() {
        Cursor cursor = WeatherApp.getInstance().getContentResolver().query(BookMarksContentProvider.CONTENT_URI, null, BookMarksDataBaseHelper.BOOKMARK_DEFAULT + "=?", new String[]{String.valueOf(1)}, null);

        if (cursor != null) {

            boolean notEmpty = (cursor.getCount() > 0);
            cursor.close();
            return notEmpty;
        }

        return false;
    }
}
