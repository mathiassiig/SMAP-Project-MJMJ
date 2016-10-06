package examproject.group22.roominator;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OverviewActivity extends AppCompatActivity implements OverviewFragment.OnFragmentInteractionListener {

    private Button btnList;
    private Button btnOverview;
    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        OverviewFragment overviewFragment = new OverviewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, overviewFragment).commit();

        btnList = (Button) findViewById(R.id.overview_btnShoppinglist);
        btnOverview = (Button) findViewById(R.id.overview_btnUserList);
        btnProfile = (Button) findViewById(R.id.overview_btnUserProfile);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, overviewFragment).commit();
            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, overviewFragment).commit();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, overviewFragment).commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
