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

public class SessionListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_SESSION_ID = "session_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EXERCISE_TYPE = "type";
    private static final String KEY_SESSION_LOCATION = "location";
    private static final String KEY_SESSION_SETS = "sets";
    private static final String KEY_SESSION_DATE = "date";
    private static final String BASE_URL = "http://192.168.100.4/taweret/";
    private ArrayList<HashMap<String, String>> sessionList;
    private ListView sessionListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_listing);
        sessionListView = findViewById(R.id.sessionList);
        new FetchSessionsAsyncTask().execute();
    }
    /**
     * Fetches the list of past sessions from the server
     */
    private class FetchSessionsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(SessionListingActivity.this);
            pDialog.setMessage("Loading sessions. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetch_all_sessions.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray sessions;
                if (success == 1) {
                    sessionList = new ArrayList<>();
                    sessions = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate sessions list
                    for (int i = 0; i < sessions.length(); i++) {
                        JSONObject session = sessions.getJSONObject(i);

                        Integer sessionId = session.getInt(KEY_SESSION_ID);
                        Integer userId = session.getInt(KEY_USER_ID);
                        String exerciseType = session.getString(KEY_EXERCISE_TYPE);
                        String sessionLocation = session.getString(KEY_SESSION_LOCATION);
                        Integer sessionSets = session.getInt(KEY_SESSION_SETS);
                        String sessionDate = session.getString(KEY_SESSION_DATE);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_SESSION_ID, sessionId.toString());
                        map.put(KEY_USER_ID, userId.toString());
                        map.put(KEY_EXERCISE_TYPE, exerciseType);
                        map.put(KEY_SESSION_LOCATION, sessionLocation);
                        map.put(KEY_SESSION_SETS, sessionSets.toString());
                        map.put(KEY_SESSION_DATE, sessionDate);

                        sessionList.add(map);
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
                    populateSessionList();
                }
            });
        }

    }
    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateSessionList() {
        ListAdapter adapter = new SimpleAdapter(
                SessionListingActivity.this, sessionList,
                R.layout.session_list_item, new String[]{KEY_USER_ID,
                KEY_SESSION_ID, KEY_EXERCISE_TYPE, KEY_SESSION_LOCATION, KEY_SESSION_SETS, KEY_SESSION_DATE},
                new int[]{R.id.userId, R.id.sessionId, R.id.exerciseType, R.id.sessionLocation, R.id.sessionSets, R.id.sessionDate});
        // updating listview
        sessionListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            // If the result code is 20 that means that
            // the user has deleted/updated the session.
            // So refresh the session listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}