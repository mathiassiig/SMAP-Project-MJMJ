package examproject.group22.roominator.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mathias on 08-Oct-16.
 */

public class Apartment implements Serializable{

    public int id;
    public String name;
    public String password;
    public ArrayList<GroceryItem> groceries;

    public Apartment(String name, String password)
    {
        this.name = name;
        this.password = name;
    }
}
