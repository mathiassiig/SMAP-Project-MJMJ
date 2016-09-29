package mathiassiig.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "d5a8341b52c8adfc0b4ec902bf53261c"; //Jonas API
    private static final String ID_CITY = "Aarhus,dk";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=" + ID_CITY + "&appid=" + API_KEY+"&units=metric";
    //http://api.openweathermap.org/data/2.5/weather?q=Aarhus,dk&appid=d5a8341b52c8adfc0b4ec902bf53261c&units=metric
    WeatherInfoAdapter adapter;
    ArrayList<WeatherInfo> weatherInfos;
    ListView weatherListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherListView = (ListView) findViewById(R.id.weatherListView);
        SetUpListView();
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, new IntentFilter("weatherInfo"));
        startWeatherRequestService();
    }

    private void SetUpListView()
    {
        weatherInfos = new ArrayList<>();
        adapter = new WeatherInfoAdapter(this, weatherInfos);
        weatherListView.setAdapter(adapter);
    }

    public void startWeatherRequestService()
    {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
        backgroundServiceIntent.putExtra("weather", WEATHER_URL);
        startService(backgroundServiceIntent);

    }

    /*
    private boolean checkConnection() {
        ConnectivityManager connectionManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "You are connected " +
                    networkInfo.toString(), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(MainActivity.this, "No connection " + networkInfo.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    */

    /*
    private void stopBackgroundService()
    {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
        stopService(backgroundServiceIntent);
    }*/

    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<WeatherInfo> weatherInfoList = new ArrayList<WeatherInfo>();
            weatherInfoList = (ArrayList<WeatherInfo>)intent.getSerializableExtra("WeatherInfoList");
            handleBackgroundResult(weatherInfoList);
        }
    };

    private void handleBackgroundResult(ArrayList<WeatherInfo> weatherInfos)
    {
        adapter.clear();
        for(int i = 0; i < weatherInfos.size();i++)
        {
            adapter.add(weatherInfos.get(i));
        }
        //Toast.makeText(MainActivity.this,  weatherInfos.get(0).description, Toast.LENGTH_SHORT).show();
    }

}
