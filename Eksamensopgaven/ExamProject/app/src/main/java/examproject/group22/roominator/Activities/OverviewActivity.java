package examproject.group22.roominator.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Fragments.DeleteProductFragment;
import examproject.group22.roominator.Fragments.DeleteUserFragment;
import examproject.group22.roominator.Fragments.UsersFragment;
import examproject.group22.roominator.Fragments.ProductListFragment;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.NotifikationService;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.TabsPagerAdapter;
import examproject.group22.roominator.NotifikationService.LocalBinder;

// KILDER:
// Tabs: https://www.youtube.com/watch?v=zQekzaAgIlQ


public class OverviewActivity extends AppCompatActivity implements UsersFragment.UserItemClickListener,
        ProductListFragment.GroceryItemClickListener,
        DeleteUserFragment.DeleteUserDialogListener,
        DeleteProductFragment.DeleteProductDialogListener{
    
    private static final int NEW_GROCERY_REQUEST = 9001;
    public Apartment currentApartment;
    public ArrayList<GroceryItem> unBoughts;
    public User currentUser;
    public DatabaseService db;

    public NotifikationService notificationService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_ALL_GROCERIES_IN_APARTMENT));
        SetupData();
        Intent i = new Intent(OverviewActivity.this,NotifikationService.class);
        bindService(i,serviceConnection,0);
        startNotificationService();
    }
    public void startNotificationService(){
        Intent notificationService = new Intent(OverviewActivity.this, NotifikationService.class);
        startService(notificationService);
    }
    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Apartment a = (Apartment)intent.getSerializableExtra("apartment");

            /// gemmer til sharedpreferences
            SharedPreferences sharedPref = OverviewActivity.this.getSharedPreferences("Groceries",MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            for (GroceryItem g:a.groceries) {
                Log.v("Debug","grocery " + g.id +"has been saved to sharedpref");
                prefEditor.putString(Integer.toString(g.id),Integer.toString(g.buyerID));
            }
            ///
            currentApartment = a;
            unBoughts = new ArrayList<>();
            FilterGroceries();
            SetUpGui();
        }
    };

    private void FilterGroceries()
    {
        for (GroceryItem g: currentApartment.groceries)
        {
            int buyerid = g.buyerID;
            if(buyerid == 0)
            {
                unBoughts.add(g);
            }
            else
            {
                User u = GetUserByID(buyerid);
                if(u.boughtByUser == null)
                    u.boughtByUser = new ArrayList<>();
                u.boughtByUser.add(g);
            }
        }
    }

    private User GetUserByID(int ID)
    {
        for(User u : currentApartment.users)
        {
            if(u.id == ID)
                return u;
        }
        return null;
    }

    PagerAdapter pagerAdapter;
    private void SetUpGui()
    {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),this, unBoughts, currentApartment.users);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void SetupData()
    {
        Intent i = getIntent();
        int apartmentId = i.getIntExtra("apartmentID", 0); //if this is 0 well fuck
        User u = (User)i.getSerializableExtra("User");
        currentUser = u;
        db.get_Apartment(apartmentId);

    }

    @Override
    public void onUserItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(OverviewActivity.this, DetailActivity.class);
        detailIntent.putExtra("groceries", currentApartment.users.get(position).boughtByUser);
        startActivity(detailIntent);
    }

    @Override
    public void onUserItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DialogFragment dialog= new DeleteUserFragment();
        dialog.show(getSupportFragmentManager(),"DeleteUserDialogFragment");
    }

    @Override
    public void onGroceryItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent buyIntent = new Intent(OverviewActivity.this, BuyProductActivity.class);

        buyIntent.putExtra("groceryItem", unBoughts.get(position));
        buyIntent.putExtra("potentialBuyer", currentUser);
        startActivity(buyIntent);
    }

    @Override
    public void onGroceryItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DialogFragment dialog= new DeleteProductFragment();
        dialog.show(getSupportFragmentManager(),"DeleteProductDialogFragment");
    }

    @Override
    public void onFABClick(View view) {
        Intent addIntent = new Intent(OverviewActivity.this, AddProductActivity.class);
        addIntent.putExtra("ApartmentID", currentApartment.id);
        Toast.makeText(this, "Add clicked",Toast.LENGTH_LONG).show();
        startActivityForResult(addIntent, NEW_GROCERY_REQUEST);
    }


    @Override
    public void onUserDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        //TODO Implement
        Toast.makeText(this, R.string.dialog_user_deleted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        //TODO Implement
    }

    @Override
    public void onProductDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        //TODO Implement
        Toast.makeText(this, R.string.dialog_product_deleted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        // TODO Implement
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            LocalBinder binder = (LocalBinder) service;
            notificationService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
