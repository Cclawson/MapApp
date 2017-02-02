package com.example.mapapp;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Console;
import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap m_Map;
    private MapControl m_mapControl;// = new MapControl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        m_mapControl = new MapControl(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    public void OnMapButtonClick(View view)
    {
        m_mapControl.CreateRoute("143 Main St, Salt Lake City, UT", "555 Main St, Salt Lake City, UT");



    }


    public void ProcessRoute(Route route)
    {
        System.out.println("Duration = " + route.GetDurationString() + ", " + route.GetDuration());
        DisplayRoute(route);
    }


    private void DisplayRoute(Route route) {
        if (route != null) {

            PolylineOptions opts = new PolylineOptions().
                    color(Color.BLUE).
                    width(10.0f);
            ArrayList<LatLng> latLngs = route.GetPoints();

            for (int j = 0; j < latLngs.size(); j++)
            {
                opts.add(latLngs.get(j));
            }

            m_Map.addPolyline(opts);
            m_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0),18));
        } else {
            System.err.println("Error adding route!");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_Map = googleMap;

    }
}
