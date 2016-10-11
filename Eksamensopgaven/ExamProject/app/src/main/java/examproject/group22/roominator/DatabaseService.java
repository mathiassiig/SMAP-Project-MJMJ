package examproject.group22.roominator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;



public class DatabaseService extends Service {

    public static String HOST_API = "http://localhost:63785/api/";
    public static String TABLE_APARTMENTS = "Apartments";
    public static String TABLE_USERS = "Users";
    public static String TABLE_GROCERIES = "GroceryItems";

    public static String INTENT_ALL_GROCERIES_IN_APARTMENT = "groceriesApartment";
    public static String INTENT_USER_AUTHENTICATION = "userAuthentication";
    public static String INTENT_APARTMENT_AUTHENTICATION = "apartmentAuthentication";

    private Context current_context;
    public  ResponseParser parser;
    private final IBinder binder = new LocalBinder();


    //Spørgsmål til Jesper:
    //Forbindelse fra User til Apartment?
    //Skal der være en seperat tabel til det?
    //Eller skal der fikses noget i CodeFirst/Entity Framework??

    //TODO: Pull groceries every 10 minutes or so? Alarmmanager
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("Debug", "onCreate service called");
        return super.onStartCommand(intent, flags, startId);
    }

    //https://developer.android.com/training/volley/simple.html
    public void getGroceriesInApartment(int apartment_id)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_GROCERIES+"?ApartmentID="+apartment_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                ArrayList<GroceryItem> groceries = parser.parseGroceries(response);
                sendReceivedGroceries(groceries);
            }
        }, new Response.ErrorListener()
        {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            Log.v("DatabaseHelper", "Couldn't fetch groceries: " + error.getMessage());
        }
        });
        queue.add(stringRequest);
    }

    private void sendReceivedGroceries(ArrayList<GroceryItem> groceries)
    {
        Intent intent = new Intent(INTENT_ALL_GROCERIES_IN_APARTMENT);
        Bundle bundle = new Bundle();
        bundle.putSerializable("groceries", groceries);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    public void checkPassWithApartmentName(String apartmentName, final String password)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS+"?Name="+apartmentName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        ArrayList<Apartment> apartments = parser.parseApartments(response,true);
                        Apartment a = apartments.get(0); //there can be only ONE user with that username
                        sendApartmentAuthenticationAnswer(a, password);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.v("DatabaseHelper", "Couldn't fetch groceries: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void sendApartmentAuthenticationAnswer(Apartment a, String passwordGuess)
    {
        boolean same = false;
        if(a.password.equals(passwordGuess))
            same = true;
        Intent intent = new Intent(INTENT_APARTMENT_AUTHENTICATION);
        intent.putExtra("AreEqual", same);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    //omg it's so safe, it's crazy
    public void checkPassWithUsername(final String username, final String password)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        ArrayList<User> users = parser.parseUsers(response,true);
                        User u = null;
                        for(int i =0; i<users.size();i++)
                        {
                            if(users.get(i).name.equals(username))
                            {
                                u = users.get(i);
                            }
                        }

                        sendUserAuthenticationAnswer(u, password);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.v("DatabaseHelper", "Couldn't fetch fisse: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        Log.v("Debug", "onDestroy service called");
        super.onDestroy();
    }

    public void setContext(Context c)
    {
        current_context = c;
    }

    @Override
    public void onCreate()
    {
        parser = new ResponseParser();
        Log.v("Debug", "onCreate service called");
    }

    private void sendUserAuthenticationAnswer(User u, String passwordGuess)
    {
        boolean same = false;
        if(u != null) {
            if (u.password.equals(passwordGuess))
                same = true;
        }
        Intent intent = new Intent(INTENT_USER_AUTHENTICATION);
        intent.putExtra("AreEqual", same);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    // taget fra timens eksempel
    public class LocalBinder extends Binder {
        public DatabaseService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DatabaseService.this;
        }
    }

}
