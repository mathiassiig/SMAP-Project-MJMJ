package mathiassiig.assignment2;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import mathiassiig.assignment2.weatherService.LocalBinder;

public class MainActivity extends AppCompatActivity {

    weatherService weatherService;
    boolean isBound = false;

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
        //startWeatherRequestService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to weatherService
        Intent intent = new Intent(this, weatherService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from weatherService
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void SetUpListView()
    {
        weatherInfos = new ArrayList<>();
        adapter = new WeatherInfoAdapter(this, weatherInfos);
        weatherListView.setAdapter(adapter);
    }

    public void button_manualWeatherCheck(View view)
    {
        //weatherService.FetchCurrentWeather();
        //stopWeatherService();
        //startWeatherRequestService();
    }

    /*private void stopWeatherService()
    {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
        stopService(backgroundServiceIntent);
    }

    public void startWeatherRequestService()
    {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
        startService(backgroundServiceIntent);
    }*/

    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<WeatherInfo> weatherInfoList = new ArrayList<WeatherInfo>();
            weatherInfoList = (ArrayList<WeatherInfo>)intent.getSerializableExtra("WeatherInfoList");
            handleBackgroundResult(weatherInfoList);
        }
    };

    private void UpdateCurrentWeather(WeatherInfo current)
    {
        TextView desc = (TextView)findViewById(R.id.txtCurrentDescription);
        TextView temp = (TextView)findViewById(R.id.txtCurrentTemp);
        TextView time = (TextView)findViewById(R.id.txtCurrentTime);
        int icon = WeatherInfoAdapter.GetWeatherIcon(current.description);


        ImageView img = (ImageView)findViewById(R.id.imgViewCurrent);
        img.setImageResource(icon);
        int localized = WeatherInfoAdapter.GetWeatherText(current.description);
        if(localized != -1)
            desc.setText(localized);
        else
            desc.setText(current.description);
        temp.setText(Double.toString(Math.round(current.temperature)) + " C");
        time.setText(WeatherInfoAdapter.getTime(current.timestamp));
    }

    private void handleBackgroundResult(ArrayList<WeatherInfo> weatherInfos)
    {
        adapter.clear();
        WeatherInfo current = weatherInfos.get(0);
        UpdateCurrentWeather(current);
        weatherInfos.remove(0);
        for(int i = 0; i < weatherInfos.size();i++)
        {
            adapter.add(weatherInfos.get(i));
        }
    }

    @Override
    public void onDestroy()
    {
        //stopWeatherService();
    }


    // https://www.youtube.com/watch?v=zCj5Qzzex_A
    // https://developer.android.com/guide/components/bound-services.html
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            weatherService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
