package examproject.group22.roominator.Adapters;

import android.content.Context;
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
                return new ProductListFragment();
            case 2:
                return new UsersFragment();
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
