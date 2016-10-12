package examproject.group22.roominator;

import android.app.AlarmManager;
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

import java.util.Calendar;
import java.util.List;

import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;

public class NotificationService extends Service {
    private static final int LOOP_TIME = 1;
    private static final String SHARED_GROCERY_FILE = "Groceries";

    private final IBinder binder = new LocalBinder();

    SharedPreferences sharedPref;
    DatabaseService db;
    List<GroceryItem> db_Groceries;
    Apartment db_apartment;

    @Override
    public void onCreate() {
        Log.v("Debug","NotificationService is created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Debug","NotificationService has started");
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_ALL_GROCERIES_IN_APARTMENT));
        checkDataBase();
        setUpAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

    public void setUpAlarm() {
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), NotifikationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.v("Debug", "Setting up the alarm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, LOOP_TIME);
        aManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void checkDataBase() {
        Log.v("Debug","Checker data for changes");
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sharedPref = NotificationService.this.getSharedPreferences("Groceries", MODE_PRIVATE);
                    for (GroceryItem g : db_Groceries) {
                        int buyer = sharedPref.getInt(Integer.toString(g.id),-1);
                        if(buyer==-1 || buyer != g.buyerID)
                        {
                                notifyUser("changes made");
                                break;

                        }

                    }
                }catch (Exception e){
                    Log.v("Debug",e.toString());
                }
            }
        };
        t.start();
    }

    public void notifyUser(String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.shoppingicon).
                        setContentTitle("Shoppinglist Update").
                        setContentText("Someone has made changes in the shoppinglist");
    }
    public BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            db_apartment = (Apartment)intent.getSerializableExtra("apartment");
            db_Groceries = db_apartment.groceries;
            Log.v("Debug","notifyService has recieved apartment from db");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //https://www.youtube.com/watch?v=0c4jRCm353c
    //https://developer.android.com/guide/components/bound-services.html
    public class LocalBinder extends Binder {
       public NotificationService getService() {
            // Return this instance of weatherService so clients can call public methods
            return NotificationService.this;
        }
    }
}
