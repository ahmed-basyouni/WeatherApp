package com.ark.android.weatherapp.mvpContract;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import com.ark.android.weatherapp.data.model.BookMarksObject;

/**
 * MVP contract for Bookmark list
 * Created by Ark on 6/24/2017.
 */

public interface BookmarksListContract {

    interface IBookmarksContractView{
        void showErrorMsg(String msg);
        void hideErrorMsg();
        RecyclerView getBookmarksList();
        Context getContext();
        boolean isTabMode();
        boolean isLocationPermissionGranted();
        void askForPermission();
        void locationObtainFail();
    }

    interface IBookmarksContractPresenter{
        void onLoaderFinish(Cursor cursor);
        void deleteBookmark(BookMarksObject bookMarksObject);
        void getCurrentLocation();
        void onPermissionGranted();
        void onFragmentPaused();
    }

    interface IBookmarksContractModel{
        void updateBookmarkObjectData(BookMarksObject bookMarksObject);
        void addBookmarkObject(BookMarksObject bookMarksObject);
        void updateBookmarkObject(BookMarksObject bookMarksObject);
        void deleteBookmark(BookMarksObject bookMarksObject);
        void deleteAllBookmarks();
    }
}
