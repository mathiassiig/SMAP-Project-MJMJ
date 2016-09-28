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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "d5a8341b52c8adfc0b4ec902bf53261c"; //Jonas API
    private static final String ID_CITY = "Aarhus,dk";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast/weather?q=" + ID_CITY + "&appid=" + API_KEY;
    //http://api.openweathermap.org/data/2.5/weather?q=Aarhus,dk&appid=d5a8341b52c8adfc0b4ec902bf53261c

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, new IntentFilter("weatherInfo"));
    }

    public void weatherRequest(View view) {
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
            String result = intent.getStringExtra("Result");
            if(result==null){
                result = "Error";
            }
            handleBackgroundResult(result);
        }
    };

    private void handleBackgroundResult(String result){
        Toast.makeText(MainActivity.this, "Got result from background service:\n" + result, Toast.LENGTH_SHORT).show();
    }

}
