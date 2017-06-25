package com.ark.android.weatherapp.ui.presenter;

import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.manager.BookmarkManager;
import com.ark.android.weatherapp.mvpContract.AddBookmarkDialogContract;

/**
 * Presenter class for {@link com.ark.android.weatherapp.ui.fragment.AddBookmarkDialog}
 * Created by Ark on 6/25/2017.
 */

public class AddBookmarkDialogPresenter implements AddBookmarkDialogContract.IAddBookmarkPresenter {

    private final AddBookmarkDialogContract.IAddBookmarkView addBookmarkView;

    public AddBookmarkDialogPresenter(AddBookmarkDialogContract.IAddBookmarkView iAddBookmarkView) {
        this.addBookmarkView = iAddBookmarkView;
    }

    @Override
    public void addBookmark() {
        if (addBookmarkView.getBookmarkName().isEmpty())
            addBookmarkView.showEmptyBookmarkError();
        else {
            BookMarksObject bookMarksObject = new BookMarksObject();
            bookMarksObject.setTitle(addBookmarkView.getBookmarkName());
            bookMarksObject.setLongitude(addBookmarkView.getLongitude());
            bookMarksObject.setLatitude(addBookmarkView.getLatitude());
            bookMarksObject.setCreatedTime(System.currentTimeMillis());
            new BookmarkManager().addBookmarkObject(bookMarksObject);
            addBookmarkView.onBookmarkAdded();
        }
    }
}
