package com.ark.android.weatherapp.ui.presenter;

import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.WeatherApp;
import com.ark.android.weatherapp.data.cache.BookMarksUtils;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.manager.BookmarkManager;
import com.ark.android.weatherapp.mvpContract.BookmarksListContract;
import com.ark.android.weatherapp.service.GPSTracker;
import com.ark.android.weatherapp.ui.adapter.BookmarkAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A presenter class for {@link com.ark.android.weatherapp.ui.fragment.HomeFragment}
 * Created by Ark on 6/25/2017.
 */

public class HomePresenter implements BookmarksListContract.IBookmarksContractPresenter {

    private final BookmarksListContract.IBookmarksContractView bookmarkView;
    private final BookmarkManager bookmarkManager;
    private BookmarkAdapter bookmarkAdapter;
    private GPSTracker gpsTracker;

    /**
     * on initialization try to get the user location if not defined before
     * @param iBookmarksContractView
     */
    public HomePresenter(BookmarksListContract.IBookmarksContractView iBookmarksContractView) {
        this.bookmarkView = iBookmarksContractView;
        this.bookmarkManager = new BookmarkManager();
        getCurrentLocation();
    }

    @Override
    public void onLoaderFinish(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            List<BookMarksObject> bookmarks = getBookmarksFromCursor(cursor);
            if(bookmarkAdapter == null) {
                bookmarkAdapter = new BookmarkAdapter(bookmarks, bookmarkView.getContext());
                bookmarkView.getBookmarksList().setLayoutManager(new LinearLayoutManager(bookmarkView.getContext()));
                bookmarkView.getBookmarksList().setAdapter(bookmarkAdapter);
            }else{
                bookmarkAdapter.updateList(bookmarks);
                bookmarkAdapter.notifyDataSetChanged();
            }
            bookmarkView.getBookmarksList().setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    bookmarkAdapter.setScrollingDown(dy > 0);
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
    }

    /**
     * get {@link BookMarksObject} list from cursor
     * @param cursor
     * @return
     */
    private List<BookMarksObject> getBookmarksFromCursor(Cursor cursor) {
        List<BookMarksObject> bookmarks = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bookmarks.add(BookMarksUtils.getBookMarkFromCursor(cursor));
            cursor.moveToNext();
        }
        return bookmarks;
    }

    @Override
    public void deleteBookmark(BookMarksObject bookMarksObject) {

    }

    /**
     * first check {@link android.system.Os} and if it's Android M or above ask user for location permission
     * if not try to retrieve location
     */
    @Override
    public void getCurrentLocation() {
        if(bookmarkView.isLocationPermissionGranted())
            onPermissionGranted();
        else
            bookmarkView.askForPermission();
    }

    @Override
    public void onPermissionGranted() {
        if(gpsTracker == null)
            gpsTracker = new GPSTracker(bookmarkView.getContext());
        checkUserLocation(gpsTracker);
    }

    /**
     * check user location
     * @param gpsTracker
     */
    private void checkUserLocation(GPSTracker gpsTracker) {
        if(!bookmarkManager.isDefaultAvailable()) {
            double latitude = 0;
            double longitude = 0;
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

            } else {
                gpsTracker.showSettingsAlert();
            }
            if (latitude != 0 && longitude != 0) {
                BookMarksObject bookMarksObject = new BookMarksObject();
                bookMarksObject.setTitle(WeatherApp.getInstance().getString(R.string.userLocation));
                bookMarksObject.setLongitude(longitude);
                bookMarksObject.setLatitude(latitude);
                bookMarksObject.setCreatedTime(System.currentTimeMillis());
                bookMarksObject.setUpdating(true);
                bookMarksObject.setUpdateFailed(false);
                bookMarksObject.setDefault(true);
                bookmarkManager.addBookmarkObject(bookMarksObject);
            } else {
                bookmarkView.locationObtainFail();
            }

            gpsTracker.stopUsingGPS();
        }
    }

    @Override
    public void onFragmentPaused() {
        if(gpsTracker != null)
            gpsTracker.stopUsingGPS();
    }
}
