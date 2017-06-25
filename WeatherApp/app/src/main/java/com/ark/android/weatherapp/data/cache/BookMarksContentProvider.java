package com.ark.android.weatherapp.data.cache;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * BookmarkContentProvider class
 * I decided to go with contentprovider so if one day we decided to implement
 * update interval using the syncAdapter and AccountManager so this class mainly
 * for future releases
 * Created by ahmedb on 12/13/16.
 */

public class BookMarksContentProvider extends ContentProvider {

    //list of projections
    public static String[] AVAILABLE_COLUMNS = { BookMarksDataBaseHelper.BOOKMARK_ID,
            BookMarksDataBaseHelper.BOOKMARK_NAME
            ,BookMarksDataBaseHelper.BOOKMARK_GEO
            ,BookMarksDataBaseHelper.LONGITUDE
            ,BookMarksDataBaseHelper.LATITUDE
            ,BookMarksDataBaseHelper.DATE_INSERTED
            ,BookMarksDataBaseHelper.WEATHER_TITLE
            ,BookMarksDataBaseHelper.BOOKMARK_TEMP
            ,BookMarksDataBaseHelper.BOOKMARK_MAX_TEMP
            ,BookMarksDataBaseHelper.BOOKMARK_MIN_TEMP
            ,BookMarksDataBaseHelper.BOOKMARK_HUMIDITY
            ,BookMarksDataBaseHelper.BOOKMARK_WIND_SPEED
            ,BookMarksDataBaseHelper.BOOKMARK_RAIN_VOLUME
            ,BookMarksDataBaseHelper.BOOKMARK_DATE
            ,BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE
            ,BookMarksDataBaseHelper.BOOKMARK_FAILED
            ,BookMarksDataBaseHelper.BOOKMARK_DEFAULT};

    // used for the UriMacher
    private static final int BOOKMARKS = 10;
    private static final int BOOKMARK_ID = 20;

    public static final String AUTHORITY = "com.ark.android.weatherApp.contentprovider";

    private static final String BASE_PATH = "bookmarks";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, BOOKMARKS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", BOOKMARK_ID);
    }

    private BookMarksDataBaseHelper dataBaseHelper;


    @Override
    public boolean onCreate() {
        dataBaseHelper = new BookMarksDataBaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(BookMarksDataBaseHelper.BOOKMARKS_TABLE);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case BOOKMARKS:
                break;
            case BOOKMARK_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(BookMarksDataBaseHelper.BOOKMARK_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case BOOKMARKS:
                id = sqlDB.insert(BookMarksDataBaseHelper.BOOKMARKS_TABLE, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case BOOKMARKS:
                rowsDeleted = sqlDB.delete(BookMarksDataBaseHelper.BOOKMARKS_TABLE, selection,
                        selectionArgs);
                break;
            case BOOKMARK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            BookMarksDataBaseHelper.BOOKMARKS_TABLE,
                            BookMarksDataBaseHelper.BOOKMARK_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            BookMarksDataBaseHelper.BOOKMARKS_TABLE,
                            BookMarksDataBaseHelper.BOOKMARK_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case BOOKMARKS:
                rowsUpdated = sqlDB.update(BookMarksDataBaseHelper.BOOKMARKS_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case BOOKMARK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(BookMarksDataBaseHelper.BOOKMARKS_TABLE,
                            values,
                            BookMarksDataBaseHelper.BOOKMARK_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(BookMarksDataBaseHelper.BOOKMARKS_TABLE,
                            values,
                            BookMarksDataBaseHelper.BOOKMARK_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    /**
     * we use that method to be 100% sure that the caller of query method otherwise the app will crash
     * to notify caller that something is wrong with his projections
     * @param projection
     */
    private void checkColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(AVAILABLE_COLUMNS));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }

    }
}
