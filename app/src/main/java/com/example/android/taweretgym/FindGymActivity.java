package com.example.android.taweretgym;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindGymActivity extends FragmentActivity implements OnMapReadyCallback {
    private LocationListener locationListener;
    private LocationManager locationManager;
    private LatLng latLng;
    private GoogleMap mMap;
    Location mLocation;
    Location mLocation1;
    Location mLocation2;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_gym);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
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
        mMap = googleMap;

        //LatLng nairobi = new LatLng(-1.2833300, 36.8166700);
        //mMap.addMarker(new MarkerOptions().position(nairobi).title(getString(R.string.my_marker)));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(nairobi));
        mLocation = new Location("network");
        mLocation.setLatitude(-1.2833300);
        mLocation.setLongitude(36.8166700);

        LatLng pname = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pname).title(getString(R.string.nbi_marker)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(pname));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pname)
                .zoom(15)
                .bearing(90)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //location 1
        mLocation1 = new Location("network");
        mLocation1.setLatitude(-4.036878);
        mLocation1.setLongitude(39.669571);

        LatLng pname1 = new LatLng(mLocation1.getLatitude(), mLocation1.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pname1).title(getString(R.string.nbi_marker)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(pname1));
        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                .target(pname)
                .zoom(15)
                .bearing(90)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));

        //location 2
        mLocation2 = new Location("network");
        mLocation2.setLatitude(-0.303099);
        mLocation2.setLongitude(36.080025);

        LatLng pname2 = new LatLng(mLocation2.getLatitude(), mLocation2.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pname2).title(getString(R.string.nbi_marker)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(pname2));
        CameraPosition cameraPosition2 = new CameraPosition.Builder()
                .target(pname)
                .zoom(15)
                .bearing(90)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.my_marker)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e){
            e.printStackTrace();
        }

    }
}
