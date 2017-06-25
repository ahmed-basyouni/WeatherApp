package com.ark.android.weatherapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.cache.BookMarksContentProvider;
import com.ark.android.weatherapp.data.cache.BookMarksDataBaseHelper;
import com.ark.android.weatherapp.mvpContract.BookmarksListContract;
import com.ark.android.weatherapp.ui.activity.MainActivity;
import com.ark.android.weatherapp.ui.presenter.HomePresenter;

/**
 * Fragment That display bookmarks list which app obtained from content provider
 * Created by Ark on 6/24/2017.
 */

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, BookmarksListContract.IBookmarksContractView, View.OnClickListener {

    private final int LOADER_ID = 231;
    private final int PERMISSION_REQUEST = 234;
    private Toolbar toolbar;
    private RecyclerView bookmarksList;
    private HomePresenter homePresenter;
    private ImageButton addBtn;
    private boolean isAddShown = true;

    //init loader manager and presenter
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, this);
        homePresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bookmarks_list_fragment, container, false);
        initUI(rootView);
        initToolBar();
        return rootView;
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    private void initToolBar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle(R.string.app_name);
    }

    private void initUI(View rootView) {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        addBtn = (ImageButton) rootView.findViewById(R.id.openMap);
        addBtn.setOnClickListener(this);
        bookmarksList = (RecyclerView) rootView.findViewById(R.id.bookmarkList);
    }

    @Override
    public void onPause() {
        super.onPause();
        homePresenter.onFragmentPaused();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BookMarksContentProvider.CONTENT_URI,
                null, null, null,  BookMarksDataBaseHelper.BOOKMARK_DEFAULT + " DESC," + BookMarksDataBaseHelper.DATE_INSERTED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID)
             homePresenter.onLoaderFinish(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void hideErrorMsg() {

    }

    @Override
    public RecyclerView getBookmarksList() {
        return bookmarksList;
    }

    @Override
    public boolean isTabMode() {
        return false;
    }

    @Override
    public boolean isLocationPermissionGranted() {
        return !(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void askForPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
    }

    @Override
    public void locationObtainFail() {
        Toast.makeText(getActivity(), getString(R.string.locationFail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteIcon() {
        if(isAddShown)
            changeIconAnimation();
    }

    @Override
    public void showAddIcon() {
        if(!isAddShown)
            changeIconAnimation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                homePresenter.onPermissionGranted();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Not all/any permission granted
                ((MainActivity)getActivity()).showMessageOK(getString(R.string.locationDenied), null);
            } else {
                ((MainActivity)getActivity()).showMessageOKCancel(getString(R.string.allowLocation),
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST);
                            }
                        });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openMap:
                if(isAddShown)
                    openPlacePickerFragment();
                else
                    homePresenter.deleteBookmark();
                break;
        }
    }

    private void openPlacePickerFragment() {
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new PlacePickerFragment())
                .addToBackStack(null).commit();
    }

    public void changeIconAnimation(){
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_animatin);
        addBtn.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_anim);
                if(isAddShown)
                    addBtn.setImageResource(R.drawable.ic_delete_white_24dp);
                else
                    addBtn.setImageResource(R.drawable.ic_add_white_24dp);
                addBtn.startAnimation(fadeOut);
                isAddShown = !isAddShown;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
