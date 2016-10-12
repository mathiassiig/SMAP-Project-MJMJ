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
    public SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public ArrayList<User> parseUsers(String response, boolean withPass) throws JSONException {
        return getUsersFromJson(new JSONArray(response), withPass);
    }

    private ArrayList<User> getUsersFromJson(JSONArray users, boolean withPass)
    {
        ArrayList<User> usersArray = new ArrayList<User>();
        try
        {
            JSONArray array = users;
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
                usersArray.add(u);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return usersArray;
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

            JSONArray users = object.getJSONArray("Users");
            ArrayList<User> userArrayList = getUsersFromJson(users, false);

            JSONArray groceries = object.getJSONArray("GroceryItems");
            ArrayList<GroceryItem> groceryItemArrayList = parseGroceries(groceries);

            a = new Apartment(name, pass);
            a.id = apartmentID;
            a.groceries = groceryItemArrayList;
            a.users = userArrayList;

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return a;
    }

    private Timestamp GetTimestampFromString(String string) throws ParseException {
        //String replaced = string.replace('T',' ');
        java.util.Date d = TIME_FORMAT.parse(string);
        java.sql.Date date = new java.sql.Date(d.getTime());
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
            int ApartmentID = currentGrocery.getInt("ApartmentID");

            Timestamp g_creation = null;
            Timestamp g_bought = null;
            try { g_creation = GetTimestampFromString(currentGrocery.getString("Creation")); } catch(Exception ex) {}
            try { g_bought = GetTimestampFromString(currentGrocery.getString("Bought")); }     catch(Exception ex) {}
            int UserID = 0;
            try { UserID = currentGrocery.getInt("UserId"); } catch(Exception ex) {}
            GroceryItem grocery = new GroceryItem(g_id, g_name, g_price, g_creation, ApartmentID);
            grocery.buyerID = UserID;
            if(g_bought != null)
                grocery.boughtStamp = g_bought;
            groceryItemArrayList.add(grocery);
        }
        return groceryItemArrayList;
    }
}
