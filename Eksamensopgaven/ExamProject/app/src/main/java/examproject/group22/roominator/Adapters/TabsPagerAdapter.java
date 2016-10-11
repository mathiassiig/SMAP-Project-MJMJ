package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import examproject.group22.roominator.Fragments.UsersFragment;
import examproject.group22.roominator.Fragments.ProductListFragment;
import examproject.group22.roominator.Fragments.ProfileFragment;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.R;

/**
 * Created by Maria Dam on 09-10-2016.
 */

// KILDER:
// https://www.youtube.com/watch?v=zQekzaAgIlQ


public class TabsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    Apartment currentApartment;
    public TabsPagerAdapter(FragmentManager fragmentManager, Context context, Apartment a){
        super(fragmentManager);
        this.context = context;
        currentApartment = a;
    }

    @Override
    public int getCount(){
        return 3;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new ProfileFragment();
            case 1:
                ProductListFragment plf = new ProductListFragment();
                Bundle b1 = new Bundle();
                b1.putSerializable("apartment", currentApartment);
                plf.setArguments(b1);
                return plf;
            case 2:
                UsersFragment uf = new UsersFragment();
                Bundle b2 = new Bundle();
                b2.putSerializable("apartment", currentApartment);
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
                return context.getResources().getString(R.string.overview_btnUserProfile);
            case 1:
                return context.getResources().getString(R.string.overview_btnShoppinglist);
            case 2:
                return context.getResources().getString(R.string.overview_btnUserList);
            default:
                return null;
        }
    }
}
