package examproject.group22.roominator.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Fragments.DeleteProductFragment;
import examproject.group22.roominator.Fragments.DeleteUserFragment;
import examproject.group22.roominator.Fragments.UsersFragment;
import examproject.group22.roominator.Fragments.ProductListFragment;
import examproject.group22.roominator.Fragments.ProfileFragment;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.TabsPagerAdapter;

// KILDER:
// Tabs: https://www.youtube.com/watch?v=zQekzaAgIlQ


public class OverviewActivity extends AppCompatActivity implements UsersFragment.UserItemClickListener,
        ProductListFragment.GroceryItemClickListener,
        DeleteUserFragment.DeleteUserDialogListener,
        DeleteProductFragment.DeleteProductDialogListener,
        ProfileFragment.OnImageClickListener {

    private static final int REQUEST_IMG_ACTIVITY = 100;
    private static final int REQUEST_PERMISSION_CAM = 200;
    private static final int NEW_GROCERY_REQUEST = 9001;
    public Apartment currentApartment;
    public ArrayList<GroceryItem> unBoughts;
    public User currentUser;
    public DatabaseService db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_ALL_GROCERIES_IN_APARTMENT));
        SetupData();
    }

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Apartment a = (Apartment)intent.getSerializableExtra("apartment");
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
    public void onImageClick(View view) {
        takePicture();
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


    public void takePicture(){
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (ContextCompat.checkSelfPermission(OverviewActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OverviewActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAM);
        } else {
            startActivityForResult(camIntent, REQUEST_IMG_ACTIVITY);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case REQUEST_IMG_ACTIVITY:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    imgView.setImageBitmap(imageBitmap);
                    Toast.makeText(this, R.string.toastSave, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.toastCancel, Toast.LENGTH_SHORT).show();
                }
                break;*/
            case NEW_GROCERY_REQUEST:
                if(resultCode==AddProductActivity.RESULT_ADDED)
                {
                    this.recreate();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


}
