package com.example.salun.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    public static final int REQUEST_CODE = 10;
    protected Button mainButton;
    protected Button startChatButton;
    protected Button weatherForecastButton;
    protected Button testToolbarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        weatherForecastButton = findViewById(R.id.startWeatherForecastButton);
        testToolbarButton = findViewById(R.id.startTestToolbar);
        mainButton = (Button)findViewById(R.id.buttonStart1);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listItemIntent = new Intent(getApplicationContext(),ListItemsActivity.class);
                startActivityForResult(listItemIntent,REQUEST_CODE);
            }
        });
        Log.i(ACTIVITY_NAME, "In onCreate()");
        startChatButton = (Button)findViewById(R.id.startChatButton);
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
            }
        });

        startChatButton = (Button)findViewById(R.id.startChatButton);
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getApplicationContext(),ChatWindow.class);
                startActivity(chatIntent);
            }
        });

        weatherForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherIntent = new Intent(getApplicationContext(),WeatherForecast.class);
                startActivity(weatherIntent);
            }
        });

        testToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toolbarIntent = new Intent(getApplicationContext(), TestToolbar.class);
                startActivity(toolbarIntent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                CharSequence messagePassed = getString(R.string.listItemActivityInfoPassPrefix) + data.getStringExtra("Response");
                Toast toast = Toast.makeText(StartActivity.this, messagePassed, Toast.LENGTH_LONG);
                toast.show();
            }
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
    }
}