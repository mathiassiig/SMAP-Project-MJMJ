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
import java.util.Map;

import examproject.group22.roominator.Activities.OverviewActivity;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;

public class NotifikationService extends Service {
    private static final int LOOP_TIME = 1;
    private static final String SHARED_GROCERY_FILE = "Groceries";

    private final IBinder binder = new LocalBinder();

    Context current_Context;
    SharedPreferences sharedPref;
    DatabaseService db;
    List<GroceryItem> db_Groceries;
    Apartment db_apartment;


    public NotifikationService(Context context) {
        current_Context = context;
    }

    @Override
    public void onCreate() {
        Log.v("Debug","NotificationService is created");
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_ALL_GROCERIES_IN_APARTMENT));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Debug","NotificationService has started");;
        checkDataBase();
        setUpAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

    public void setUpAlarm() {
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), NotifikationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotifikationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                sharedPref = NotifikationService.this.getSharedPreferences("Groceries",MODE_PRIVATE);
                List<String> sharedGroceries = (List<String>) sharedPref.getAll();
                for (GroceryItem g:db_Groceries) {
                    String gId = Integer.toString(g.id);
                    String gBuyerId = Integer.toString(g.buyerID);
                    String groceryCheckString = gId+gBuyerId;
                    if(sharedGroceries.contains(groceryCheckString)){
                        notifyUser("User:"+ gBuyerId + ". Has made changes to your shared shopping list");
                    }
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
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //https://www.youtube.com/watch?v=0c4jRCm353c
    //https://developer.android.com/guide/components/bound-services.html
    public class LocalBinder extends Binder {
       public NotifikationService getService() {
            // Return this instance of weatherService so clients can call public methods
            return NotifikationService.this;
        }
    }
}
