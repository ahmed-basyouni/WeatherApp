package com.ark.android.weatherapp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.ui.fragment.HomeFragment;

/**
 * Main App activity which add the {@link HomeFragment} and contain two helper methods for Permission Request
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
        }
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

}