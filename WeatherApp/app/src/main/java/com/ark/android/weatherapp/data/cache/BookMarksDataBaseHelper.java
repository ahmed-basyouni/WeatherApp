package com.ark.android.weatherapp.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class
 * Created by ahmedb on 11/4/16.
 */

public class BookMarksDataBaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Weather.db";
    private static final int DATABASE_VERSION = 1;
    static final String BOOKMARKS_TABLE = "BookmarksTable";

    public static final String BOOKMARK_ID = "_id";
    public static final String BOOKMARK_NAME = "bookmarkName";
    static final String BOOKMARK_GEO = "bookMarkGeo";
    static final String LONGITUDE = "longitude";
    static final String LATITUDE = "latitude";
    public static final String DATE_INSERTED = "date";
    static final String WEATHER_TITLE = "weatherTitle";
    static final String BOOKMARK_TEMP = "bookmark_temp";
    static final String BOOKMARK_MAX_TEMP = "bookmark_maxTemp";
    static final String BOOKMARK_MIN_TEMP = "bookmark_minTemp";
    static final String BOOKMARK_HUMIDITY = "bookmark_humidity";
    static final String BOOKMARK_WIND_SPEED = "bookmark_wind_speed";
    static final String BOOKMARK_RAIN_VOLUME = "bookmark_rain_volume";
    static final String BOOKMARK_UPDATING_STATE = "bookmark_update_state";
    static final String BOOKMARK_FAILED = "bookmark_failed";
    static final String BOOKMARK_DATE = "bookmarkDate";
    public static final String BOOKMARK_DEFAULT = "bookmarkDefault";
    static final String BOOKMARK_WEATHER_ICON = "weatherIcon";

    private static final String DATABASE_CREATE = "create table "
            + BOOKMARKS_TABLE + "( " + BOOKMARK_ID
            + " integer primary key , " + BOOKMARK_NAME
            + " text not null, " + BOOKMARK_GEO + " text, "
            + LONGITUDE + " text not null, "
            + LATITUDE + " text not null, "
            + WEATHER_TITLE + " text , "
            + BOOKMARK_WEATHER_ICON + " text , "
            + BOOKMARK_TEMP + " integer, "
            + BOOKMARK_MAX_TEMP + " integer, "
            + BOOKMARK_MIN_TEMP + " integer, "
            + BOOKMARK_HUMIDITY + " integer, "
            + BOOKMARK_WIND_SPEED + " integer, "
            + BOOKMARK_RAIN_VOLUME + " integer, "
            + BOOKMARK_UPDATING_STATE + " integer, "
            + BOOKMARK_FAILED + " integer, "
            + BOOKMARK_DATE + " integer, "
            + BOOKMARK_DEFAULT + " integer, "
            + DATE_INSERTED + " integer "
            + ");";

    public BookMarksDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BOOKMARKS_TABLE);
        onCreate(sqLiteDatabase);
    }
}
