package com.ark.android.weatherapp.mvpContract;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ark.android.weatherapp.data.model.BookMarksObject;

import java.util.List;

/**
 * MVP contract for Bookmark list
 * Created by Ark on 6/24/2017.
 */

public interface BookmarksListContract {

    interface IBookmarksContractView{
        void showErrorMsg(String msg);
        void hideErrorMsg();
        RecyclerView getBookmarksList();
        Activity getActivityContext();
        boolean isTabMode();
        boolean isLocationPermissionGranted();
        void askForPermission();
        void locationObtainFail();
        void showDeleteIcon();
        void showAddIcon();
        void closeApp();
        void restartLoader();
        void openDetailsForBookmark(Bundle bundle);
    }

    interface IBookmarksContractPresenter{
        void onLoaderFinish(Cursor cursor);
        void deleteBookmark();
        void getCurrentLocation();
        void onPermissionGranted();
        void onFragmentPaused();
        void onSaveInstance(Bundle outState);
    }

    interface IBookmarksContractModel{
        void updateBookmarkObjectData(BookMarksObject bookMarksObject);
        void addBookmarkObject(BookMarksObject bookMarksObject);
        void updateBookmarkObject(BookMarksObject bookMarksObject);
        void deleteBookmark(BookMarksObject bookMarksObject);
        void deleteAllBookmarks();
    }

    interface IBookmarkAdapterView{
        List<BookMarksObject> getBookmarks();
        void checkFahrenhiet();
    }

    interface IBookmarkAdapterPresenter{
        Activity getActivityContext();
        void selectBookmarkAtPosition(int position, boolean checked);
        boolean isItemAtPositionSelected(int position);
    }
}
