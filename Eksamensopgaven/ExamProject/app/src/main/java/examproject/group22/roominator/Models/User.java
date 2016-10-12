package examproject.group22.roominator.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jeppemalmberg on 06/10/2016.
 */

public class User implements Serializable {

    public String name;
    public String password;
    public Bitmap image;
    public ArrayList<GroceryItem> boughtByUser;
    public int id;

    public User(String name, String password, Bitmap image) {
        this.name = name;
        this.password = password;
        this.image = image;
    }
}
