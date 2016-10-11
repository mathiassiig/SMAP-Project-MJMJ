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
                int id = object.getInt("Id");
                if(withPass)
                    pass = object.getString("Pass");
                User u = new User(name, pass, null);
                u.id = id;
                users.add(u);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return users;
    }

    public Apartment ParseSingleApartmentNoGroceries(JSONObject object)
    {
        Apartment a = null;
        try{
            String name = object.getString("Name");
            String pass = object.getString("Pass");
            int apartmentID = object.getInt("Id");

            a = new Apartment(name, pass);
            a.id = apartmentID;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return a;
    }

    public ArrayList<Apartment> GetAllApartmentsNoGroceries(String response)
    {
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        try{
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("Name");
                String pass = object.getString("Pass");
                int apartmentID = object.getInt("Id");

                Apartment a = new Apartment(name, pass);
                a.id = apartmentID;
                apartments.add(a);

            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return apartments;
    }

    public Apartment parseApartmentWithGroceries(String response) throws ParseException {
        Apartment a = null;
        try
        {
            JSONObject object = new JSONObject(response);
            String name = object.getString("Name");
            String pass = "";
            int apartmentID = object.getInt("Id");

            JSONArray groceries = object.getJSONArray("GroceryItems");
            ArrayList<GroceryItem> groceryItemArrayList = parseGroceries(groceries);

            a = new Apartment(name, pass);
            a.id = apartmentID;
            a.groceries = groceryItemArrayList;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return a;
    }

    private Timestamp GetTimestampFromString(String string) throws ParseException {
        String replaced = string.replace('T',' ');
        java.sql.Date date = new java.sql.Date((TIME_FORMAT.parse(replaced)).getTime());
        return new Timestamp(date.getTime());
    }

    private ArrayList<GroceryItem> parseGroceries(JSONArray groceries) throws JSONException, ParseException {
        ArrayList<GroceryItem> groceryItemArrayList = new ArrayList<GroceryItem>();
        for(int g = 0; g < groceries.length(); g++)
        {
            JSONObject currentGrocery = groceries.getJSONObject(g);
            int g_id = currentGrocery.getInt("Id");
            String g_name = currentGrocery.getString("Name");
            int g_price = currentGrocery.getInt("Price");
            Timestamp g_creation = GetTimestampFromString(currentGrocery.getString("Creation"));
            Timestamp g_bought = null;
            try
            {
                g_bought = GetTimestampFromString(currentGrocery.getString("Bought"));
            }
            catch(Exception ex)
            {

            }
            int ApartmentID = currentGrocery.getInt("ApartmentID");
            int UserID = 0;
            try
            {
                UserID = currentGrocery.getInt("UserID");
            }
            catch(Exception ex)
            {

            }
            GroceryItem grocery = new GroceryItem(g_id, g_name, g_price, g_creation, ApartmentID);
            if(UserID != 0)
                grocery.buyerID = UserID;
            if(g_bought != null)
                grocery.boughtStamp = g_bought;
            groceryItemArrayList.add(grocery);
        }
        return groceryItemArrayList;
    }
}
