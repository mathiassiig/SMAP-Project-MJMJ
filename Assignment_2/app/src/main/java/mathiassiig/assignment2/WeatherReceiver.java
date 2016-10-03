package mathiassiig.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WeatherReceiver extends BroadcastReceiver {
    public WeatherReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.v("Debug", "Received pendingIntent");
        Intent backgroundServiceIntent = new Intent(context, weatherService.class);
        context.startService(backgroundServiceIntent);
    }
}
