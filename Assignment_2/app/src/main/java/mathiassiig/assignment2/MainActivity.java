package mathiassiig.assignment2;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, new IntentFilter(weatherService.INTENT_LOOP));
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, new IntentFilter(weatherService.INTENT_CURRENT));

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to weatherService
        Intent intent = new Intent(this, weatherService.class);
        bindService(intent, serviceConnection, 0);
        startWeatherRequestService();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void SetUpListView()
    {
        weatherInfos = new ArrayList<>();
        adapter = new WeatherInfoAdapter(this, weatherInfos);
        weatherListView.setAdapter(adapter);
    }

    public void button_manualWeatherCheck(View view)
    {
        weatherService.requestCurrentWeather();
    }

    public void startWeatherRequestService()
    {
        if(weatherService == null) {
            Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
            startService(backgroundServiceIntent);
        }
    }

    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equalsIgnoreCase(weatherService.INTENT_LOOP))
            {
                ArrayList<WeatherInfo> weatherInfoList = weatherService.getPastWeather();
                handleBackgroundResult(weatherInfoList);
            }
            else if (action.equalsIgnoreCase(weatherService.INTENT_CURRENT))
            {
                WeatherInfo w = (WeatherInfo) intent.getSerializableExtra("current");
                UpdateCurrentWeather(w);
            }
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
        for(int i = 0; i < weatherInfos.size();i++)
        {
            adapter.add(weatherInfos.get(i));
        }
    }

    // https://www.youtube.com/watch?v=zCj5Qzzex_A
    // https://developer.android.com/guide/components/bound-services.html
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
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
