package examproject.group22.roominator;

import java.io.Serializable;

/**
 * Created by Maria Dam on 02-10-2016.
 */

public class UserInfo implements Serializable{
    public long id;
    public String userName;
    public int totalPrice;
    public String userImg;

    public UserInfo(String name, int total, String image)
    {
        userName = name;
        totalPrice = total;
        userImg = image;
    }
}
