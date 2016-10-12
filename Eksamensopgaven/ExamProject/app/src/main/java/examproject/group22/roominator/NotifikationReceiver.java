package examproject.group22.roominator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifikationReceiver extends BroadcastReceiver {
    public NotifikationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Debug", "Recieved Pending intent");
        Intent i = new Intent(context,NotifikationService.class);
        context.startService(i);
    }
}
