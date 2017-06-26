package com.ark.android.weatherapp.mvpContract;

import android.app.Fragment;

/**
 *
 * Created by Ark on 6/26/2017.
 */

public interface ActivityFragmentContract {

    interface FragmentInteractionListener {
        void changeToolbarButtonsVisibility(boolean settingVisible, boolean addVisible, boolean donVisible);
        void showBackBtn(boolean show);
        void resetToolBar();
    }

    interface FragmentToolBarSetupInterface{
        void setupToolBar();
    }
}
