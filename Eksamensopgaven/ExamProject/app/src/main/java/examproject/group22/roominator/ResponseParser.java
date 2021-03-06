package examproject.group22.roominator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
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
    public final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public ArrayList<User> parseUsers(String response) throws JSONException {
        return getUsersFromJson(new JSONArray(response));
    }

    private ArrayList<User> getUsersFromJson(JSONArray users)
    {
        ArrayList<User> usersArray = new ArrayList<>();
        try
        {
            for (int i = 0; i < users.length(); i++)
            {
                JSONObject object = users.getJSONObject(i);
                usersArray.add(parseOneUser(object));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return usersArray;
    }

    public User parseOneUser(String response) throws JSONException
    {
        return parseOneUser(new JSONObject(response));
    }

    private User parseOneUser(JSONObject object) throws JSONException
    {
        User u = null;
        String name = object.getString("Name");
        String pass = "";
        int id = object.getInt("Id");
        pass = object.getString("Pass");
        int apartmentId = 0;
        try
        {
            apartmentId = object.getInt("ApartmentID");
        }
        catch(Exception ex)
        {

        }
        u = new User(name, pass, null);
        u.ApartmentID = apartmentId;
        u.id = id;
        return u;
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
        ArrayList<Apartment> apartments = new ArrayList<>();
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
            ArrayList<User> userArrayList = getUsersFromJson(users);

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

    private ArrayList<GroceryItem> parseGroceries(JSONArray groceries) throws JSONException {
        ArrayList<GroceryItem> groceryItemArrayList = new ArrayList<>();
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
