package examproject.group22.roominator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Debug", "Recieved Pending intent");
        int id = intent.getIntExtra("apartmentID", 0);
        Intent i = new Intent(context,NotificationService.class);
        i.putExtra("apartmentID", id);
        context.startService(i);
    }
}
