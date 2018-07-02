package com.example.android.taweretgym;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AddWorkoutActivity extends AppCompatActivity {
    private Button btnViewSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        Spinner dropdown = findViewById(R.id.txtTypeWorkout);
        String[] items = new String[]{"Lifting Weights", "Yoga", "Aerobic Exercises"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        btnViewSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddWorkoutActivity.this, SessionListingActivity.class);
                startActivity(intent);
            }
        });
    }

}
