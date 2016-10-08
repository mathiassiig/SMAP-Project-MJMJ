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

    public ArrayList<User> parseUsers(String response, boolean withPass)
    {
        ArrayList<User> users = new ArrayList<User>();
        try
        {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("Name");
                String pass = "";
                //TODO: image
                if(withPass)
                    pass = object.getString("Password");

                users.add(new User(name, pass, null));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<Apartment> parseApartments(String response, boolean withPass)
    {
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        try
        {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("Name");
                String pass = "";
                if(withPass)
                    pass = object.getString("Password");

                apartments.add(new Apartment(name, pass));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return apartments;
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
                Date creationDate = (Date) TIME_FORMAT.parse(object.getString("Creation"));
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
