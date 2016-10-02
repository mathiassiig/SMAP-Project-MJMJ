package mathiassiig.assignment2;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class weatherService extends Service {

    public static final long LOOP_TIME = 30 * 60 * 1000; //30 minutter, 60 sekunder, 1000 millisekunder
    private boolean started = false;
    private DatabaseHelper dbinstance;

    private static final String API_KEY = "d5a8341b52c8adfc0b4ec902bf53261c"; //Jonas API
    private static final String ID_CITY = "Aarhus,dk";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=" + ID_CITY + "&appid=" + API_KEY + "&units=metric";

    private final IBinder binder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Debug", "Starting service");
        started = true;
        dbinstance = DatabaseHelper.getInstance(getApplicationContext());
        FetchCurrentWeather();
        return super.onStartCommand(intent, flags, startId);
    }

    public WeatherInfo ParseJson(String json) {
        Log.v("Debug", "Parsing JSON");
        WeatherInfo weather = null;
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray weatherObject = jsonResponse.getJSONArray("weather");
            String weatherMain = weatherObject.getJSONObject(0).getString("main");

            JSONObject mainObject = jsonResponse.getJSONObject("main");
            double temperature = mainObject.getDouble("temp");
            java.sql.Timestamp timeNow = new java.sql.Timestamp(System.currentTimeMillis());

            weather = new WeatherInfo(weatherMain, temperature, timeNow);
            Log.v("Debug", "Parsing JSON went OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }

    private boolean FirstTimeRunning;
    private AsyncTask<Object, Object, String> backgroundTask;

    private void runInBackground() {
        Log.v("Debug", "Run in background called");
        backgroundTask = new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object[] params) {
                try {
                    Log.v("Debug", "AsyncTask: Do in Background Called");
                    if (!FirstTimeRunning) {
                        Log.v("Debug", "AsyncTask: Sleeping for " + LOOP_TIME + " milliseconds");
                        Thread.sleep(LOOP_TIME);
                    }
                    FirstTimeRunning = false;
                } catch (Exception e) {
                    Log.v("Debug", e.getMessage());
                }

                return callURL(WEATHER_URL);
            }

            @Override
            protected void onPostExecute(String stringResult) {

                super.onPostExecute(stringResult);
                Log.v("Debug", "AsyncTask: onPostExecute");
                WeatherInfo currentWeather = ParseJson(stringResult);
                dbinstance.PurgeOld();
                dbinstance.AddWeatherInfo(currentWeather);
                ArrayList<WeatherInfo> weatherInfos = dbinstance.GetAllWeatherInfos();

                broadcastTaskResult(weatherInfos);
                if (started)
                    runInBackground();
            }
        };
        backgroundTask.execute();
    }

    public void FetchCurrentWeather() {
        Log.v("Debug", "Fetch current weather called");
        FirstTimeRunning = true;
        runInBackground();
    }


    //Weather Service demo kode fra undervisningen
    private String callURL(String callUrl) {

        InputStream is = null;

        try {
            Log.v("Debug", "Calling URL");
            URL url = new URL(callUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            try {
                conn.connect();
            } catch (Exception ex) {
                Log.v("Debug", ex.getMessage());
            }
            is = conn.getInputStream();
            String weatherToString = convertStreamToStringBuffered(is);
            Log.v("Debug", "HTTP request was OK");
            return weatherToString;

        } catch (ProtocolException pe) {

        } catch (UnsupportedEncodingException uee) {

        } catch (IOException ioe) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {

                }
            }
        }
        return null;
    }

    //Weather Service demo kode fra undervisningen
    private String convertStreamToStringBuffered(InputStream is) {
        String s = "";
        String line;
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                s += line;
            }
        } catch (IOException ex) {

        }
        return s;
    }

    //http://stackoverflow.com/questions/13601883/how-to-pass-arraylist-of-objects-from-one-to-another-activity-using-intent-in-an
    private void broadcastTaskResult(ArrayList<WeatherInfo> weatherInfos) {
        Log.v("Debug", "Broadcasting result back to actvity");
        Collections.reverse(weatherInfos); //Nyeste kommer øverst
        Intent broadcastIntent = new Intent("weatherInfo");
        broadcastIntent.putExtra("WeatherInfoList", weatherInfos);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        Log.v("Debug", "Service onDestroy");
        started = false;
        backgroundTask.cancel(true);
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //https://www.youtube.com/watch?v=0c4jRCm353c
    //https://developer.android.com/guide/components/bound-services.html
    public class LocalBinder extends Binder {
        weatherService getService() {
            // Return this instance of weatherService so clients can call public methods
            return weatherService.this;
        }
    }
}
