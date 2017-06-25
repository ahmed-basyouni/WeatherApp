package com.ark.android.weatherapp.data.cache;

import android.content.ContentValues;
import android.database.Cursor;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.WeatherApp;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.data.model.RainObject;
import com.ark.android.weatherapp.data.model.Weather;
import com.ark.android.weatherapp.data.model.WeatherInfoObject;
import com.ark.android.weatherapp.data.model.WeatherObj;
import com.ark.android.weatherapp.data.model.Wind;
import com.ark.android.weatherapp.manager.BookmarkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class contain most of the methods we gonna need to interact between our data layer
 * (i.e models) and database layer
 * Created by Ark on 6/24/2017.
 */

public class BookMarksUtils {

    /**
     * pass a {@link Cursor} and get {@link BookMarksObject}
     * as we gonna use that method when retrieving objects from database
     * @param cursor
     * @return
     */
    public static BookMarksObject getBookMarkFromCursor(Cursor cursor) {
        BookMarksObject bookMarksObject = new BookMarksObject();
        WeatherObj weatherObj = new WeatherObj();
        bookMarksObject.setId(cursor.getInt(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_ID)));
        bookMarksObject.setTitle(cursor.getString(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_NAME)));
        bookMarksObject.setGeoAddress(cursor.getString(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_GEO)));
        bookMarksObject.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(BookMarksDataBaseHelper.LATITUDE))));
        bookMarksObject.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(BookMarksDataBaseHelper.LONGITUDE))));
        List<Weather> weathers = new ArrayList<>();
        weathers.add(new Weather());
        weatherObj.setWeather(weathers);
        weatherObj.getWeather().get(0).setWeatherTitle(cursor.getString(cursor.getColumnIndex(BookMarksDataBaseHelper.WEATHER_TITLE)));
        weatherObj.getWeather().get(0).setIcon(cursor.getString(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_WEATHER_ICON)));
        weatherObj.setWeatherInfoObject(new WeatherInfoObject());
        weatherObj.getWeatherInfoObject().setTemp(cursor.getDouble(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_TEMP)));
        weatherObj.getWeatherInfoObject().setTempMax(cursor.getDouble(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_MAX_TEMP)));
        weatherObj.getWeatherInfoObject().setTempMin(cursor.getDouble(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_MIN_TEMP)));
        weatherObj.getWeatherInfoObject().setHumidity(cursor.getInt(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_HUMIDITY)));
        weatherObj.setWind(new Wind());
        weatherObj.getWind().setSpeed(cursor.getDouble(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_WIND_SPEED)));
        weatherObj.setRain(new RainObject());
        weatherObj.getRain().setRainVolume(cursor.getDouble(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_RAIN_VOLUME)));
        weatherObj.setWeatherDate(cursor.getLong(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_DATE)));
        bookMarksObject.setUpdating(cursor.getInt(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE)) == 1);
        bookMarksObject.setUpdateFailed(cursor.getInt(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_FAILED)) == 1);
        bookMarksObject.setWeatherObj(weatherObj);
        bookMarksObject.setCreatedTime(cursor.getInt(cursor.getColumnIndex(BookMarksDataBaseHelper.DATE_INSERTED)));
        bookMarksObject.setDefault(cursor.getInt(cursor.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_DEFAULT)) == 1);
        return bookMarksObject;
    }

    /**
     * pass {@link BookMarksObject} and get {@link ContentValues}
     * As we gonna use that method in update and delete from database
     * @param bookMarksObject
     * @return
     */
    public static ContentValues getContentValueFromBookMark(BookMarksObject bookMarksObject) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_NAME, bookMarksObject.getTitle());
        contentValues.put(BookMarksDataBaseHelper.LONGITUDE, String.valueOf(bookMarksObject.getLongitude()));
        contentValues.put(BookMarksDataBaseHelper.LATITUDE, String.valueOf(bookMarksObject.getLatitude()));
        contentValues.put(BookMarksDataBaseHelper.DATE_INSERTED, bookMarksObject.getCreatedTime());
        if (bookMarksObject.getWeatherObj() != null) {
            contentValues.put(BookMarksDataBaseHelper.BOOKMARK_GEO, bookMarksObject.getWeatherObj().getName());
            if (bookMarksObject.getWeatherObj().getWeather().size() > 0) {
                contentValues.put(BookMarksDataBaseHelper.WEATHER_TITLE, bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle());
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_WEATHER_ICON, bookMarksObject.getWeatherObj().getWeather().get(0).getIcon());
            }
            if (bookMarksObject.getWeatherObj().getWeatherInfoObject() != null) {
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_TEMP, bookMarksObject.getWeatherObj().getWeatherInfoObject().getTemp());
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_MAX_TEMP, bookMarksObject.getWeatherObj().getWeatherInfoObject().getTempMax());
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_MIN_TEMP, bookMarksObject.getWeatherObj().getWeatherInfoObject().getTempMin());
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_HUMIDITY, bookMarksObject.getWeatherObj().getWeatherInfoObject().getHumidity());
            }
            if (bookMarksObject.getWeatherObj().getWind() != null)
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_WIND_SPEED, bookMarksObject.getWeatherObj().getWind().getSpeed());
            if (bookMarksObject.getWeatherObj().getRain() != null)
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_RAIN_VOLUME, bookMarksObject.getWeatherObj().getRain().getRainVolume());
            if (bookMarksObject.getWeatherObj().getWeatherDate() != 0)
                contentValues.put(BookMarksDataBaseHelper.BOOKMARK_DATE, bookMarksObject.getWeatherObj().getWeatherDate());
        }
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE, bookMarksObject.isUpdating() ? 1 : 0);
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_FAILED, bookMarksObject.isUpdateFailed() ? 1 : 0);
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_DEFAULT, bookMarksObject.isDefault() ? 1 : 0);
        return contentValues;
    }

    /**
     * Method that return default {@link BookMarksObject} in case user denied the location permission
     * or app failed to retrieve the user location so that our app home screen is never empty
     * @return
     */
    static BookMarksObject getDefaultBookmark() {
        BookMarksObject bookMarksObject = new BookMarksObject();
        bookMarksObject.setTitle(WeatherApp.getInstance().getString(R.string.defaultCity));
        bookMarksObject.setLongitude(-0.13);
        bookMarksObject.setLatitude(51.51);
        bookMarksObject.setCreatedTime(System.currentTimeMillis());
        bookMarksObject.setUpdating(true);
        bookMarksObject.setUpdateFailed(false);
        return bookMarksObject;
    }

    /**
     * we call that method in {@link WeatherApp#onCreate()} so that we make sure that default object
     * is present in database (i.e. our database is not empty)
     * then we make sure that all updating state inside our database is false
     * for more info refer to {@link BookMarksUtils#resetBookmarksState()}
     */
    public static void init() {
        checkDefaultBookmark();
        resetBookmarksState();
    }

    /**
     * If database is empty add default object
     */
    private static void checkDefaultBookmark() {
        BookmarkManager bookmarkManager = new BookmarkManager();
        boolean isBookmarkNotEmpty = bookmarkManager.isBookmarksNotEmpty();
        if(!isBookmarkNotEmpty){
            bookmarkManager.addBookmarkObject(BookMarksUtils.getDefaultBookmark());
        }
    }

    /**
     * this method is a error recovery
     * if for example app was closed before bookmark information was retrieved successfully(i.e. weatherInfo)
     * then we we open app again we will mark that object as in transit state meaning that we have an object
     * with location and bookmark name but without weather information, so when app identify the object as in transit
     * state it will start the downloading process again
     */
    private static void resetBookmarksState(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE, 0);
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_FAILED, 0);
        new BookmarkManager().resetState(contentValues);
    }
}
