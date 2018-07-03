package com.example.android.taweretgym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.android.taweretgym.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InstructorListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_INSTRUCTOR_ID = "instructor_id";
    private static final String KEY_INSTRUCTOR_FIRST_NAME = "first_name";
    private static final String KEY_INSTRUCTOR_LAST_NAME = "last_name";
    private static final String KEY_INSTRUCTOR_EMAIL_ADDRESS = "email_address";
    private static final String KEY_INSTRUCTOR_GENDER = "gender";
    private static final String KEY_INSTRUCTOR_PHOTO = "photo";
    private static final String KEY_INSTRUCTOR_PHONE = "phone_number";
    private static final String BASE_URL = "http://192.168.100.4/taweret/";
    private ArrayList<HashMap<String, String>> instructorList;
    private ListView instructorListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_listing);

        instructorListView = findViewById(R.id.instructorList);
        new FetchInstructorsAsyncTask().execute();
    }
    /**
     * Fetches the list of instructors from the server
     */
    private class FetchInstructorsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(InstructorListingActivity.this);
            pDialog.setMessage(getString(R.string.ins_load));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetch_all_instructors.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray instructors;
                if (success == 1) {
                    instructorList = new ArrayList<>();
                    instructors = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate instructors list
                    for (int i = 0; i < instructors.length(); i++) {
                        JSONObject instructor = instructors.getJSONObject(i);
                        String instructorPhoto = instructor.getString(KEY_INSTRUCTOR_PHOTO);
                        Integer instructorId = instructor.getInt(KEY_INSTRUCTOR_ID);
                        String instructorFName = instructor.getString(KEY_INSTRUCTOR_FIRST_NAME);
                        String instructorLName = instructor.getString(KEY_INSTRUCTOR_LAST_NAME);
                        String instructorGender = instructor.getString(KEY_INSTRUCTOR_GENDER);
                        String instructorPhone = instructor.getString(KEY_INSTRUCTOR_PHONE);
                        String instructorEmail= instructor.getString(KEY_INSTRUCTOR_EMAIL_ADDRESS);

                        HashMap<String, String> map = new HashMap<>();
                        map.put(KEY_INSTRUCTOR_PHOTO, instructorPhoto);
                        map.put(KEY_INSTRUCTOR_ID, instructorId.toString());
                        map.put(KEY_INSTRUCTOR_FIRST_NAME, instructorFName);
                        map.put(KEY_INSTRUCTOR_LAST_NAME, instructorLName);
                        map.put(KEY_INSTRUCTOR_GENDER, instructorGender);
                        map.put(KEY_INSTRUCTOR_PHONE, instructorPhone);
                        map.put(KEY_INSTRUCTOR_EMAIL_ADDRESS, instructorEmail);

                        instructorList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    populateInstructorList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateInstructorList() {
        ListAdapter adapter = new SimpleAdapter(
                InstructorListingActivity.this, instructorList,
                R.layout.instructor_list_item, new String[]{KEY_INSTRUCTOR_PHOTO, KEY_INSTRUCTOR_ID,
                KEY_INSTRUCTOR_FIRST_NAME, KEY_INSTRUCTOR_LAST_NAME, KEY_INSTRUCTOR_GENDER, KEY_INSTRUCTOR_PHONE, KEY_INSTRUCTOR_EMAIL_ADDRESS},
                new int[]{R.id.instructorPhoto, R.id.instructorId, R.id.instructorFirstName, R.id.instructorLastName, R.id.instructorGender, R.id.instructorPhoneNumber, R.id.instructorEmailAddress});
        // updating listview
        instructorListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            // If the result code is 20 that means that
            // the user has deleted/updated the instructor.
            // So refresh the instructor listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}