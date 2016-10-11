package examproject.group22.roominator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.security.Timestamp;
import java.util.List;

import examproject.group22.roominator.Models.GroceryItem;

public class NotifikationService extends Service {
    SharedPreferences preferences;
    DatabaseService db;
    public NotifikationService() {

    }

    @Override
    public void onCreate() {
        preferences = getSharedPreferences("Groceries",MODE_PRIVATE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public void checkDataBase(List<GroceryItem> groceries){
        for (GroceryItem g: groceries) {
            String id = Integer.toString(g.id);
            try{
                
            }catch (Exception e){

            }
        }
    }
    public void notifyUser(String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.img_placeholder).
                        setContentTitle("My notification").
                        setContentText("Hello World!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
