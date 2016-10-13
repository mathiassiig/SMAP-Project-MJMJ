package examproject.group22.roominator.Models;

import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Mathias on 08-Oct-16.
 */

public class GroceryItem implements Serializable
{
    public int id;
    public String name;
    public int price;
    public Timestamp creationStamp;
    public Timestamp boughtStamp;
    public int buyerID;
    public int apartmentID;

    public GroceryItem(int id, String name, int price, Timestamp creationStamp, int apartmentId)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationStamp = creationStamp;
        this.apartmentID = apartmentId;
    }
}
