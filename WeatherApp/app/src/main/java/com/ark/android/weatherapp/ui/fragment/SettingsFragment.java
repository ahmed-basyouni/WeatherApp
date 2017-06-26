package com.ark.android.weatherapp.ui.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.cache.BookMarksUtils;
import com.ark.android.weatherapp.mvpContract.ActivityFragmentContract;

/**
 *
 * Created by Ark on 6/26/2017.
 */

public class SettingsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener
        , View.OnClickListener
        ,ActivityFragmentContract.FragmentToolBarSetupInterface{

    private RadioButton fahrenheit;
    private RadioButton celsius;
    private RadioButton ascending;
    private RadioButton descending;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        intUi(rootView);
        setupToolBar();
        setValues();
        return rootView;
    }

    private void setValues() {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(pre.getBoolean(BookMarksUtils.IS_Fahrenheit, false)){
            fahrenheit.setChecked(true);
        }else{
            celsius.setChecked(true);
        }

        if(pre.getBoolean(BookMarksUtils.IS_ASCENDING_ORDER, false)){
            ascending.setChecked(true);
        }else{
            descending.setChecked(true);
        }
    }

    @Override
    public void setupToolBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.settings);
            ((ActivityFragmentContract.FragmentInteractionListener)getActivity()).showBackBtn(true);
        }

        ((ActivityFragmentContract.FragmentInteractionListener)getActivity()).changeToolbarButtonsVisibility(false, false, false);
    }

    private void intUi(View rootView) {
        RadioGroup unitGroup = (RadioGroup) rootView.findViewById(R.id.tempGroup);
        fahrenheit = (RadioButton) rootView.findViewById(R.id.fahrenheit);
        celsius = (RadioButton) rootView.findViewById(R.id.celsius);
        ascending = (RadioButton) rootView.findViewById(R.id.ascending);
        descending = (RadioButton) rootView.findViewById(R.id.descending);
        unitGroup.setOnCheckedChangeListener(this);
        RadioGroup sortGroup = (RadioGroup) rootView.findViewById(R.id.sortGroup);
        sortGroup.setOnCheckedChangeListener(this);
        TextView about = (TextView) rootView.findViewById(R.id.aboutAppBtn);
        about.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()){
            case R.id.tempGroup:
                if(checkedId == R.id.celsius){
                    changePrefToFahrenheit(false);
                }else{
                    changePrefToFahrenheit(true);
                }
                break;

            case R.id.sortGroup:
                if(checkedId == R.id.ascending){
                    changeSortToAscending(true);
                }else{
                    changeSortToAscending(false);
                }
                break;
        }
    }

    private void changeSortToAscending(boolean ascending) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(BookMarksUtils.IS_ASCENDING_ORDER, ascending).apply();
    }

    private void changePrefToFahrenheit(boolean fahrenheit) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(BookMarksUtils.IS_Fahrenheit , fahrenheit).apply();
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction().add(R.id.fragmentContainer, new AboutFragment(), AboutFragment.class.getSimpleName())
                .addToBackStack(AboutFragment.class.getSimpleName()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupToolBar();
    }
}
