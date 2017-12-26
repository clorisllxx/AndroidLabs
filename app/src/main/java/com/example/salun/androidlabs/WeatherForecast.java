package com.example.salun.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends Activity {

    ProgressBar progressBar;
    TextView tvMinTemp;
    TextView tvMaxTemp;
    TextView tvCurrentTemp;
    ImageView ivWeatherIcon;
    RelativeLayout weatherInfoTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        // handles to layout elements
        progressBar = findViewById(R.id.weatherProgressBar);
        tvMinTemp = findViewById(R.id.minTemperatureTextView);
        tvMaxTemp = findViewById(R.id.maxTemperatureTextView);
        tvCurrentTemp = findViewById(R.id.currentTemperatureTextView);
        ivWeatherIcon = findViewById(R.id.currentWeatherImageView);
        weatherInfoTextViews = findViewById(R.id.weatherInfoLayout);

        // get the weather forecast using ASyncTask below
        new ForecastQuery().execute();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private Bitmap imgWeather;
        private String iconName;

        @Override
        protected String doInBackground(String... strings) {
            try {
                getWeather("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            weatherInfoTextViews.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            tvMinTemp.setText(minTemp);
            tvMaxTemp.setText(maxTemp);
            tvCurrentTemp.setText(currentTemp);
            ivWeatherIcon.setImageBitmap(imgWeather);
            weatherInfoTextViews.setVisibility(View.VISIBLE);
        }

        private void getWeather(String urlString) throws IOException, XmlPullParserException {

            // make connection to URL (code from android developer website)
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            InputStream in =  conn.getInputStream();

            try {

                // instantiate a parser with the input stream from above
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);

                // while we are not at the end of the document
                while (parser.next() != XmlPullParser.END_DOCUMENT) {

                    // find a start tag called "temperature"
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("temperature")) {

                            // get the required values (value (i.e. current temp), min and max)
                            SystemClock.sleep(500);
                            currentTemp = parser.getAttributeValue(null, "value") + " C";
                            publishProgress(25);
                            SystemClock.sleep(500);
                            minTemp = parser.getAttributeValue(null, "min") + " C";
                            publishProgress(50);
                            SystemClock.sleep(500);
                            maxTemp = parser.getAttributeValue(null, "max") + " C";
                            publishProgress(75);
                        }

                        // get the weather icon
                        if (parser.getName().equals("weather")) {

                            // get the icon name from the XML and show log message saying that you are looking for file
                            iconName = parser.getAttributeValue(null, "icon");
                            String fileName = iconName + ".png";

                            // if the file does not already exist in local storage
                            if (!fileExistence(fileName)) {
                                // get the image from the URL
                                Log.i("Downloaded",fileName);
                                imgWeather = HTTPUtils.getImage("http://openweathermap.org/img/w/" + fileName);

                                // save to local storage
                                FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                imgWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            }

                            // if it does already exist, go get it from local storage
                            else {
                                Log.i("Found in storage", fileName);
                                FileInputStream fIS = null;
                                try {
                                    fIS = openFileInput(fileName);
                                }
                                catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                // set reference to image
                                imgWeather = BitmapFactory.decodeStream(fIS);
                            }

                            // process is finished, show 100 %
                            SystemClock.sleep(500);
                            publishProgress(100);
                        }
                    }
                }
            }
            finally {
                in.close();
            }
        }
    }
    public boolean fileExistence(String fName){
        File file = getBaseContext().getFileStreamPath(fName);
        return file.exists();   }

}