package com.ark.android.weatherapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.mvpContract.ActivityFragmentContract;
import com.ark.android.weatherapp.ui.activity.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class PlacePickerFragment extends Fragment implements OnMapReadyCallback
        , View.OnClickListener
        , ActivityFragmentContract.FragmentToolBarSetupInterface{

    private static final int PERMISSION_REQUEST = 234;
    private static final int ADD_BOOKMARK_DIALOG_REQUEST = 43;
    GoogleMap mMap;
    private MapView mapView;
    private LatLng currentLatLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_picker_fragment, container, false);

        initUI(rootView);
        setUpMap(savedInstanceState);

        return rootView;
    }

    private void setUpMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void initUI(View rootView) {
        Button doneButton = (Button) getActivity().findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
        mapView = (MapView) rootView.findViewById(R.id.mapV);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        } else {
            permissionGranted();
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .title(getString(R.string.choosedLocation)));
                currentLatLng = latLng;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView != null)
        mapView.onSaveInstanceState(outState);
    }

    private void permissionGranted() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted();
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
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        if (mapView != null)
            mapView.onResume();
        super.onResume();
        setupToolBar();
    }

    public void showAddBookmarkDialog() {
        AddBookmarkDialog addBookmarkDialog = new AddBookmarkDialog();
        Bundle bundle = new Bundle();
        bundle.putDouble(AddBookmarkDialog.LONGITUDE , currentLatLng.longitude);
        bundle.putDouble(AddBookmarkDialog.LATITUDE, currentLatLng.latitude);
        addBookmarkDialog.setArguments(bundle);
        addBookmarkDialog.setTargetFragment(this, ADD_BOOKMARK_DIALOG_REQUEST);
        addBookmarkDialog.show(getFragmentManager(),null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_BOOKMARK_DIALOG_REQUEST && resultCode == Activity.RESULT_OK){
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.doneButton){
            if(currentLatLng != null){
                showAddBookmarkDialog();
            }else{
                Toast.makeText(getActivity(), getString(R.string.no_location_provide), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setupToolBar() {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle(getString(R.string.pick_location));
            ((ActivityFragmentContract.FragmentInteractionListener)getActivity()).showBackBtn(true);
        }

        ((ActivityFragmentContract.FragmentInteractionListener)getActivity()).changeToolbarButtonsVisibility(false, false, true);
    }
}
