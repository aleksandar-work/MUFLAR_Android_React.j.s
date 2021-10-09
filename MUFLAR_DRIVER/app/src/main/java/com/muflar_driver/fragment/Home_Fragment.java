package com.muflar_driver.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.muflar_driver.R;
import com.muflar_driver.activity.MainActivity;


/**
 * Created by CISS31 on 4/4/2018.
 */

public class Home_Fragment extends Fragment implements OnMapReadyCallback{
    MapView mapFragment;
    String currancy,service,bank,add,state;

    private GoogleMap googleMap;


    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.home_layout,container,false);

        MainActivity.toolbar.setVisibility(View.GONE);

        mapFragment = (MapView)view. findViewById(R.id.map);


            mapFragment.onCreate(savedInstanceState);
            mapFragment.onResume();
            mapFragment.getMapAsync(this);

        LocationManager locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location; location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);





        return view;
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        MapStyleOptions styleOptions=MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.mapstyle);
        googleMap.setMapStyle(styleOptions);


        googleMap.setMyLocationEnabled(true);


    }






}
