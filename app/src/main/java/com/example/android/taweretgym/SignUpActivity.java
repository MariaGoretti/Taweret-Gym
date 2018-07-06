package com.example.android.taweretgym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.taweretgym.helper.CheckNetworkStatus;
import com.example.android.taweretgym.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL_ADDRESS_SIGNUP = "email_address";
    private static final String KEY_PASSWORD= "password";
    private static final String BASE_URL = "https://taweret.herokuapp.com/";
    private static String STRING_EMPTY = "";
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    String confirm_password;
    private String first_name;
    private String last_name;
    private String email_address;
    private String password;
    private EditText passwordConfirmEditText;
    private Button registerButtonSignup;
    private Button loginButtonSignup;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.txtFirstnameSignup);
        lastNameEditText = findViewById(R.id.txtLastnameSignup);
        emailEditText = findViewById(R.id.txtEmailSignup);
        passwordEditText = findViewById(R.id.txtPasswordSignup);
        passwordConfirmEditText = findViewById(R.id.txtPasswordConfirmSignup);
        registerButtonSignup = findViewById(R.id.btnSignupSignup);
        loginButtonSignup = findViewById(R.id.btnLoginSignup);

        loginButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registerButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addUser();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            R.string.no_internet,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checks whether all files are filled. If so then calls AddUserAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void addUser() {
        if (!STRING_EMPTY.equals(firstNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(lastNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(emailEditText.getText().toString()) &&
                !STRING_EMPTY.equals(passwordEditText.getText().toString()) &&
                !STRING_EMPTY.equals(passwordConfirmEditText.getText().toString())) {

            first_name = firstNameEditText.getText().toString();
            last_name = lastNameEditText.getText().toString();
            email_address = emailEditText.getText().toString().trim();
            password = passwordEditText.getText().toString();
            confirm_password = passwordConfirmEditText.getText().toString();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (email_address.matches(emailPattern)) {
                Toast.makeText(getApplicationContext(), "Valid email address", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            }


            if (!passwordConfirmEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                Toast.makeText(SignUpActivity.this, R.string.pass_match, Toast.LENGTH_SHORT).show();


            } else {
                new AddUserAsyncTask().execute();
            }
        } else {
            Toast.makeText(SignUpActivity.this,
                    R.string.fill_all,
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * AsyncTask for adding a user
     */
    private class AddUserAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage(getString(R.string.register_wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_FIRST_NAME, first_name);
            httpParams.put(KEY_LAST_NAME, last_name);
            httpParams.put(KEY_EMAIL_ADDRESS_SIGNUP, email_address);
            httpParams.put(KEY_PASSWORD, password);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "register", "POST", httpParams);
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
                        Toast.makeText(SignUpActivity.this,
                                R.string.registration_success, Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about user update
                        setResult(20, i);
                        //Finish ths activity and go to login activity

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this,
                                R.string.some_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}