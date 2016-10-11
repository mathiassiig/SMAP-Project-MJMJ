package examproject.group22.roominator.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import examproject.group22.roominator.Fragments.DeleteUserFragment;
import examproject.group22.roominator.Fragments.UsersFragment;
import examproject.group22.roominator.Fragments.ProductListFragment;
import examproject.group22.roominator.Fragments.ProfileFragment;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.TabsPagerAdapter;

// KILDER:
// Tabs: https://www.youtube.com/watch?v=zQekzaAgIlQ


public class OverviewActivity extends AppCompatActivity implements UsersFragment.UserItemClickListener,
        ProductListFragment.GroceryItemClickListener,
        DeleteUserFragment.DeleteUserDialogListener,
        ProfileFragment.OnImageClickListener {

    private static final int REQUEST_IMG_ACTIVITY = 100;
    private static final int REQUEST_PERMISSION_CAM = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onUserItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(OverviewActivity.this, DetailActivity.class);
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
        //String messageproduct = Products[position];
        //int messagenumber = Integer.parseInt(Number[position]);
        buyIntent.putExtra("productnumber", position);
        Toast.makeText(this, "Grocery clicked",Toast.LENGTH_LONG).show();
        startActivity(buyIntent);

    }

    @Override
    public void onFABClick(View view) {
        Intent addIntent = new Intent(OverviewActivity.this, AddProductActivity.class);
        Toast.makeText(this, "Add clicked",Toast.LENGTH_LONG).show();
        startActivity(addIntent);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        //TODO Implement
        Toast.makeText(this, R.string.overview_UserDeleted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        //TODO Implement
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
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
