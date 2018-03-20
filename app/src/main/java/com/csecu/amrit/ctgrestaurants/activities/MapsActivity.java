package com.csecu.amrit.ctgrestaurants.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ResAndOwner resAndOwner;
    Boolean mLocationPermissionGranted = false;
    LatLng destination = null;
    int DEFAULT_ZOOM = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("resAndOwner");
        }

        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Double latitude = Double.parseDouble(resAndOwner.getLatitude());
        Double longitude = Double.parseDouble(resAndOwner.getLongitude());

        String name = decodeString(resAndOwner.getName());
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ToastController toastController = new ToastController(this);
            toastController.errorToast("Set the permission first");
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            destination = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(destination).title("" + name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, DEFAULT_ZOOM));
        }
    }

    private String decodeString(String s) {
        ToastController toastController = new ToastController(this);
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            toastController.errorToast(e.toString());
        }
        return s;
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
}
