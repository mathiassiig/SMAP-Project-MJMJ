package examproject.group22.roominator.Models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jeppemalmberg on 06/10/2016.
 */

public class User implements Serializable {

    public final String name;
    public final String password;
    public final Bitmap image;
    public ArrayList<GroceryItem> boughtByUser;
    public int ApartmentID;
    public int id;

    public User(String name, String password, Bitmap image) {
        this.name = name;
        this.password = password;
        this.image = image;
    }

    public static int Total(ArrayList<GroceryItem> groceries)
    {
        int total = 0;
        for(GroceryItem g : groceries)
        {
            total += g.price;
        }
        return total;
    }

}
