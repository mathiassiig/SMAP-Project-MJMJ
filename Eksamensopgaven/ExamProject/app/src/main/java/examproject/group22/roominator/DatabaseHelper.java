package examproject.group22.roominator;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class DatabaseHelper {

    private static DatabaseHelper sInstance;
    private static String HOST_API = "http://roomienator.azurewebsites.net/api/";
    private static String TABLE_APARTMENTS = "Apartments";
    public static synchronized DatabaseHelper getInstance()
    {
        if (sInstance == null)
            sInstance = new DatabaseHelper();
        return sInstance;
    }

    private DatabaseHelper()
    {
    }

    //https://developer.android.com/training/volley/simple.html
    public void SendRequest(Context context)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = HOST_API+TABLE_APARTMENTS;
        //"The most disgusting tabulation you will ever see".java
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Display the first 500 characters of the response string.
                        Log.v("DatabaseHelper", "Response is: " + response.substring(0, 500));
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
    }
}
