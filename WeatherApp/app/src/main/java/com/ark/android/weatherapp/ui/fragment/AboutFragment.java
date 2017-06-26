package com.ark.android.weatherapp.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.mvpContract.ActivityFragmentContract;
import com.ark.android.weatherapp.ui.custom.NestedWebView;

/**
 *
 * Created by Ark on 6/26/2017.
 */

public class AboutFragment extends Fragment implements ActivityFragmentContract.FragmentToolBarSetupInterface{

    private NestedWebView aboutWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);

        initUi(rootView);
        loadAboutPage();

        return rootView;
    }

    private void loadAboutPage() {
        aboutWebView.loadUrl("file:///android_asset/about.html");
    }

    private void initUi(View rootView) {
        aboutWebView = (NestedWebView) rootView.findViewById(R.id.aboutWebView);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupToolBar();
    }

    @Override
    public void setupToolBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.aboutApp);
            ((ActivityFragmentContract.FragmentInteractionListener)getActivity()).showBackBtn(true);
        }

        ((ActivityFragmentContract.FragmentInteractionListener)getActivity()).changeToolbarButtonsVisibility(false, false, false);
    }
}
