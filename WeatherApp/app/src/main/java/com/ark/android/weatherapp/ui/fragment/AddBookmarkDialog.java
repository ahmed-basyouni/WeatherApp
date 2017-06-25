package com.ark.android.weatherapp.ui.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.mvpContract.AddBookmarkDialogContract;
import com.ark.android.weatherapp.ui.presenter.AddBookmarkDialogPresenter;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public class AddBookmarkDialog extends DialogFragment implements AddBookmarkDialogContract.IAddBookmarkView, View.OnClickListener {

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    private EditText bookmarkNameField;
    private TextInputLayout bookmarkNameInput;
    private AddBookmarkDialogPresenter dialogPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogPresenter = new AddBookmarkDialogPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_bookmark_dialog, container, false);

        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        bookmarkNameField = (EditText) rootView.findViewById(R.id.bookmarkNameField);
        bookmarkNameInput = (TextInputLayout) rootView.findViewById(R.id.bookMarkNameInputLayout);
        Button okButton = (Button) rootView.findViewById(R.id.addBookmark);
        okButton.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getDialog().getWindow() != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(getDialog().getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(lp);
        }
    }

    @Override
    public String getBookmarkName() {
        return bookmarkNameField.getText().toString();
    }

    @Override
    public void showEmptyBookmarkError() {
        bookmarkNameInput.setError(getString(R.string.no_name_provided));
    }

    @Override
    public void onBookmarkAdded() {
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        getDialog().dismiss();
    }

    @Override
    public double getLongitude() {
        return getArguments().getDouble(LONGITUDE);
    }

    @Override
    public double getLatitude() {
        return getArguments().getDouble(LATITUDE);
    }

    @Override
    public void onClick(View v) {
        dialogPresenter.addBookmark();
    }
}
