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

import java.text.ParseException;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("Debug", "onCreate service called");
        return super.onStartCommand(intent, flags, startId);
    }

    //https://developer.android.com/training/volley/simple.html
    public void getApartmentWithGroceries(int apartment_id)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_APARTMENTS+"/"+apartment_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    Apartment a = parser.parseApartmentWithGroceries(response);
                    sendApartmentWithGroceries(a);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
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

    private void sendApartmentWithGroceries(Apartment a)
    {
        Intent intent = new Intent(INTENT_ALL_GROCERIES_IN_APARTMENT);
        Bundle bundle = new Bundle();
        bundle.putSerializable("apartment", a);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    public void checkPassWithApartmentName(final String apartmentName, final String password)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_APARTMENTS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        ArrayList<Apartment> apartments = parser.GetAllApartmentsNoGroceries(response);
                        boolean areEqual = false;
                        for(int i = 0; i < apartments.size();i++)
                        {
                            Apartment b = apartments.get(i);
                            if(b.name.equals(apartmentName) && b.password.equals(password))
                                areEqual = true;
                        }
                        sendApartmentAuthenticationAnswer(areEqual);
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

    private void sendApartmentAuthenticationAnswer(boolean areEqual)
    {
        Intent intent = new Intent(INTENT_APARTMENT_AUTHENTICATION);
        intent.putExtra("areEqual", areEqual);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

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
                        ArrayList<User> users = parser.parseUsers(response, true);
                        boolean areEqual = false;
                        for(int i = 0; i < users.size();i++)
                        {
                            User b = users.get(i);
                            if(b.name.equals(username) && b.password.equals(password))
                                areEqual = true;
                        }
                        sendUserAuthenticationAnswer(areEqual);
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

    private void sendUserAuthenticationAnswer(boolean areEqual)
    {
        Intent intent = new Intent(INTENT_USER_AUTHENTICATION);
        intent.putExtra("areEqual", areEqual);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
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

    @Override
    public void onDestroy() {
        Log.v("Debug", "onDestroy service called");
        super.onDestroy();
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
