package examproject.group22.roominator.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import examproject.group22.roominator.Fragments.DetailFragment;
import examproject.group22.roominator.Models.GroceryItemModel;
import examproject.group22.roominator.R;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        ArrayList<GroceryItemModel> items = (ArrayList<GroceryItemModel>)i.getSerializableExtra("groceries");
        if(items == null)
            items = new ArrayList<>();

        DetailFragment detailFragment = new DetailFragment();
        Bundle b = new Bundle();
        b.putSerializable("groceries", items);
        detailFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_detail, detailFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
