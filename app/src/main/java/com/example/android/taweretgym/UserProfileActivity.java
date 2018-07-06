package com.example.android.taweretgym;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected Context context;
    TextView txtLat;

    ImageView profileImage;
    TextView nameUser, locationUser, txtGender, txtAge, txtEmailAddressProfile, txtCurrentWeight, txtTargetWeight, prefWorkoutLoc;
    EditText txtEmailAddressProfileEdit, txtCurrentWeightEdit, txtTargetWeightEdit, prefWorkoutLocEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        profileImage = findViewById(R.id.profileImage);
        locationUser = findViewById(R.id.locationUser);
        txtGender = findViewById(R.id.txtFirstNameProfile);
        txtAge = findViewById(R.id.txtLastNameProfile);
        txtEmailAddressProfile = findViewById(R.id.txtEmailAddressProfile);
        txtCurrentWeight = findViewById(R.id.txtCurrentWeight);
        txtTargetWeight = findViewById(R.id.txtTargetWeight);
        prefWorkoutLoc = findViewById(R.id.prefWorkoutLoc);
        txtEmailAddressProfileEdit = findViewById(R.id.txtEmailAddressProfileEdit);
        txtCurrentWeightEdit = findViewById(R.id.txtCurrentWeightEdit);
        txtTargetWeightEdit = findViewById(R.id.txtTargetWeightEdit);
        prefWorkoutLocEdit = findViewById(R.id.prefWorkoutLocEdit);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onLocationChanged(Location location) {
        txtLat = findViewById(R.id.locationUser);
        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Location lastKnownLocation = null;
        List<String> providers = null;
        if (locationManager != null) providers = locationManager.getAllProviders();

        if (providers != null) {
            for (int i = 0; i < providers.size(); i++) {
                if (locationManager != null)
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                lastKnownLocation = locationManager.getLastKnownLocation(providers.get(i));
                if (lastKnownLocation != null) {
                    LatLng position = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    break;
                }
            }
        }
        Log.d("Latitude", "disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }
}
