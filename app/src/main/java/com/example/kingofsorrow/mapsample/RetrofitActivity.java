package com.example.kingofsorrow.mapsample;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.kingofsorrow.mapsample.network.ApiHelper;
import com.example.kingofsorrow.mapsample.network.RequestCallBack;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.mapbox.services.commons.geojson.GeoJSON;
import com.nextome.geojsonify.GeoJsonify;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class RetrofitActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String JSON_KEY = "json";
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Restore preferences
        settings = getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(JSON_KEY, "");
        if (StringUtils.isEmpty(value)) {
            String url = "http://api.tiles.mapbox.com/v3/mapbox.o11ipb8h/markers.geojson";
            ApiHelper.getInstance().getLayer(this, url,
                    new RequestCallBack() {
                        @Override
                        public void onResponse(Object response) {
                            if (response != null) {
                                String json = "";
                                try {
                                    // We need an Editor object to make preference changes.
                                    // All objects are from android.context.Context
                                    json = ((ResponseBody) response).string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    json = "";
                                }
                                editor.putString(JSON_KEY, json);
                                // Commit the edits!
                                editor.commit();
                            }
                        }
                    }
            );
        }else{
            try {
                JSONObject geoJson = new JSONObject(value.toString());
                GeoJsonLayer layer = new GeoJsonLayer(mMap,geoJson);
                layer.addLayerToMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
