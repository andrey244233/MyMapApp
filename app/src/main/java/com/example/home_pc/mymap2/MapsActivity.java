package com.example.home_pc.mymap2;

import android.content.Context;
import android.hardware.Camera;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {


    @BindView(R.id.btnNormal)
    Button btnNormal;
    @BindView(R.id.btnHibryd)
    Button btnHibryd;
    @BindView(R.id.btnSatelite)
    Button btnSatelite;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;

    public static final int UKRAINE_POSITION = 0;
    public static final int MADAGASKAR_POSITION = 1;
    public static final int CANADA_POSITION = 2;
    public static final int JAPAN_POSITION = 3;
    private static final LatLng UKRAINE_COORDINATES = new LatLng(49, 35);
    private static final LatLng MADAGASKAR_COORDINATES = new LatLng(-18, 46);
    private static final LatLng INDIA_COORDINATES = new LatLng(20, 77);
    private static final LatLng JAPAN_COORDINATES = new LatLng(36, 138);
    private GoogleMap mMap;
    float zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        btnNormal.setOnClickListener(this);
        btnHibryd.setOnClickListener(this);
        btnSatelite.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(0.0f);
        mMap.setMaxZoomPreference(15.0f);
        mMap.setOnCameraChangeListener(onCameraChangeListener);
        mMap.addMarker(new MarkerOptions().position(UKRAINE_COORDINATES).title("You are in Ukraine"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UKRAINE_COORDINATES));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.btnHibryd:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.btnSatelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case UKRAINE_POSITION:
                    moveToOtherPlace(UKRAINE_COORDINATES, "You are in Ukraine");
                    break;
                case MADAGASKAR_POSITION:
                    moveToOtherPlace(MADAGASKAR_COORDINATES, "You are in Madagaskar");
                    break;
                case CANADA_POSITION:
                    moveToOtherPlace(INDIA_COORDINATES, "You are in India");
                    break;
                case JAPAN_POSITION:
                    moveToOtherPlace(JAPAN_COORDINATES, "You are in Japan");
                    break;
            }
        }

        private void moveToOtherPlace(LatLng coordinates, String placeMarker) {
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinates), 3000, null);
            mMap.addMarker(new MarkerOptions().position(coordinates).title(placeMarker));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(progress), 500, null);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    GoogleMap.OnCameraChangeListener onCameraChangeListener = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
           float zoom = mMap.getCameraPosition().zoom;
            Log.v("tag", "Zoom =" + zoom);
            seekBar.setProgress((int) zoom);
        }
    };
}
