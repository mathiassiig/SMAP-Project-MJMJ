package examproject.group22.roominator.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

public class BuyProductActivity extends AppCompatActivity {


    private TextView TextviewProductName;
    private EditText editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextviewProductName = (TextView) findViewById(R.id.buyproduct_txtName);
        editTextPrice = (EditText) findViewById(R.id.buyproduct_etPrice);


        Bundle bundle = getIntent().getExtras();
        final GroceryItem i = (GroceryItem)bundle.getSerializable("groceryItem");
        final User u = (User)bundle.getSerializable("potentialBuyer");
        TextviewProductName.setText(i.name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCurrent(i, u);
            }
        });
    }

    private void BuyCurrent(GroceryItem i, User buyer)
    {

    }



}
