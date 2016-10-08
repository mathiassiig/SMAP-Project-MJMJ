package examproject.group22.roominator;

import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class OverviewActivity extends AppCompatActivity implements OverviewFragment.LongClickListener, ProductListFragment.OnFragmentInteractionListener2, DeleteUserFragment.DeleteUserDialogListener{

    private Button btnList;
    private Button btnOverview;
    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ProductListFragment productlistFragment = new ProductListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, productlistFragment).commit();

        btnList = (Button) findViewById(R.id.overview_btnShoppinglist);
        btnOverview = (Button) findViewById(R.id.overview_btnUserList);
        btnProfile = (Button) findViewById(R.id.overview_btnUserProfile);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, overviewFragment).commit();

            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, overviewFragment).commit();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, overviewFragment).commit();
            }
        });
    }

    @Override
    public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListView userList = (ListView) findViewById(R.id.overviewList);
        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "On long click listener", Toast.LENGTH_LONG).show();
                //DialogFragment dialog = new DeleteUserFragment();
                //dialog.show(getSupportFragmentManager(), "DeleteUserDialogFragment");
                return false;
            }
        });
    }

    @Override
    public void onFragmentInteraction2(Uri uri) {

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }
}
