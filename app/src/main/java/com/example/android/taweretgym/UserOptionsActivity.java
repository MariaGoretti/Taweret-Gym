package com.example.android.taweretgym;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class UserOptionsActivity extends AppCompatActivity {
    CardView btnGymsPage, btnInstructorsPage, btnSessionsPage, btnProfilePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        btnGymsPage = findViewById(R.id.btnGymsPage);
        btnInstructorsPage = findViewById(R.id.btnInstructorsPage);
        btnSessionsPage = findViewById(R.id.btnSessionsPage);
        btnProfilePage = findViewById(R.id.btnProfilePage);

       btnProfilePage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(UserOptionsActivity.this, UserProfileActivity.class);
               startActivity(intent);
           }
       });

       btnSessionsPage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(UserOptionsActivity.this, AddWorkoutActivity.class);
               startActivity(intent);
           }
       });

       btnInstructorsPage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(UserOptionsActivity.this, InstructorListingActivity.class);
               startActivity(intent);
           }
       });

       btnGymsPage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(UserOptionsActivity.this, FindGymActivity.class);
               startActivity(intent);
           }
       });

      /* btnLogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(UserOptionsActivity.this, MainActivity.class);
               startActivity(intent);
               finish();
           }
       });*/
    }
}
