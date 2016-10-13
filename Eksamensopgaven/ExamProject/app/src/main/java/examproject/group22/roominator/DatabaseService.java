package examproject.group22.roominator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import examproject.group22.roominator.Models.ApartmentModel;
import examproject.group22.roominator.Models.GroceryItemModel;
import examproject.group22.roominator.Models.UserModel;


public class DatabaseService{

    public static String HOST_API = "http://roomienatorweb3.azurewebsites.net/api/";
    public static String TABLE_APARTMENTS = "Apartments";
    public static String TABLE_USERS = "Users";
    public static String TABLE_GROCERIES = "GroceryItems";

    public static String INTENT_ALL_GROCERIES_IN_APARTMENT = "groceriesApartment";
    public static String INTENT_ALL_GROCERIES_IN_APARTMENT_SERVICE = "groceriesApartmentService";
    public static String INTENT_USER_AUTHENTICATION = "userAuthentication";
    public static String INTENT_APARTMENT_AUTHENTICATION = "apartmentAuthentication";
    public static String INTENT_USER = "userIntent";
    public static String INTENT_TRY_MAKING_NEW_USER = "tryNewUser";

    private Context current_context;
    public  ResponseParser parser;

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
    public void get_Apartment(int apartment_id, final boolean forService)
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
                    ApartmentModel a = parser.parseApartmentWithGroceries(response);
                    saveGroceriesToPref(a.groceries);
                    sendApartmentWithGroceries(a, forService);
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

    private void sendApartmentWithGroceries(ApartmentModel a, boolean forService)
    {
        Intent intent = new Intent(INTENT_ALL_GROCERIES_IN_APARTMENT);
        if(forService)
            intent = new Intent(INTENT_ALL_GROCERIES_IN_APARTMENT_SERVICE);
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
                        ArrayList<ApartmentModel> apartments = parser.GetAllApartmentsNoGroceries(response);
                        ApartmentModel a = null;
                        boolean passOK = false;
                        for(int i = 0; i < apartments.size();i++)
                        {
                            ApartmentModel b = apartments.get(i);
                            if(b.name.equals(apartmentName))
                            {
                                a = b;
                                if (b.password.equals(password))
                                {
                                    passOK = true;
                                    break;
                                }
                            }
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

    private void sendApartmentAuthenticationAnswer(ApartmentModel apartment, boolean passWordOk)
    {
        Intent intent = new Intent(INTENT_APARTMENT_AUTHENTICATION);
        int apartmentID = 0;
        if(apartment != null)
        {
            apartmentID = apartment.id;
        }
        intent.putExtra("apartmentID", apartmentID);
        intent.putExtra("apartmentOK", passWordOk);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    public void get_user(final int id)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS+"/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            UserModel u = parser.parseOneUser(response);
                            sendUserAnswer(u);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void sendUserAnswer(UserModel u)
    {
        Intent intent = new Intent(INTENT_USER);
        intent.putExtra("UserModel", u);
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
                        ArrayList<UserModel> users = null;
                        try {
                            users = parser.parseUsers(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        UserModel a = null;
                        for(int i = 0; i < users.size();i++)
                        {
                            UserModel b = users.get(i);
                            if(b.name.equals(username) && b.password.equals(password)) {
                                a = b;
                                break;
                            }
                        }
                        sendUserAuthenticationAnswer(a);
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

    private void sendUserAuthenticationAnswer(UserModel u)
    {
        Intent intent = new Intent(INTENT_USER_AUTHENTICATION);
        intent.putExtra("UserModel", u);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    public void post_NewApartment(final ApartmentModel a)
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
                        ApartmentModel a = parser.ParseSingleApartmentNoGroceries(response);
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

    public void delete_user(final int user_id)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS+"/"+user_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
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

    public void delete_grocery(final int grocery_id)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_GROCERIES+"/"+grocery_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
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

    private void get_AllUsersToCheckIfExists(final UserModel u)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            ArrayList<UserModel> users = parser.parseUsers(response);
                            checkIfUserExists(users, u);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

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

    public void try_AddingNewUser(final UserModel u)
    {
        get_AllUsersToCheckIfExists(u);
    }

    private void checkIfUserExists(ArrayList<UserModel> allUsers, UserModel userToCheck)
    {
        boolean exists = false;
        for (UserModel u: allUsers)
        {
            if(u.name.equals(userToCheck.name))
                exists = true;
        }
        if(exists)
        {
            sendNewUserMessage(userToCheck.name, exists);
        }
        else
        {
            post_NewUser(userToCheck);
        }
    }

    private void sendNewUserMessage(String username, boolean exists)
    {
        Intent intent = new Intent(INTENT_TRY_MAKING_NEW_USER);
        intent.putExtra("username", username);
        intent.putExtra("exists", exists);
        LocalBroadcastManager.getInstance(current_context).sendBroadcast(intent);
    }

    private void post_NewUser(final UserModel u)
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
                        sendNewUserMessage(u.name, false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });
        queue.add(req);
    }

    public void put_userToApartment(final UserModel u, final int apartmentID)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_USERS+"/"+u.id;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Id", Integer.toString(u.id));
        params.put("Name", u.name);
        params.put("Pass", u.password);
        params.put("ApartmentID", Integer.toString(apartmentID));
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

    public void post_NewGrocery(final String name, final Timestamp creation, final int apartmentID)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_GROCERIES;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Name", name);
        params.put("Creation", parser.TIME_FORMAT.format(creation));
        params.put("ApartmentID", Integer.toString(apartmentID));
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //TODO: ja det gik fint du hej
                        //Opdater Overview?
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
        });
        queue.add(req);
    }

    public void put_UpdateGrocery(final GroceryItemModel i)
    {
        RequestQueue queue = Volley.newRequestQueue(current_context);
        String url = HOST_API+TABLE_GROCERIES+"/"+i.id;
        Map<String,String> params = new HashMap<String, String>();
        params.put("Id", Integer.toString(i.id));
        params.put("Name", i.name);
        params.put("Price", Integer.toString(i.price));
        params.put("Creation", parser.TIME_FORMAT.format(i.creationStamp));
        params.put("Bought", parser.TIME_FORMAT.format(i.boughtStamp));
        params.put("UserId", Integer.toString(i.buyerID));
        params.put("ApartmentID", Integer.toString(i.apartmentID));
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

    public void saveGroceriesToPref(List<GroceryItemModel> groceries){
        try {
            SharedPreferences sharedPref = current_context.getSharedPreferences("Groceries", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            for (GroceryItemModel g : groceries) {
                String id = Integer.toString(g.id);
                String boughtStamp = g.boughtStamp.toString();
                editor.putString(id, boughtStamp);
                editor.apply();
            }
        }catch (Exception e){
            Log.v("Debug",e.toString());
        }
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
