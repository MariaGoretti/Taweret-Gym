package com.example.android.taweretgym;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {
    Button addViewWorkouts;
    ImageView profileImage;
    TextView nameUser, locationUser, txtGender, txtAge, txtEmailAddressProfile, txtCurrentWeight, txtTargetWeight, prefWorkoutLoc;
    EditText txtEmailAddressProfileEdit, txtCurrentWeightEdit, txtTargetWeightEdit, prefWorkoutLocEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        addViewWorkouts = findViewById(R.id.addViewWorkouts);
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
}
