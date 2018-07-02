package com.example.android.taweretgym;

//import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import com.example.android.taweretgym.helper.CheckNetworkStatus;
import com.example.android.taweretgym.helper.HttpJsonParser;



import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_EMAIL_ADDRESS = "email_address";
    private static final String KEY_PASSWORD= "password";
    private static final String BASE_URL = "http://192.168.100.3/taweret/";
    private static String STRING_EMPTY = "";
    private EditText txtEmailMain;
    private EditText txtPasswordMain;
    private int success;
    private ProgressDialog pDialog;
    private String email;
    private String password;
    private Button btnSignUpMain;
    private Button btnLoginMain;
    private Button btnChangeLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));*/

        btnLoginMain = findViewById(R.id.btnLoginMain);
        btnSignUpMain = findViewById(R.id.btnSignUpMain);
        txtEmailMain = findViewById(R.id.txtEmailMain);
        txtPasswordMain = findViewById(R.id.txtPasswordMain);
        btnChangeLang = findViewById(R.id.btnChangeLanguage);

        btnChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        btnLoginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    verifyUser();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Unable to connect to the internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSignUpMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English (Default)", "Kiswahili"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose language: ");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    setLocale("en-us");
                    recreate();
                }
                else {
                    setLocale("sw");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    private void verifyUser() {
        if (!STRING_EMPTY.equals(txtEmailMain.getText().toString()) &&
                !STRING_EMPTY.equals(txtPasswordMain.getText().toString())) {

            email = txtEmailMain.getText().toString();
            password = txtPasswordMain.getText().toString();
            new VerifyUserAsyncTask().execute();
        } else {
            Toast.makeText(MainActivity.this,
                    "Please fill in all the fields",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * AsyncTask for verifying a user
     */
    private class VerifyUserAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters

            httpParams.put(KEY_EMAIL_ADDRESS, email);
            httpParams.put(KEY_PASSWORD, password);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "login_user.php", "POST", httpParams);
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
                        Toast.makeText(MainActivity.this,
                                "Login Successful", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about user login
                        setResult(20, i);
                        //Finish ths activity and go to user options activity
                        Intent intent = new Intent(MainActivity.this, UserOptionsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Some error occurred. Please try again",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}