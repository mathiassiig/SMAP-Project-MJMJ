package examproject.group22.roominator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import examproject.group22.roominator.Activities.LoginActivity;
import examproject.group22.roominator.Activities.OverviewActivity;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;

public class NotificationService extends Service {
    private static final int LOOP_TIME = 5;
    private static final String SHARED_GROCERY_FILE = "Groceries";

    private final IBinder binder = new LocalBinder();

    private SharedPreferences sharedPref;
    private int apartment_id;

    @Override
    public void onCreate() {
        Log.v("Debug","NotificationService is created");
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_ALL_GROCERIES_IN_APARTMENT_SERVICE));
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        Log.v("Service", "Fuck dig din bøsse :) ");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Debug","NotificationService has started");
        checkDataBase();
        apartment_id = intent.getIntExtra("apartmentID", 0);
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    private void setUpAlarm() {
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), NotificationReceiver.class);
        intent.putExtra("apartmentID", apartment_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.v("Debug", "Setting up the alarm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, LOOP_TIME);
        aManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void compareResults(ArrayList<GroceryItem> groceries)
    {
        Log.v("Service","Comparing results");
        sharedPref = NotificationService.this.getSharedPreferences("Groceries", MODE_PRIVATE);
        for (GroceryItem g : groceries) {
            String key = Integer.toString(g.id);
                int buyer = sharedPref.getInt(key,-1);
            if(buyer==-1 || buyer != g.buyerID)
            {
                notifyUser();
                break;
            }
        }
    }

    private void checkDataBase() {
        Log.v("Debug","Checker data for changes");
        Thread t = new Thread() {
            @Override
            public void run()
            {
                DatabaseService.getInstance(getApplicationContext()).get_Apartment(apartment_id, true);
                setUpAlarm();
            }
        };
        t.start();
    }

    //https://developer.android.com/training/notify-user/build-notification.html#action
    private void notifyUser()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.shoppingicon).
                        setContentTitle(getResources().getString(R.string.notification_title)).
                        setContentText(getResources().getString(R.string.notification_content));

        Intent resultIntent = new Intent(this, LoginActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity( this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        int mNotificationId = 1;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private final BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Apartment a = (Apartment)intent.getSerializableExtra("apartment");
            ArrayList<GroceryItem> groceries = a.groceries;
            compareResults(groceries);
            Log.v("Debug","notifyService has recieved apartment from db");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //https://www.youtube.com/watch?v=0c4jRCm353c
    //https://developer.android.com/guide/components/bound-services.html
    private class LocalBinder extends Binder {
       public NotificationService getService() {
            // Return this instance of weatherService so clients can call public methods
            return NotificationService.this;
        }
    }
}
