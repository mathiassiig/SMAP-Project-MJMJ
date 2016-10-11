package examproject.group22.roominator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;



public class DatabaseService{

    public static String HOST_API = "http://roomienatorweb3.azurewebsites.net/api/";
    public static String TABLE_APARTMENTS = "Apartments";
    public static String TABLE_USERS = "Users";
    public static String TABLE_GROCERIES = "GroceryItems";

    public static String INTENT_ALL_GROCERIES_IN_APARTMENT = "groceriesApartment";
    public static String INTENT_USER_AUTHENTICATION = "userAuthentication";
    public static String INTENT_APARTMENT_AUTHENTICATION = "apartmentAuthentication";

    private Context current_context;
    public  ResponseParser parser;
    //private final IBinder binder = new LocalBinder();

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("Debug", "onCreate service called");
        return super.onStartCommand(intent, flags, startId);
    }*/

    private static DatabaseService instance;
    protected DatabaseService(Context c)
    {
        parser = new ResponseParser();
        current_context = c;
    }
    public static DatabaseService getInstance(Context c)
    {
        if(instance == null)
            instance = new DatabaseService(c);
        return instance;
    }


    //https://developer.android.com/training/volley/simple.html
    public void get_ApartmentWithGroceries(int apartment_id)
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

    public void get_CheckPassWithApartmentName(final String apartmentName, final String password)
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
                        Apartment a = null;
                        boolean passOK = false;
                        for(int i = 0; i < apartments.size();i++)
                        {
                            Apartment b = apartments.get(i);
                            if(b.name.equals(apartmentName))
                                a = b;
                            if(b.password.equals(password))
                                passOK = true;
                        }
                        sendApartmentAuthenticationAnswer(a, passOK);
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

    private void sendApartmentAuthenticationAnswer(Apartment apartment, boolean passWordOk)
    {
        Intent intent = new Intent(INTENT_APARTMENT_AUTHENTICATION);
        if(apartment != null) {
            intent.putExtra("apartmentID", apartment.id);
        }
        else
        {
            intent.putExtra("apartmentID", 0);
        }
        intent.putExtra("apartmentOK", passWordOk);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    public void get_checkPassWithUsername(final String username, final String password)
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
                            if(b.name.equals(username) && b.password.equals(password)) {
                                areEqual = true;
                                break;
                            }
                        }
                        sendUserAuthenticationAnswer(areEqual);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.v("DatabaseHelper", "Couldn't fetch: " + error.getMessage());
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

    public void post_NewApartment(final Apartment a)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_APARTMENTS;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Name",a.name);
        params.put("Pass",a.password);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Apartment a = parser.ParseSingleApartmentNoGroceries(response);
                        sendApartmentAuthenticationAnswer(a, true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        queue.add(req);
    }

    public void post_NewUser(final User u)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Name",u.name);
        params.put("Pass",u.password);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //TODO: ja det gik fint du hej
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        queue.add(req);
    }

    public void post_NewGrocery(final GroceryItem i)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_GROCERIES;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Name", i.name);
        params.put("Creation", parser.TIME_FORMAT.format(i.creationStamp));
        params.put("ApartmentID", Integer.toString(i.apartmentID));
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //TODO: ja det gik fint du hej
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
        });
        queue.add(req);
    }

    public void put_UpdateGrocery(final GroceryItem i)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_GROCERIES+"/"+i.id;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Price", Integer.toString(i.price));
        params.put("Bought", parser.TIME_FORMAT.format(i.boughtStamp));
        params.put("UserId", Integer.toString(i.buyerID));
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //TODO: ja det gik fint du hej
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
        });
        queue.add(req);
    }

    public void setContext(Context c)
    {
        current_context = c;
    }

    /*
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
    }*/

}
