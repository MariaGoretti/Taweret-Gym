package com.example.android.taweretgym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.taweretgym.helper.CheckNetworkStatus;
import com.example.android.taweretgym.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddWorkoutActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_EXERCISE_TYPE = "exercise";
    private static final String KEY_SETS = "sets";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "session_date";
    private static final String BASE_URL = "https://taweret.herokuapp.com/";
    private static String STRING_EMPTY = "";
    Button btnViewSessions, btnLogout;
    private Spinner exerciseSpinner;
    private EditText setsEditText;
    private Spinner locationSpinner;
    private EditText dateEditText;
    private String exercise;
    private String sets;
    private String session_date;
    private String location;
    private Button btnAddWorkout;
    private int success;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        btnViewSessions = findViewById(R.id.btnViewSessions);
        exerciseSpinner = findViewById(R.id.txtTypeWorkout);
        setsEditText = findViewById(R.id.txtSetsWorkout);
        dateEditText = findViewById(R.id.txtDateWorkout);
        locationSpinner = findViewById(R.id.txtLocationWorkout);
        btnAddWorkout = findViewById(R.id.btnAddWorkout);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addSession();
                } else {
                    Toast.makeText(AddWorkoutActivity.this, R.string.no_internet,
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        Spinner type = findViewById(R.id.txtTypeWorkout);
        String[] items = new String[]{getString(R.string.weights), getString(R.string.yoga), getString(R.string.aerobics)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        type.setAdapter(adapter);

        Spinner location = findViewById(R.id.txtLocationWorkout);
        String[] items1 = new String[]{getString(R.string.nairobi), getString(R.string.nakuru), getString(R.string.mombasa)};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        location.setAdapter(adapter1);

        btnViewSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent intent = new Intent(AddWorkoutActivity.this, SessionListingActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddWorkoutActivity.this, R.string.no_internet,
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddWorkoutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void addSession() {
        if (!STRING_EMPTY.equals(setsEditText.getText().toString()) &&
                !STRING_EMPTY.equals(dateEditText.getText().toString()) &&
                !STRING_EMPTY.equals(exerciseSpinner.getSelectedItem().toString()) &&
                !STRING_EMPTY.equals(locationSpinner.getSelectedItem().toString())) {

            exercise = exerciseSpinner.getSelectedItem().toString();
            location = locationSpinner.getSelectedItem().toString();
            session_date = dateEditText.getText().toString();
            sets = setsEditText.getText().toString();
            new AddSessionAsyncTask().execute();
        } else {
            Toast.makeText(AddWorkoutActivity.this, R.string.fill_all,
                    Toast.LENGTH_LONG).show();

        }
    }

    /**
     * AsyncTask for adding a session
     */
    private class AddSessionAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(AddWorkoutActivity.this);
            pDialog.setMessage(getString(R.string.add_sess_dialog));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_EXERCISE_TYPE, exercise);
            httpParams.put(KEY_SETS, sets);
            httpParams.put(KEY_DATE, session_date);
            httpParams.put(KEY_LOCATION, location);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_session", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(AddWorkoutActivity.this, R.string.sess_added, Toast.LENGTH_LONG).show();
                        Intent i = getIntent();

                        setResult(20, i);
                        Intent intent = new Intent(AddWorkoutActivity.this, SessionListingActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(AddWorkoutActivity.this, R.string.some_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
