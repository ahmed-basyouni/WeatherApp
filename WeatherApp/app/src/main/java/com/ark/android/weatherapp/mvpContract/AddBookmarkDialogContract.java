package com.ark.android.weatherapp.mvpContract;

/**
 * MVP contract for AddBookmarkDialog
 * Created by Ark on 6/25/2017.
 */

public interface AddBookmarkDialogContract {

    interface IAddBookmarkView{
        String getBookmarkName();
        void showEmptyBookmarkError();
        void onBookmarkAdded();
        double getLongitude();
        double getLatitude();
    }

    interface IAddBookmarkPresenter{
        void addBookmark();
    }
}
