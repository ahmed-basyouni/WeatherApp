package com.ark.android.weatherapp.ui.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.mvpContract.ActivityFragmentContract;
import com.ark.android.weatherapp.ui.fragment.HomeFragment;
import com.ark.android.weatherapp.utils.FragmentUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Main App activity which add the {@link HomeFragment} and contain two helper methods for Permission Request
 */
public class MainActivity extends AppCompatActivity implements ActivityFragmentContract.FragmentInteractionListener{

    private Toolbar toolbar;
    private final FragmentUtils fragmentUtils = new FragmentUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        initToolBar();
        boolean isTab = getResources().getBoolean(R.bool.isTab);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(isTab ? R.id.masterViewMain : R.id.fragmentContainer, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
        }

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment topFrag = fragmentUtils.getCurrentTopFragment(getFragmentManager());
                if(topFrag != null)
                    ((ActivityFragmentContract.FragmentToolBarSetupInterface)topFrag).setupToolBar();
            }
        });
    }

    @Override
    public void onAttachFragment (Fragment fragment) {
        fragmentUtils.onAttachFragment(fragment);
    }

    private void initUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
    private void initToolBar() {
        setSupportActionBar(toolbar);
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(android.R.string.ok), okListener)
                .setNegativeButton(getString(android.R.string.cancel), null)
                .create()
                .show();
    }

    public void showMessageOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(android.R.string.ok), okListener)
                .create()
                .show();
    }

    @Override
    public void changeToolbarButtonsVisibility(boolean settingVisible, boolean addVisible, boolean donVisible) {
        findViewById(R.id.settingBtn).setVisibility(settingVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.openMap).setVisibility(addVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.doneButton).setVisibility(donVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showBackBtn(boolean show){

        ActionBar actionBar = getSupportActionBar();
        if(show){
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }else{
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
            }
            toolbar.setNavigationOnClickListener(null);
        }
    }

    @Override
    public void resetToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }
}