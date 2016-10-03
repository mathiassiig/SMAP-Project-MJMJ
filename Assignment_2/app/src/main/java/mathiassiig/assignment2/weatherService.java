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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class weatherService extends Service {

    public static final long LOOP_TIME = 30 * 60 * 1000; //30 minutter, 60 sekunder, 1000 millisekunder
    private DatabaseHelper dbinstance;

    private static final String API_KEY = "d5a8341b52c8adfc0b4ec902bf53261c"; //Jonas API
    private static final String ID_CITY = "Aarhus,dk";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=" + ID_CITY + "&appid=" + API_KEY + "&units=metric";

    public static final String INTENT_LOOP = "loopPassed";
    public static final String INTENT_CURRENT = "currentWeather";

    private final IBinder binder = new LocalBinder();
    public boolean ServiceStarted = false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceStarted = true;
        Log.v("Debug", "Starting service");
        dbinstance = DatabaseHelper.getInstance(getApplicationContext());
        runInBackground();
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

    private AsyncTask<Object, Object, String> backgroundTask;

    private void runInBackground() {
        Log.v("Debug", "Run in background called");
        Thread t = new Thread(){
            @Override
            public void run()
            {
                try {
                    WeatherInfo w = getCurrentWeather();
                    broadcastWeather(w);
                    dbinstance.AddWeatherInfo(w);
                    broadcastTaskResult();
                    Thread.sleep(LOOP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
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

    private WeatherInfo getCurrentWeather()
    {
        String json = callURL(WEATHER_URL);
        WeatherInfo current = ParseJson(json);
        return current;
    }

    public void requestCurrentWeather()
    {
        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                WeatherInfo w = getCurrentWeather();
                broadcastWeather(w);
            }
        };
        t.start();
    }

    public ArrayList<WeatherInfo> getPastWeather()
    {
        dbinstance.PurgeOld();
        ArrayList<WeatherInfo> infos = dbinstance.GetAllWeatherInfos();
        Collections.reverse(infos);
        return infos;
    }

    private void broadcastWeather(WeatherInfo w)
    {
        Intent broadcastIntent = new Intent(INTENT_CURRENT);
        broadcastIntent.putExtra("current", w);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void broadcastTaskResult()
    {
        Log.v("Debug", "Broadcasting result back to actvity");
        Intent broadcastIntent = new Intent(INTENT_LOOP);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        ServiceStarted = false;
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
