package com.ark.android.weatherapp.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.WeatherApp;
import com.ark.android.weatherapp.data.cache.BookMarksUtils;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.manager.BookmarkManager;
import com.ark.android.weatherapp.mvpContract.BookmarksListContract;
import com.ark.android.weatherapp.service.GPSTracker;
import com.ark.android.weatherapp.ui.adapter.BookmarkAdapter;
import com.ark.android.weatherapp.ui.fragment.DetailsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A presenter class for {@link com.ark.android.weatherapp.ui.fragment.HomeFragment}
 * Created by Ark on 6/25/2017.
 */

public class HomePresenter implements BookmarksListContract.IBookmarksContractPresenter, BookmarksListContract.IBookmarkAdapterPresenter, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LIST_KEY = "bookmarkList";
    private static final String SCROLL_OFFSET = "scrollOffset";
    private final BookmarksListContract.IBookmarksContractView bookmarkView;
    private final BookmarkManager bookmarkManager;
    private BookmarkAdapter bookmarkAdapter;
    private GPSTracker gpsTracker;
    private Map<Integer, BookMarksObject> bookmarksToRemove = new HashMap<>();

    /**
     * on initialization try to get the user location if not defined before
     *
     * @param iBookmarksContractView
     */
    public HomePresenter(BookmarksListContract.IBookmarksContractView iBookmarksContractView, Bundle bundle) {
        this.bookmarkView = iBookmarksContractView;
        this.bookmarkManager = new BookmarkManager();
        PreferenceManager.getDefaultSharedPreferences(bookmarkView.getActivityContext()).registerOnSharedPreferenceChangeListener(this);
        if (bundle == null)
            getCurrentLocation();
        else
            onRestoreState(bundle);
    }

    private void onRestoreState(Bundle bundle) {
        ArrayList<BookMarksObject> bookmarks = bundle.getParcelableArrayList(LIST_KEY);
        int scrollOffset = bundle.getInt(SCROLL_OFFSET);
        markListAsForceUpdae(bookmarks);
        bookmarkAdapter = new BookmarkAdapter(bookmarks, this);
        bookmarkView.getBookmarksList().setLayoutManager(new LinearLayoutManager(bookmarkView.getActivityContext()));
        bookmarkView.getBookmarksList().setAdapter(bookmarkAdapter);
        bookmarkView.getBookmarksList().scrollTo(0, scrollOffset);
    }

    @Override
    public void onLoaderFinish(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<BookMarksObject> bookmarks = getBookmarksFromCursor(cursor);
            if (bookmarkAdapter == null) {
                markListAsForceUpdae(bookmarks);
                bookmarkAdapter = new BookmarkAdapter(bookmarks, this);
                bookmarkView.getBookmarksList().setLayoutManager(new LinearLayoutManager(bookmarkView.getActivityContext()));
                bookmarkView.getBookmarksList().setAdapter(bookmarkAdapter);
                handler.sendEmptyMessage(2);
            } else {
                bookmarkAdapter.updateList(bookmarks);
                bookmarkAdapter.notifyDataSetChanged();
            }
        }
    }

    private void markListAsForceUpdae(List<BookMarksObject> bookmarks) {
        for (BookMarksObject bookMarksObject : bookmarks) {
            bookMarksObject.setForceUpdate(true);
        }
    }

    /**
     * get {@link BookMarksObject} list from cursor
     *
     * @param cursor
     * @return
     */
    private ArrayList<BookMarksObject> getBookmarksFromCursor(Cursor cursor) {
        ArrayList<BookMarksObject> bookmarks = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bookmarks.add(BookMarksUtils.getBookMarkFromCursor(cursor));
            cursor.moveToNext();
        }
        return bookmarks;
    }

    @Override
    public void deleteBookmark() {
        if (!bookmarksToRemove.isEmpty()) {
            Iterator<Map.Entry<Integer, BookMarksObject>> iter = bookmarksToRemove.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, BookMarksObject> entry = iter.next();
                bookmarkManager.deleteBookmark(entry.getValue());
                bookmarkAdapter.getBookmarks().remove(entry.getKey().intValue());
                bookmarkAdapter.notifyItemRemoved(entry.getKey());
                iter.remove();
            }

        }
        bookmarkView.showAddIcon();

        if (bookmarkView.getActivityContext().getResources().getBoolean(R.bool.isTab) && !bookmarkAdapter.getBookmarks().isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailsFragment.BOOKMARK_OBJ, bookmarkAdapter.getBookmarks().get(0));
            bookmarkView.openDetailsForBookmark(bundle);
            bookmarkView.getBookmarksList().smoothScrollToPosition(0);
        }

        if (bookmarkAdapter.getBookmarks().isEmpty())
            bookmarkView.closeApp();
    }

    private Handler handler = new Handler() { // handler for commiting fragment after data is loaded
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                if (bookmarkView.getActivityContext().getResources().getBoolean(R.bool.isTab) && !bookmarkAdapter.getBookmarks().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DetailsFragment.BOOKMARK_OBJ, bookmarkAdapter.getBookmarks().get(0));
                    bookmarkView.openDetailsForBookmark(bundle);
                }
            }
        }
    };

    /**
     * first check {@link android.system.Os} and if it's Android M or above ask user for location permission
     * if not try to retrieve location
     */
    @Override
    public void getCurrentLocation() {
        if (bookmarkView.isLocationPermissionGranted())
            onPermissionGranted();
        else
            bookmarkView.askForPermission();
    }

    @Override
    public void onPermissionGranted() {
        if (gpsTracker == null)
            gpsTracker = new GPSTracker(bookmarkView.getActivityContext());
        checkUserLocation(gpsTracker);
    }

    /**
     * check user location
     *
     * @param gpsTracker
     */
    private void checkUserLocation(GPSTracker gpsTracker) {
        if (!bookmarkManager.isDefaultAvailable()) {
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
        if (gpsTracker != null)
            gpsTracker.stopUsingGPS();
    }

    @Override
    public void onSaveInstance(Bundle outState) {
        outState.putParcelableArrayList(LIST_KEY, bookmarkAdapter.getBookmarks());
        outState.putInt(SCROLL_OFFSET, bookmarkView.getBookmarksList().computeVerticalScrollOffset());
    }

    @Override
    public Activity getActivityContext() {
        return bookmarkView.getActivityContext();
    }

    @Override
    public void selectBookmarkAtPosition(int position, boolean checked) {
        if (checked)
            bookmarksToRemove.put(position, bookmarkAdapter.getBookmarks().get(position));
        else
            bookmarksToRemove.remove(position);

        if (!bookmarksToRemove.isEmpty())
            bookmarkView.showDeleteIcon();
        else
            bookmarkView.showAddIcon();
    }

    @Override
    public boolean isItemAtPositionSelected(int position) {
        return bookmarksToRemove.get(position) != null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(BookMarksUtils.IS_Fahrenheit)) {
            if (bookmarkAdapter != null) {
                bookmarkAdapter.checkFahrenhiet();
                bookmarkAdapter.notifyDataSetChanged();
            }
        } else if (key.equals(BookMarksUtils.IS_ASCENDING_ORDER)) {
            bookmarkView.restartLoader();
        }
    }
}
