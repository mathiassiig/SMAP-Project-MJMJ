package mathiassiig.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class MainActivity extends AppCompatActivity {

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

    public void button_manualWeatherCheck(View view)
    {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
        stopService(backgroundServiceIntent);
        backgroundServiceIntent.putExtra("manual",true);
        startWeatherRequestService();
    }

    public void startWeatherRequestService()
    {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, weatherService.class);
        startService(backgroundServiceIntent);
    }

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
        int icon = WeatherInfoAdapter.GetWeatherIcon(current.description);


        ImageView img = (ImageView)findViewById(R.id.imgViewCurrent);
        img.setImageResource(icon);
        desc.setText(current.description);
        temp.setText(Double.toString(current.temperature));
    }

    private void handleBackgroundResult(ArrayList<WeatherInfo> weatherInfos)
    {
        adapter.clear();
        WeatherInfo current = weatherInfos.get(0);
        UpdateCurrentWeather(current);
        weatherInfos.remove(0);
        if(weatherInfos.size()>0)
        for(int i = 1; i < weatherInfos.size();i++)
        {
            adapter.add(weatherInfos.get(i));
        }
    }

}
