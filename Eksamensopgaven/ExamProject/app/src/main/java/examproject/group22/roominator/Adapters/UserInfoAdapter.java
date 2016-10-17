package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

/**
 * Created by Maria Dam on 02-10-2016.
 */

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

//public class UserInfoAdapter extends ArrayAdapter<UserInfo> {
public class UserInfoAdapter extends ArrayAdapter<User> {

    public UserInfoAdapter(Context context, ArrayList<User> users)
    {
        super(context, 0, users);
        ArrayList<User> users1 = users;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final User user = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_users, parent, false);

        TextView txtName= (TextView) convertView.findViewById(R.id.customUser_txtName);
        TextView txtTotal= (TextView) convertView.findViewById(R.id.customUser_txtTotal);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.imgUser);

        txtName.setText(user.name);
        txtTotal.setText(getContext().getString(R.string.users_total) + " " + User.Total(user.boughtByUser));

        if(user.image !=null) {
            imgUser.setImageBitmap(user.image);
        }else{
            imgUser.setImageResource(R.drawable.img_placeholder);
        }

        return convertView;
    }
}
