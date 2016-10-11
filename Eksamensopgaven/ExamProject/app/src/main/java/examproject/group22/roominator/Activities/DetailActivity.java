package examproject.group22.roominator.Activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import examproject.group22.roominator.Fragments.DetailFragment;
import examproject.group22.roominator.R;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailFragment detailFragment = new DetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_detail, detailFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
