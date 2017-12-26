package com.example.salun.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    protected EditText emailField;
    protected Button loginButton;
    protected SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        // get the last email address from the file "MyData"
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailFromPrefs = prefs.getString("DefaultEmail","email@domain.com");

        // set the value of the login email EditText
        emailField = (EditText)findViewById(R.id.emailLoginField);
        emailField.setText(emailFromPrefs);

        // function to store email in the file "MyData"
        loginButton = (Button)findViewById(R.id.emailLoginButton); // grab reference to login button
        loginButton.setOnClickListener(new View.OnClickListener() { // function to handle clicks
            @Override
            public void onClick(View view) {
                // save the current value in the EditText box to "MyData"
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("DefaultEmail",emailField.getText().toString());
                edit.commit();
                // go to StartActivity
                Intent startIntent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(startIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}