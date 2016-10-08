package examproject.group22.roominator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;

/**
 * Created by Mathias on 08-Oct-16.
 */



public class ResponseParser
{
    public SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public ArrayList<User> parseUsers(String response)
    {
        ArrayList<User> users = new ArrayList<>();
        try
        {
            JSONObject jsonResponse = new JSONObject(response);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<Apartment> parseApartments(String response)
    {
        return null;
    }

    public ArrayList<GroceryItem> parseGroceries(String response)
    {
        ArrayList<GroceryItem> groceries = new ArrayList<GroceryItem>();
        try
        {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                //Must-haves:
                int id = object.getInt("Id");
                String name = object.getString("Name");
                int price = object.getInt("Price");

                String creationString = object.getString("Creation").replace('T', ' ');
                java.util.Date creationDate = TIME_FORMAT.parse(creationString);
                Timestamp creation = new Timestamp(creationDate.getTime());

                int apartmentId = object.getInt("ApartmentID");
                //If it has been bought:
                groceries.add(new GroceryItem(id, name, price, creation, apartmentId));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return groceries;
    }
}
