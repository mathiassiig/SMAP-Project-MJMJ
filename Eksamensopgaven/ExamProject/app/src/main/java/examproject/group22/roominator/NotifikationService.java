package examproject.group22.roominator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import examproject.group22.roominator.Models.GroceryItem;

public class NotifikationService extends Service {
    private  static  final int LOOP_TIME =30*60;
    private  static  final  String SHARED_GROCERY_FILE = "Groceries";

    Context current_Context;
    SharedPreferences sharedPref;
    DatabaseService db;
    List<GroceryItem> db_Groceries;


    public NotifikationService(Context context) {
        current_Context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkDataBase();
        return super.onStartCommand(intent, flags, startId);
    }
    public void setUpAlarm()
    {
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), NotifikationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotifikationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.v("Debug", "Setting up the alarm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, LOOP_TIME);
        aManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void checkDataBase(){
        Thread t = new Thread(){
            @Override
            public void run() {
                sharedPref= current_Context.getSharedPreferences(SHARED_GROCERY_FILE,MODE_PRIVATE);
                for (GroceryItem g: db_Groceries) {
                    String id = Integer.toString(g.id);
                    String bougthStamp = g.boughtStamp.toString();
                    try{
                        if(sharedPref.getString(id,bougthStamp).contains(id)){

                        }
                        else if(sharedPref.getString(id,bougthStamp).contains(bougthStamp)){

                        }
                    }catch (Exception e){
                        notifyUser("");
                    }
                }
                setUpAlarm();
                super.run();
            }
        };
        t.start();
    }
    public void notifyUser(String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.shoppingicon).
                        setContentTitle("Shoppinglist Update").
                        setContentText("Someone has made changes in the shoppinglist");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
