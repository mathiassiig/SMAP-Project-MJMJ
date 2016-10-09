package examproject.group22.roominator;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import examproject.group22.roominator.Fragments.OverviewFragment;

public class OverviewActivity extends AppCompatActivity implements OverviewFragment.ItemClickListener,
        ProductListFragment.OnFragmentInteractionListener2,
        DeleteUserFragment.DeleteUserDialogListener,
        ProfileFragment.OnFragmentInteractionListener{

    // KILDER:
    // Tabs: https://www.youtube.com/watch?v=zQekzaAgIlQ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(OverviewActivity.this, DetailActivity.class);
        startActivity(detailIntent);
        Toast.makeText(this, "Item clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Item long clicked", Toast.LENGTH_LONG).show();
        DialogFragment dialog= new DeleteUserFragment();
        dialog.show(getSupportFragmentManager(),"DeleteUserDialogFragment");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFragmentInteraction2(Uri uri) {
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
}
