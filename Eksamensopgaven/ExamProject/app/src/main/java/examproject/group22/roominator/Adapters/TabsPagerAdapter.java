package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import examproject.group22.roominator.Fragments.UsersFragment;
import examproject.group22.roominator.Fragments.ProductListFragment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

/**
 * Created by Maria Dam on 09-10-2016.
 */

// KILDER:
// https://www.youtube.com/watch?v=zQekzaAgIlQ


public class TabsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    ArrayList<GroceryItem> unboughts;
    ArrayList<User> users;
    public TabsPagerAdapter(FragmentManager fragmentManager, Context context, ArrayList<GroceryItem> unboughts, ArrayList<User> users){
        super(fragmentManager);
        this.context = context;
        this.unboughts = unboughts;
        this.users = users;
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                ProductListFragment plf = new ProductListFragment();
                Bundle b1 = new Bundle();
                b1.putSerializable("unboughts", unboughts);
                plf.setArguments(b1);
                return plf;
            case 1:
                UsersFragment uf = new UsersFragment();
                Bundle b2 = new Bundle();
                b2.putSerializable("users", users);
                uf.setArguments(b2);
                return uf;
            default:
                return null;
        }
    }

    //KILDE: http://stackoverflow.com/questions/20006736/cant-use-method-getstringint-resid-in-fragmentpageradapter
    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return context.getResources().getString(R.string.overview_btnShoppinglist);
            case 1:
                return context.getResources().getString(R.string.overview_btnUserList);
            default:
                return null;
        }
    }
}
