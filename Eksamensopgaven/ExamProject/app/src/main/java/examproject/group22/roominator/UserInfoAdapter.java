package examproject.group22.roominator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Maria Dam on 02-10-2016.
 */

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

//public class UserInfoAdapter extends ArrayAdapter<UserInfo> {
public class UserInfoAdapter extends ArrayAdapter<String> {

    ArrayList<UserInfo> users;
   /* public UserInfoAdapter(Context context, ArrayList<UserInfo> users)
    {
        super(context, 0, users);
        this.users = users;
    }

     public static String getName(String userName)
    {
        //return R.string.customUser_txtName;
        return "TEST_NAVN";
    }

    public static int getTotal(int total)
    {
        return 100;
    }

    public static int getUserImage(String userName)
    {
        return R.drawable.user_image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final UserInfo users = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_users, parent, false);

        TextView txtName= (TextView) convertView.findViewById(R.id.customUser_txtName);
        TextView txtTotal = (TextView) convertView.findViewById(R.id.customUser_txtTotal);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.imgUser);

        txtName.setText(getName(users.userName));
        txtTotal.setText(getTotal(users.totalPrice));
        imgUser.setImageResource(getUserImage(users.userImg));

        return convertView;
    }*/

   public UserInfoAdapter(Context context, String[] names)
   {
       super(context, 0, names);
   }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_users, parent, false);

        String name = getItem(position);

        TextView txtName= (TextView) convertView.findViewById(R.id.customUser_txtName);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.imgUser);

        txtName.setText(name);
        imgUser.setImageResource(R.drawable.img_placeholder);

        return convertView;
    }


}
