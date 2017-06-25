package com.ark.android.weatherapp.data.cache;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;

import com.ark.android.weatherapp.BuildConfig;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.manager.BookmarkManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 * Created by Ark on 6/12/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class ContentProviderTest {
    private ContentResolver contentResolver;
    private BookmarkManager bookmarkManager;

    @Before
    public void setup() {
        BookMarksContentProvider provider = new BookMarksContentProvider();
        provider.onCreate();
        bookmarkManager = new BookmarkManager();
        ProviderInfo info = new ProviderInfo();
        info.authority = BookMarksContentProvider.AUTHORITY;
        Robolectric.buildContentProvider(BookMarksContentProvider.class).create(info);
        contentResolver = RuntimeEnvironment.application.getContentResolver();
    }

    @Test
    public void testAddingAndDeleting() {
        bookmarkManager.deleteAllBookmarks();
        bookmarkManager.addBookmarkObject(BookMarksUtils.getDefaultBookmark());

        Cursor cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertEquals(cursorCheck.getString(cursorCheck.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_NAME)), "London");
        assertTrue(cursorCheck.getCount() > 0);

        assertTrue(bookmarkManager.isBookmarksNotEmpty());

        bookmarkManager.deleteAllBookmarks();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        assertTrue(cursorCheck.getCount() == 0);
    }

    @Test
    public void testDefaultBookmark(){
        bookmarkManager.deleteAllBookmarks();
        BookMarksObject bookMarksObject = BookMarksUtils.getDefaultBookmark();
        bookMarksObject.setDefault(true);
        bookmarkManager.addBookmarkObject(bookMarksObject);

        assertTrue(bookmarkManager.isDefaultAvailable());
    }

    @Test
    public void testUpdate(){
        bookmarkManager.deleteAllBookmarks();
        BookMarksObject bookMarksObject = BookMarksUtils.getDefaultBookmark();
        bookmarkManager.addBookmarkObject(bookMarksObject);

        Cursor cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertEquals(cursorCheck.getString(cursorCheck.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_NAME)), bookMarksObject.getTitle());

        BookMarksObject updatedObject = BookMarksUtils.getBookMarkFromCursor(cursorCheck);
        updatedObject.setTitle("Cairo");

        bookmarkManager.updateBookmarkObject(updatedObject);

        cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertEquals(cursorCheck.getString(cursorCheck.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_NAME)), updatedObject.getTitle());

    }

    @Test
    public void testDeleteRow(){
        bookmarkManager.deleteAllBookmarks();
        BookMarksObject bookMarksObject = BookMarksUtils.getDefaultBookmark();
        bookmarkManager.addBookmarkObject(bookMarksObject);
        assertTrue(bookmarkManager.isBookmarksNotEmpty());
        bookmarkManager.deleteBookmark(bookMarksObject);
        assertFalse(bookmarkManager.isBookmarksNotEmpty());
    }

    @Test
    public void testResetState(){
        bookmarkManager.deleteAllBookmarks();
        BookMarksObject bookMarksObject = BookMarksUtils.getDefaultBookmark();
        bookmarkManager.addBookmarkObject(bookMarksObject);

        Cursor cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertEquals(cursorCheck.getString(cursorCheck.getColumnIndex(BookMarksDataBaseHelper.BOOKMARK_NAME)), bookMarksObject.getTitle());

        BookMarksObject updatedObject = BookMarksUtils.getBookMarkFromCursor(cursorCheck);
        updatedObject.setUpdating(true);

        bookmarkManager.updateBookmarkObject(updatedObject);

        cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE + "=?", new String[]{String.valueOf(1)}, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertTrue(cursorCheck.getColumnCount() > 0);

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE, 0);
        contentValues.put(BookMarksDataBaseHelper.BOOKMARK_FAILED, 0);

        bookmarkManager.resetState(contentValues);

        cursorCheck = contentResolver.query(BookMarksContentProvider.CONTENT_URI,
                null, BookMarksDataBaseHelper.BOOKMARK_UPDATING_STATE + "=?", new String[]{String.valueOf(1)}, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertTrue(cursorCheck.getCount() == 0);

    }
}
