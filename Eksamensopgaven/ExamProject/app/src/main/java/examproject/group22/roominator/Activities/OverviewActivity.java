package examproject.group22.roominator.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import examproject.group22.roominator.NotificationService;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.TabsPagerAdapter;

// KILDER:
// Tabs: https://www.youtube.com/watch?v=zQekzaAgIlQ


public class OverviewActivity extends AppCompatActivity implements UsersFragment.UserItemClickListener,
        ProductListFragment.GroceryItemClickListener,
        DeleteUserFragment.DeleteUserDialogListener,
        DeleteProductFragment.DeleteProductDialogListener{
    
    private static final int NEW_GROCERY_REQUEST = 9001;
    private Apartment currentApartment;
    private int currentApartmentID;
    private ArrayList<GroceryItem> unBoughts;
    private User currentUser;
    private DatabaseService db;
    private int groceryPos;
    private int userPos;

    public static final String INTENT_UPDATE_ALL_DATA = "updateDataInOverview";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.v("OverviewActivity", "OverviewActivity onCreate");
        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_ALL_GROCERIES_IN_APARTMENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver,new IntentFilter(INTENT_UPDATE_ALL_DATA));
        Intent i = getIntent();
        currentApartmentID = i.getIntExtra("apartmentID", 0);
        User u = (User)i.getSerializableExtra("User");
        currentUser = u;
        startNotificationService(currentApartmentID);
        SetUpGui();
    }

    @Override
    public void onResume()
    {
        UpdateAllData(currentApartmentID);
        super.onResume();
    }

    private void UpdateAllData(int apartmentID)
    {
        db.get_Apartment(apartmentID, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {

            SharedPreferences preferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent logoutIntent = new Intent(OverviewActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startNotificationService(int apartmentId){
        Intent notificationService = new Intent(OverviewActivity.this, NotificationService.class);
        notificationService.putExtra("apartmentID", apartmentId);
        startService(notificationService);
        Log.v("Debug","Overview has started notification service");
    }

    private final BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Apartment a = (Apartment)intent.getSerializableExtra("apartment");
            SaveToSP(a);
            ///
            currentApartment = a;
            unBoughts = new ArrayList<>();
            FilterGroceries();
            setTitle(currentApartment.name + " - " + currentUser.name);
            UpdateUserFragment(currentApartment.users);
            UpdateGroceriesFragment(unBoughts);
        }
    };

    private void SaveToSP(Apartment a)
    {
        SharedPreferences sharedPref = OverviewActivity.this.getSharedPreferences("Groceries",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.clear();
        for (GroceryItem g:a.groceries)
        {
            prefEditor.putInt(Integer.toString(g.id),g.buyerID);
        }
        prefEditor.apply();
    }

    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive (Context context, Intent intent)
        {
            UpdateAllData(currentApartmentID);
        }
    };
    public static final String INTENT_UPDATE_USERS_FRAGMENT = "updateUsersFragment";
    private void UpdateUserFragment(ArrayList<User> users)
    {
        Intent intent = new Intent(INTENT_UPDATE_USERS_FRAGMENT);
        intent.putExtra("users", users);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public static final String INTENT_UPDATE_GROCERIES_FRAGMENT = "updateGroceriesFragment";
    private void UpdateGroceriesFragment(ArrayList<GroceryItem> groceries)
    {
        Intent intent = new Intent(INTENT_UPDATE_GROCERIES_FRAGMENT);
        intent.putExtra("groceries", groceries);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void FilterGroceries()
    {
        for(User u: currentApartment.users)
        {
            u.boughtByUser = new ArrayList<>();
        }
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

    private void SetUpGui()
    {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onUserItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(OverviewActivity.this, DetailActivity.class);
        ArrayList<GroceryItem> grocieres = currentApartment.users.get(position).boughtByUser;
        detailIntent.putExtra("groceries", grocieres);
        startActivity(detailIntent);
    }

    @Override
    public void onUserItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        userPos = position;
        User u = currentApartment.users.get(userPos);
        if(u.id == currentUser.id)
            Toast.makeText(this, R.string.dialog_user_cannot_delete, Toast.LENGTH_LONG).show();
        else {
            DialogFragment dialog = new DeleteUserFragment();
            dialog.show(getSupportFragmentManager(), "DeleteUserDialogFragment");
        }
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
        groceryPos = position;
        DialogFragment dialog= new DeleteProductFragment();
        dialog.show(getSupportFragmentManager(),"DeleteProductDialogFragment");
    }

    @Override
    public void onFABClick(View view) {
        Intent addIntent = new Intent(OverviewActivity.this, AddProductActivity.class);
        addIntent.putExtra("ApartmentID", currentApartment.id);
        startActivityForResult(addIntent, NEW_GROCERY_REQUEST);
    }


    @Override
    public void onUserDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        db.delete_user(currentApartment.users.get(userPos));
        Toast.makeText(this, R.string.dialog_user_deleted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserDialogNegativeClick(DialogFragment dialog) {}
    @Override
    public void onProductDialogNegativeClick(DialogFragment dialog) {}

    @Override
    public void onProductDialogPositiveClick(DialogFragment dialog) {
        db.delete_grocery(unBoughts.get(groceryPos).id);
        Toast.makeText(this, R.string.dialog_product_deleted, Toast.LENGTH_LONG).show();
    }


   @Override
    public void onBackPressed()
    {

        this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
