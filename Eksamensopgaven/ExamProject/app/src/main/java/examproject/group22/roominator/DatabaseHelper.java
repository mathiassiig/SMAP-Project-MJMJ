package examproject.group22.roominator;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;

public class DatabaseHelper {

    private static DatabaseHelper sInstance;
    private static String HOST_API = "http://roomienator.azurewebsites.net/api/";
    private static String TABLE_APARTMENTS = "Apartments";
    private static String TABLE_USERS = "Users";
    private static String TABLE_GROCERIES = "GroceryItems";
    private Context current_context;
    public  ResponseParser parser;
    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if (sInstance == null)
            sInstance = new DatabaseHelper(context);
        return sInstance;
    }

    private DatabaseHelper(Context context)
    {
        current_context = context;
        parser = new ResponseParser();
    }

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
                        sendGroceries(groceries);
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

    private void sendGroceries(ArrayList<GroceryItem> groceries)
    {
        //broadcast
    }

    /*
    public void getUsersInApartment(int apartment_id)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.v("DatabaseHelper", "Request didn't work: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }*/

    private void sendUsersInApartment()
    {

    }

    public void broadcastResult()
    {

    }


    //https://developer.android.com/training/volley/simple.html
}
