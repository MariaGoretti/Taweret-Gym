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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    TextView txtLat;
    String lat;
    String provider;
    Button addViewWorkouts;
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

        addViewWorkouts = findViewById(R.id.btnAddViewWorkouts);
        profileImage = findViewById(R.id.profileImage);
        nameUser = findViewById(R.id.nameUser);
        locationUser = findViewById(R.id.locationUser);
        txtGender = findViewById(R.id.txtGender);
        txtAge = findViewById(R.id.txtAge);
        txtEmailAddressProfile = findViewById(R.id.txtEmailAddressProfile);
        txtCurrentWeight = findViewById(R.id.txtCurrentWeight);
        txtTargetWeight = findViewById(R.id.txtTargetWeight);
        prefWorkoutLoc = findViewById(R.id.prefWorkoutLoc);
        txtEmailAddressProfileEdit = findViewById(R.id.txtEmailAddressProfileEdit);
        txtCurrentWeightEdit = findViewById(R.id.txtCurrentWeightEdit);
        txtTargetWeightEdit = findViewById(R.id.txtTargetWeightEdit);
        prefWorkoutLocEdit = findViewById(R.id.prefWorkoutLocEdit);

        addViewWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, AddWorkoutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
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
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");


    }
}
