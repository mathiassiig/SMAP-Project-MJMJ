package examproject.group22.roominator.Models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jeppemalmberg on 06/10/2016.
 */

public class UserModel implements Serializable {

    public String name;
    public String password;
    public Bitmap image;
    public ArrayList<GroceryItemModel> boughtByUser;
    public int ApartmentID;
    public int id;

    public UserModel(String name, String password, Bitmap image) {
        this.name = name;
        this.password = password;
        this.image = image;
    }

    public static int Total(ArrayList<GroceryItemModel> groceries)
    {
        int total = 0;
        for(GroceryItemModel g : groceries)
        {
            total += g.price;
        }
        return total;
    }

}
